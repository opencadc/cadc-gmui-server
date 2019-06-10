/*
 ************************************************************************
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 *
 * (c) 2010.                         (c) 2010.
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
 * Sep 3, 2010 - 3:15:53 PM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */

package ca.nrc.cadc.groups.web.restlet.resources;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import java.net.URISyntaxException;
import javax.print.URIException;
import org.json.JSONWriter;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.WriterRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import ca.nrc.cadc.ac.*;
import ca.nrc.cadc.groups.web.view.GMSView;
import ca.nrc.cadc.groups.web.view.json.JSONGroupViewImpl;


/**
 * Abstract resource to deal with a single Group. No methods will be
 * mapped to this particular class.
 * <p>
 * POST - Update this Group's Description.
 * GET - Get this Group's details.
 */
public class GroupResource extends AbstractResource {
    final static String GROUP_NAME_FIELD = "group-name";
    final static String GROUP_DESCRIPTION_FIELD = "group-description";


    /**
     * Obtain the default representation of a Group.
     *
     * @return A Representation of this Group.
     * @throws ca.nrc.cadc.ac.GroupNotFoundException Group isn't found.
     * @throws java.io.IOException                   Server error.
     */
    @Get("json")
    public Representation represent() throws Exception {
        final Group group = getGroup();
        final boolean hasOwnerRights = currentUserHasOwnerRights(group);
        final boolean hasAdminRights = currentUserHasAdminRights(group);

        return new WriterRepresentation(MediaType.APPLICATION_JSON) {
            @Override
            public void write(final Writer writer) throws IOException {
                final JSONWriter jsonWriter = new JSONWriter(writer);
                final GMSView<Group> view =
                    new JSONGroupViewImpl(jsonWriter, hasOwnerRights, hasAdminRights);
                try {
                    view.write(group);
                } catch (Exception e) {
                    throw new IOException(e);
                }
            }
        };
    }

    /**
     * Accept a POST request from the UI page. Unfortunately, HTML forms
     * only support GET and POST, so this POST is to accommodate submits
     * from those forms.
     *
     * @param entity The Request payload.
     * @throws ca.nrc.cadc.ac.GroupNotFoundException Group isn't found.
     * @throws java.io.IOException                   Server error.
     */
    @Post
    public void accept(final Representation entity)
        throws GroupNotFoundException, UserNotFoundException,
        WriterException, IOException, URISyntaxException, ReaderException {
        final Form form = getForm(entity);
        update(form);
    }

    /**
     * Update the current group.
     *
     * @param form The form containing data.
     * @throws ca.nrc.cadc.ac.GroupNotFoundException Group isn't found.
     * @throws java.io.IOException                   Server error.
     */
    void update(final Form form) throws GroupNotFoundException,
        UserNotFoundException,
        WriterException,
        IOException,
        URISyntaxException,
        ReaderException {
        final Group group = getGroup();
        updateGroup(group, form);
        getGMSClient().updateGroup(group);
    }

    /**
     * Delete the current Group.
     *
     * @throws ca.nrc.cadc.ac.GroupNotFoundException Group isn't found.
     * @throws java.io.IOException                   Server error.
     */
    @Delete
    public void remove() throws GroupNotFoundException, IOException {
        getGMSClient().deleteGroup(getGroupName());
    }

    /**
     * Build up the given group with form elements.  This will only update
     * the description field in this case.
     *
     * @param group The Group to build up.
     * @param form  The form submitted.
     */
    private void updateGroup(final Group group, final Form form) {
        group.description = form.getFirstValue(GROUP_DESCRIPTION_FIELD);
    }
}
