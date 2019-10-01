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
 * Sep 2, 2010 - 3:38:59 PM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */

package ca.nrc.cadc.groups.web.restlet.resources;

import javax.security.auth.Subject;
import java.io.IOException;
import java.io.Writer;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.util.*;
import java.util.concurrent.*;

import ca.nrc.cadc.ac.Group;
import ca.nrc.cadc.ac.GroupAlreadyExistsException;
import ca.nrc.cadc.ac.Role;
import ca.nrc.cadc.ac.User;
import ca.nrc.cadc.ac.UserNotFoundException;
import ca.nrc.cadc.ac.WriterException;
import ca.nrc.cadc.groups.web.WebGroupURI;
import ca.nrc.cadc.util.ObjectUtil;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONWriter;
import org.opencadc.gms.GroupURI;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.restlet.representation.WriterRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;

import ca.nrc.cadc.dali.tables.TableData;
import ca.nrc.cadc.dali.tables.TableWriter;
import ca.nrc.cadc.dali.tables.ascii.AsciiTableWriter;
import ca.nrc.cadc.dali.tables.votable.VOTableDocument;
import ca.nrc.cadc.dali.tables.votable.VOTableField;
import ca.nrc.cadc.dali.tables.votable.VOTableResource;
import ca.nrc.cadc.dali.tables.votable.VOTableTable;
import ca.nrc.cadc.groups.web.IllegalInputException;
import ca.nrc.cadc.groups.web.view.json.JSONGroupViewImpl;
import ca.nrc.cadc.util.ArrayUtil;
import ca.nrc.cadc.util.StringUtil;


/**
 * Resource to handle all Group List functionality. This resource
 * interacts with the GMS Client.
 */
public class GroupListResource extends AbstractResource {
    private final static Logger LOGGER = Logger.getLogger(GroupListResource.class);

    private final static Role[] ROLES_TO_QUERY =
        new Role[]
            {
                Role.MEMBER, Role.ADMIN, Role.OWNER
            };

    @Get
    public Representation represent() throws Exception {
        LOGGER.debug("===> GroupListResource.represent().");
        // get the resource and return nothing if this fails or
        // if this is an HTTP HEAD request
        return (getMethod().equals(Method.HEAD)) ? null : representCSV();
    }

