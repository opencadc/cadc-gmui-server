/*
 ************************************************************************
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 *
 * (c) 2019.                         (c) 2019.
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
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */

package ca.nrc.cadc.groups.web.restlet.resources;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ca.nrc.cadc.auth.SSLUtil;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONWriter;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.WriterRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;

import ca.nrc.cadc.groups.web.Associate;
import ca.nrc.cadc.groups.web.AssociateType;
import ca.nrc.cadc.groups.web.suggest.Suggester;
import ca.nrc.cadc.groups.web.suggest.SuggestionResults;
import ca.nrc.cadc.groups.web.view.json.JSONAssociateViewImpl;

import javax.security.auth.Subject;


public class AssociationsResource extends AbstractResource {
    private final static Logger LOGGER = Logger.getLogger(AssociationsResource.class);

    private final static Set<Associate> ASSOCIATE_CACHE =
                                Collections.newSetFromMap(new ConcurrentHashMap<Associate, Boolean>());

    // Fifteen minutes.  Used in the Spring configuration.
    public final static long DEFAULT_CACHE_REFRESH_PERIOD_MS = 30 * 60 * 1000;

    private final Suggester<Associate> suggester;
    private AssociationsCacheState associationsCacheState =
        AssociationsCacheState.INIT;

    /**
     * Default constructor.
     *
     * @param suggester The suggester to use to match entries.
     */
    public AssociationsResource(final Suggester<Associate> suggester) throws Exception {
        super();

        this.suggester = suggester;
    }


    Subject getAuthorizedUser() {
        final File servopsCert = new File(System.getProperty("user.home") + "/.pub/proxy.pem");
        return SSLUtil.createSubject(servopsCert);
    }

    /**
     * Refresh the cache for the associations.
     */
    public void refresh() {
        LOGGER.info("Refreshing GMUI cache");
        updateState(AssociationsCacheState.REFRESHING);

        Subject.doAs(getAuthorizedUser(), new PrivilegedAction<Void>() {
            @Override
            public Void run() {
                doRefresh();
                return null;
            }
        });
    }

    private void doRefresh() {
        final long refreshStart = System.currentTimeMillis();

        try {
            final Set<Associate> tempSet = new HashSet<>();

            for (final String groupName : getGMSClient().getGroupNames()) {
                tempSet.add(new Associate(groupName, AssociateType.GROUP));
            }

            // Assume something went wrong if the temp map is empty.
            if (!tempSet.isEmpty()) {
                ASSOCIATE_CACHE.clear();
                ASSOCIATE_CACHE.addAll(tempSet);
            }

            tempSet.clear();
        } catch (Exception e) {
            throw new ResourceException(e);
        } finally {
            updateState(AssociationsCacheState.REFRESH_COMPLETE);
            LOGGER.info(String.format(
                "Refresh of GMUI cache complete in %d seconds",
                ((System.currentTimeMillis() - refreshStart) / 1000L)));
        }
    }


    /**
     * A tiny state machine.  Check to see if we're allowed to proceed to the
     * given state.
     *
     * @param newState The new state.
     */
    private void checkState(final AssociationsCacheState newState) {
        // Don't allow a refresh when one is already underway.
        if (associationsCacheState.isRefreshing()
            && (newState == AssociationsCacheState.REFRESHING)) {
            throw new IllegalStateException("Refresh is already underway.");
        }
    }


    /**
     * Update the state of this refresh.
     *
     * @param newState The new state to set to.
     */
    private void updateState(final AssociationsCacheState newState) {
        checkState(newState);
        associationsCacheState = newState;
    }

    /**
     * Accept a POST request to refresh this cache.
     *
     * @throws Exception For anything that needs to be interpreted by the
     *                   Status Service.
     */
    @Post
    public void accept(final Representation payload) throws Exception {
        refresh();
    }

    @Get("json")
    public Representation representJSON() throws Exception {
        final SuggestionResults<Associate> searchResults = searchEntries();

        return new WriterRepresentation(MediaType.APPLICATION_JSON) {
            @Override
            public void write(final Writer writer) throws IOException {
                final JSONWriter jsonWriter = new JSONWriter(writer);

                try {
                    jsonWriter.object();

                    jsonWriter.key("matches");

                    jsonWriter.array();
                    for (final Associate associate : searchResults.getResults()) {
                        new JSONAssociateViewImpl(jsonWriter).write(associate);
                    }
                    jsonWriter.endArray();

                    if (searchResults.getMoreResultsCount() > 0) {
                        jsonWriter.key("remaining").value(
                            searchResults.getMoreResultsCount());
                    }

                    jsonWriter.endObject();
                } catch (JSONException e) {
                    throw new IOException(e);
                }
            }
        };
    }

    /**
     * Search entries for the given value.
     *
     * @return List of matched entries.  We use a list to maintain
     * order.
     */
    private SuggestionResults<Associate> searchEntries() {
        final String queryVal = getQueryValue("q");
        return searchEntries(queryVal);
    }

    private SuggestionResults<Associate> searchEntries(final String queryValue) {
        return suggester.search(queryValue, ASSOCIATE_CACHE);
    }

    /**
     * The state of the associations cache.
     */
    public enum AssociationsCacheState {
        REFRESHING, REFRESH_COMPLETE, INIT;


        protected boolean isRefreshing() {
            return (this == REFRESHING);
        }
    }
}
