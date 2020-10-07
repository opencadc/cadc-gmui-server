/*
 ************************************************************************
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 *
 * (c) 2014.                         (c) 2014.
 * National Research Council            Conseil national de recherches
 * Ottawa, Canada, K1A 0R6              Ottawa, Canada, K1A 0R6
 * All rights reserved                  Tous droits reserves
 *
 * NRC disclaims any warranties         Le CNRC denie toute garantie
 * expressed, implied, or statu-        enoncee, implicite ou legale,
 * tory, of any kind with respect       de quelque nature que se soit,
 * to the software, including           concernant le logiciel, y com-
 * without limitation any war-          pris sans restriction toute
 * ranty of merchantability or          garantie de valeur marchande
 * fitness for a particular pur-        ou de pertinence pour un usage
 * pose.  NRC shall not be liable       particulier.  Le CNRC ne
 * in any event for any damages,        pourra en aucun cas etre tenu
 * whether direct or indirect,          responsable de tout dommage,
 * special or general, consequen-       direct ou indirect, particul-
 * tial or incidental, arising          ier ou general, accessoire ou
 * from the use of the software.        fortuit, resultant de l'utili-
 *                                      sation du logiciel.
 *
 *
 * @author jenkinsd
 * 9/17/14 - 11:21 AM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */

package ca.nrc.cadc.groups.web.restlet.resources;

import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.*;

import ca.nrc.cadc.groups.web.GroupAssociatesIterator;
import ca.nrc.cadc.groups.web.WebGroupURI;
import org.json.JSONException;
import org.json.JSONWriter;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.WriterRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import ca.nrc.cadc.ac.*;
import ca.nrc.cadc.auth.HttpPrincipal;
import ca.nrc.cadc.dali.tables.TableData;
import ca.nrc.cadc.dali.tables.TableWriter;
import ca.nrc.cadc.dali.tables.ascii.AsciiTableWriter;
import ca.nrc.cadc.dali.tables.votable.VOTableDocument;
import ca.nrc.cadc.dali.tables.votable.VOTableField;
import ca.nrc.cadc.dali.tables.votable.VOTableResource;
import ca.nrc.cadc.dali.tables.votable.VOTableTable;
import ca.nrc.cadc.groups.web.Associate;
import ca.nrc.cadc.groups.web.view.json.JSONGroupViewImpl;
import ca.nrc.cadc.groups.web.view.json.JSONMemberViewImpl;


public class GroupAdminListResource extends AbstractResource {

    @Get("txt|text")
    public Representation representCSV() throws Exception {
        final Group group = getGroup();
        final boolean hasOwnerRights = currentUserHasOwnerRights(group);
        final boolean hasAdminRights = currentUserHasAdminRights(group);

        return new WriterRepresentation(MediaType.TEXT_CSV) {
            @Override
            public void write(final Writer writer) throws IOException {
                final TableWriter<VOTableDocument> tableWriter = new AsciiTableWriter(AsciiTableWriter.ContentType.CSV);
                final VOTableDocument doc = new VOTableDocument();
                final VOTableResource res = new VOTableResource("results");
                final VOTableTable tab = new VOTableTable();

                res.setTable(tab);
                doc.getResources().add(res);

                // Fields added here should correspond to fields return
                // in the groupRow
                tab.getFields().add(new VOTableField("ID", "char"));
                tab.getFields().add(new VOTableField("Name", "char"));
                tab.getFields().add(new VOTableField("AdminID", "char"));
                tab.getFields().add(new VOTableField("Type", "char"));

                // Used internally.
                tab.getFields().add(new VOTableField("OwnerRights", "char"));
                tab.getFields().add(new VOTableField("AdminRights", "char"));

                tab.setTableData(new TableData() {
                    /**
                     * An iterator over the rows in the table. Each row is
                     * returned by one call to Iterator.next() as a List of
                     * Object(s).
                     *
                     * @return iterator over the table rows
                     */
                    @Override
                    public Iterator<List<Object>> iterator() {
                        final SortedSet<User> sortedUserAdmins = new TreeSet<>(
                                Comparator.comparing(o -> o.getHttpPrincipal().getName()));

                        sortedUserAdmins.addAll(group.getUserAdmins());
                        final SortedSet<Group> sortedGroupAdmins =
                                new TreeSet<>(Comparator.comparing(o -> o.getID().toString()));
                        final Iterator<User> userIterator = sortedUserAdmins.iterator();

                        sortedGroupAdmins.addAll(group.getGroupAdmins());
                        final Iterator<Group> groupIterator = sortedGroupAdmins.iterator();

                        return new GroupAssociatesIterator(userIterator, groupIterator, group.getID(), hasOwnerRights,
                                                           hasAdminRights);
                    }
                });

                tableWriter.write(doc, writer);
            }
        };
    }