    @Get("text/csv")
    public Representation representCSV() throws Exception {
        // Get the groups here to be able to have any underlying exceptions
        // thrown from here, rather than from the anonymous class below where
        // it will not propagate back to the client.
        //
        // jenkinsd 2014.09.10
        //
        final Map<Group, Role[]> groupRoles = getGroups();

        LOGGER.debug("Found " + groupRoles.size() + " groups.");

        return new WriterRepresentation(MediaType.TEXT_CSV) {
            @Override
            public void write(final Writer writer) throws IOException {
                final TableWriter<VOTableDocument> tableWriter =
                    new AsciiTableWriter(AsciiTableWriter.ContentType.CSV);
                final VOTableDocument doc = new VOTableDocument();
                final VOTableResource res = new VOTableResource("results");
                final VOTableTable tab = new VOTableTable();

                res.setTable(tab);
                doc.getResources().add(res);

                // Fields added here should correspond to fields return
                // in the groupRow
                tab.getFields().add(new VOTableField("Name", "char"));
                tab.getFields().add(new VOTableField("Owner", "char"));

                // These two fields represent the ability to have the link
                // to the members dialog.
                tab.getFields().add(new VOTableField("Admin", "char"));
                tab.getFields().add(new VOTableField("Member", "char"));
                tab.getFields().add(new VOTableField("Description", "char"));

                // Used internally.
                tab.getFields().add(new VOTableField("OwnerRights", "char"));
                tab.getFields().add(new VOTableField("AdminRights", "char"));

                tab.setTableData(new TableData() {
                    @Override
                    public Iterator<List<Object>> iterator() {
                        final SortedSet<Map.Entry<Group, Role[]>> sortedGroupRoles =
                            new TreeSet<>(
                                new Comparator<Map.Entry<Group, Role[]>>() {
                                    @Override
                                    public int compare(
                                        Map.Entry<Group, Role[]> o1,
                                        Map.Entry<Group, Role[]> o2) {
                                        int keyCompare = o1.getKey().getID().toString().compareTo(o2.getKey().getID()
                                                                                                    .toString());

                                        if (keyCompare == 0) {
                                            keyCompare += Integer.compare(o1.getValue().length, o2.getValue().length);
                                        }

                                        return keyCompare;
                                    }
                                });

                        sortedGroupRoles.addAll(groupRoles.entrySet());

                        LOGGER.debug("Sorted into " + sortedGroupRoles.size());

                        final Iterator<Map.Entry<Group, Role[]>> iter = sortedGroupRoles.iterator();

                        return new Iterator<List<Object>>() {
                            @Override
                            public boolean hasNext() {
                                return iter.hasNext();
                            }

                            @Override
                            public List<Object> next() {
                                final List<Object> groupRow = new ArrayList<>();
                                final Map.Entry<Group, Role[]> entry = iter.next();
                                final Group group = entry.getKey();
                                final GroupURI groupID = group.getID();
                                final String description = group.description;
                                final User owner = group.getOwner();
                                final String ownerName;

                                if (owner.personalDetails == null) {
                                    ownerName = owner.getHttpPrincipal().getName();
                                } else {
                                    ownerName = owner.personalDetails
                                        .getFirstName()
                                        + " "
                                        + owner.personalDetails
                                        .getLastName();
                                }

                                // Fields added here should also be added in
                                // the headers above...
                                groupRow.add(groupID.getName());
                                groupRow.add(ownerName);
                                groupRow.add(isAdmin(entry.getValue()));
                                groupRow.add(true);

                                // Sanitize the data here.
                                groupRow.add(StringUtil.hasText(description)
                                                 ? description.replaceAll("\n", "")
                                                              .replaceAll("\r", "")
                                                 : "");

                                groupRow.add(ArrayUtil.contains(Role.OWNER, entry.getValue()));
                                groupRow.add(isAdmin(entry.getValue()));

                                return groupRow;
                            }

                            boolean isAdmin(final Role[] roles) {
                                return (ArrayUtil.contains(Role.ADMIN, roles) || ArrayUtil.contains(Role.OWNER, roles));
                            }

                            @Override
                            public void remove() {
                                iter.remove();
                            }

                        };
                    }
                });
                tableWriter.write(doc, writer);
            }
        };
    }


    /**
     * Store a new Group.
     *
     * @param entity The Request payload.
     * @throws java.io.IOException                        If the web service could not complete
     *                                                    the write.
     * @throws ca.nrc.cadc.ac.GroupAlreadyExistsException Self explanatory.
     */
    @Post
    public void store(final Representation entity)
            throws GroupAlreadyExistsException, UserNotFoundException,
                   WriterException, IOException {
        final Form form = getForm(entity);
        final String groupName =
            form.getFirstValue(GroupResource.GROUP_NAME_FIELD);

        if (!StringUtil.hasText(groupName)) {
            final String message = "Group name is required.";
            throw new IllegalStateException(message);
        } else {
            final Group newGroup = createGroup(groupName);
            newGroup.description = form.getFirstValue(
                GroupResource.GROUP_DESCRIPTION_FIELD);

            // Add the currently logged in user each time as default.
            newGroup.getUserMembers().add(getCurrentUser());
            final Group createdGroup = getGMSClient().createGroup(newGroup);

            getResponse().setEntity(new WriterRepresentation(
                MediaType.APPLICATION_JSON) {
                @Override
                public void write(final Writer writer) throws IOException {
                    final JSONWriter jsonWriter = new JSONWriter(writer);
                    final JSONGroupViewImpl groupWriter =
                        new JSONGroupViewImpl(jsonWriter, true, true);

                    try {
                        groupWriter.write(createdGroup);
                    } catch (JSONException e) {
                        throw new IOException(e);
                    }
                }
            });
        }
    }


    /**
     * Obtain a new Group instance.
     *
     * @param groupName The name of the new Group.
     * @return The Group instance.
     */
    private Group createGroup(final String groupName)
        throws IOException, UserNotFoundException {
        try {
            final Group group = new Group(new WebGroupURI(groupName));
            ObjectUtil.setField(group, getCurrentUser(), "owner");
            return group;
        } catch (IllegalArgumentException e) {
            throw new IllegalInputException("BAD_GROUP_NAME");
        }
    }

    /**
     * Obtain a mapping of Groups and the Roles the current user plays in each.
     *
     * @return Map of Groups to Roles, or empty Map.  Never null.
     * @throws java.lang.InterruptedException If the underlying thread died.
     */
    private Map<Group, Role[]> getGroups() throws InterruptedException {
        return assembleGroups();
    }

    /**
     * Threaded calls to assemble three calls to AC.
     *
     * @return Map of Groups to this user's Role(s) to each of them.
     * @throws InterruptedException If any of the query threads stop.
     */
    private Map<Group, Role[]> assembleGroups() throws InterruptedException {
        final ConcurrentMap<Group, Role[]> concurrentMap =
            new ConcurrentHashMap<>();
        final ExecutorService executorService =
            Executors.newFixedThreadPool(ROLES_TO_QUERY.length);
        final Subject subject = getSubject();
        final Map<Role, Future<Void>> futureMap =
            new HashMap<>(ROLES_TO_QUERY.length);

        try {
            for (final Role role : ROLES_TO_QUERY) {
                // Using a Callable here
                final Future<Void> future = executorService.submit(
                    new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            LOGGER.debug("Querying for " + role);
                            return Subject.doAs(subject,
                                                new PrivilegedExceptionAction<Void>() {
                                                    /**
                                                     * Performs the computation.  This method will be called by
                                                     * {@code AccessController.doPrivileged} after enabling privileges.
                                                     *
                                                     * @return a class-dependent value that may represent the results
                                                     * of the
                                                     * computation. Each class that implements
                                                     * {@code PrivilegedAction}
                                                     * should document what (if anything) this value represents.
                                                     * @see AccessController#doPrivileged(PrivilegedAction)
                                                     * @see AccessController#doPrivileged(PrivilegedAction,
                                                     * AccessControlContext)
                                                     */
                                                    @Override
                                                    public Void run() throws Exception {
                                                        final List<Group> roleGroups =
                                                            getGMSClient()
                                                                .getMemberships(role);

                                                        for (final Group g : roleGroups) {
                                                            if (concurrentMap.containsKey(g)) {
                                                                final Role[] existing = concurrentMap.get(g);
                                                                final Set<Role> newRoles =
                                                                    new HashSet<>(Arrays.asList(existing));

                                                                newRoles.add(role);

                                                                concurrentMap.put(g, newRoles.toArray(
                                                                    new Role[0]));
                                                            } else {
                                                                concurrentMap.put(g, new Role[] {role});
                                                            }
                                                        }

                                                        LOGGER.debug("Found " + roleGroups.size()
                                                                         + " for " + role);

                                                        return null;
                                                    }
                                                });
                        }
                    });

                futureMap.put(role, future);
            }
        } finally {
            executorService.shutdown();
            if (!executorService.awaitTermination(20, TimeUnit.SECONDS)) {
                LOGGER.info("Terminating threads after 20 seconds!");
                final List<Runnable> stillBirths =
                    executorService.shutdownNow();

                for (final Runnable runnable : stillBirths) {
                    LOGGER.info(runnable + " never started.");
                }
            }
        }

        for (final Map.Entry<Role, Future<Void>> futureEntry
            : futureMap.entrySet()) {
            final Future<Void> f = futureEntry.getValue();

            Subject.doAs(subject,
                         new PrivilegedAction<Void>() {
                             @Override
                             public Void run() {
                                 try {
                                     return f.get();
                                 } catch (ExecutionException e) {
                                     throw new IllegalStateException(
                                         e.getCause());
                                 } catch (InterruptedException e) {
                                     throw new ResourceException(e);
                                 }
                             }
                         });
        }

        LOGGER.debug("Returning " + concurrentMap.size() + " groups.");

        return concurrentMap;
    }
}