    /**
     * Obtain the collection of Administrators for the current Group.
     *
     * @return Representation of Administrators for this Group.
     *
     * @throws GroupNotFoundException Group isn't found.
     * @throws UserNotFoundException  User being added is not found.
     * @throws java.io.IOException    Server error.
     */
    @Get("json")
    public Representation representJSON() throws GroupNotFoundException,
                                                 UserNotFoundException,
                                                 IOException {
        final Group group = getGroup();
        final boolean hasOwnerRights = currentUserHasOwnerRights(group);
        final boolean hasAdminRights = currentUserHasAdminRights(group);

        return new WriterRepresentation(MediaType.APPLICATION_JSON) {
            @Override
            public void write(final Writer writer) throws IOException {
                final JSONWriter jsonWriter = new JSONWriter(writer);

                try {
                    jsonWriter.object();
                    jsonWriter.key("groupName").value(group.getID().getName());
                    jsonWriter.key("groupRole").value("");
                    jsonWriter.key("admins");
                    writeAdministrators(jsonWriter, group, hasOwnerRights, hasAdminRights);
                    jsonWriter.endObject();
                } catch (JSONException e) {
                    throw new IOException(e);
                }
            }
        };
    }

    private void writeAdministrators(final JSONWriter jsonWriter, final Group group,
                                     final boolean hasOwnerRights, final boolean hasAdminRights) throws JSONException {
        jsonWriter.array();

        try {
            group.getUserAdmins().forEach(
                    u -> new JSONMemberViewImpl(jsonWriter, hasOwnerRights, hasAdminRights).write(u));
            group.getGroupAdmins().forEach(
                    g -> new JSONGroupViewImpl(jsonWriter, hasOwnerRights, hasAdminRights).write(g));
        } finally {
            jsonWriter.endArray();
        }
    }

    /**
     * POST to add a group admin.
     *
     * @param entity The entity containing the User or Group to add.
     * @throws GroupNotFoundException       The Group to add does not exist.
     * @throws UserNotFoundException        The User to add does not exist.
     * @throws WriterException              Writing failed.
     * @throws IOException                  Any I/O or server failure.
     * @throws ReaderException              Reading the Group/User XML failed.
     * @throws URISyntaxException           URI for the Group to update is invalid.
     * @throws MemberAlreadyExistsException The Group or User being added already exists.
     */
    @Post
    public void accept(final Representation entity) throws GroupNotFoundException, UserNotFoundException,
                                                           WriterException, IOException, ReaderException,
                                                           URISyntaxException, MemberAlreadyExistsException {
        final Associate associate = getAssociate(entity);
        final Group group = getGroup();

        if (addSuccessful(group, associate)) {
            getGMSClient().updateGroup(group);
        } else {
            throw new MemberAlreadyExistsException(String.format("Administrator with ID '%s' already exists.",
                                                                 associate.getID()));
        }
    }

    private boolean addSuccessful(final Group group, final Associate associate) {
        if (associate.isUser()) {
            final User user = new User();
            user.getIdentities().add(new HttpPrincipal(associate.getID()));

            return group.getUserAdmins().add(user);
        } else {
            return group.getGroupAdmins().add(new Group(new WebGroupURI(associate.getID())));
        }
    }
}
