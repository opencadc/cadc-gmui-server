/*
 ************************************************************************
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 *
 * (c) 2012.                         (c) 2012.
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
 * 12/18/12 - 2:01 PM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */
package ca.nrc.cadc.groups.web.restlet;

import java.io.IOException;
import java.util.logging.Level;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.engine.adapter.HttpResponse;
import org.restlet.engine.adapter.ServerAdapter;
import org.restlet.engine.header.HeaderUtils;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.StringRepresentation;


public class GroupsWebServerAdapter extends ServerAdapter
{
    public GroupsWebServerAdapter(final Context context)
    {
        super(context);
    }


    /**
     * Commits the changes to a handled uniform call back into the original HTTP
     * call. The default implementation first invokes the "addResponseHeaders"
     * then asks the "htppCall" to send the response back to the client.
     *
     * @param response The high-level response.
     */
    public void commit(HttpResponse response)
    {
        try
        {
            if ((response.getRequest().getMethod() != null)
                && response.getRequest().getMethod().equals(Method.HEAD))
            {
                addEntityHeaders(response);
                response.setEntity(null);
            }
            else if (Method.GET.equals(response.getRequest().getMethod())
                     && Status.SUCCESS_OK.equals(response.getStatus())
                     && (!response.isEntityAvailable()))
            {
                addEntityHeaders(response);
                getLogger()
                        .warning(
                                "A response with a 200 (Ok) status should have an entity. Make sure that resource \""
                                + response.getRequest()
                                        .getResourceRef()
                                + "\" returns one or sets the status to 204 (No content).");
            }
            else if (response.getStatus().equals(Status.SUCCESS_NO_CONTENT))
            {
                addEntityHeaders(response);

                if (response.isEntityAvailable())
                {
                    getLogger()
                            .fine("Responses with a 204 (No content) status generally don't have an entity. Only adding entity headers for resource \""
                                  + response.getRequest().getResourceRef()
                                  + "\".");
                    response.setEntity(null);
                }
            }
            else if (response.getStatus()
                    .equals(Status.SUCCESS_RESET_CONTENT))
            {
                if (response.isEntityAvailable())
                {
                    getLogger()
                            .warning(
                                    "Responses with a 205 (Reset content) status can't have an entity. Ignoring the entity for resource \""
                                    + response.getRequest()
                                            .getResourceRef() + "\".");
                    response.setEntity(null);
                }
            }
            else if (response.getStatus().equals(
                    Status.REDIRECTION_NOT_MODIFIED))
            {
                if (response.getEntity() != null)
                {
                    HeaderUtils.addNotModifiedEntityHeaders(response
                                                                    .getEntity(),
                                                            response.getHttpCall()
                                                                    .getResponseHeaders());
                    response.setEntity(null);
                }
            }
            else if (response.getStatus().isInformational())
            {
                if (response.isEntityAvailable())
                {
                    getLogger()
                            .warning(
                                    "Responses with an informational (1xx) status can't have an entity. Ignoring the entity for resource \""
                                    + response.getRequest()
                                            .getResourceRef() + "\".");
                    response.setEntity(null);
                }
            }
            else
            {
                addEntityHeaders(response);

                if (!response.isEntityAvailable())
                {
                    if ((response.getEntity() != null)
                        && (response.getEntity().getSize() != 0))
                    {
                        getLogger()
                                .warning(
                                        "A response with an unavailable and potentially non empty entity was returned. Ignoring the entity for resource \""
                                        + response.getRequest()
                                                .getResourceRef()
                                        + "\".");
                    }

                    response.setEntity(null);
                }
            }

            // Add the response headers
            addResponseHeaders(response);

            // Send the response to the client
            response.getHttpCall().sendResponse(response);
        }
        catch (Throwable t)
        {
            final Application application = Application.getCurrent();

            if (response.getHttpCall().isConnectionBroken(t))
            {
                getLogger()
                        .log(Level.INFO,
                             "The connection was broken. It was probably closed by the client.",
                             t);
            }
            else
            {
                getLogger().log(Level.SEVERE,
                                "An exception occured writing the response entity",
                                t);

                final Status status =
                        application.getStatusService().getStatus(t,
                                                                 response.getRequest(),
                                                                 response);
                final Throwable statusThrowable = status.getThrowable();

                response.getHttpCall().setStatusCode(status.getCode());
                response.getHttpCall().setReasonPhrase(
                        status.getDescription());

                if (statusThrowable != null)
                {
                    response.setEntity(new StringRepresentation(
                            statusThrowable.getMessage()));
                }
                else
                {
                    response.setEntity(new EmptyRepresentation());
                }

                try
                {
                    response.getHttpCall().sendResponse(response);
                }
                catch (IOException ioe)
                {
                    getLogger().log(Level.WARNING,
                                    "Unable to send error response", ioe);
                }
            }
        }
        finally
        {
            response.getHttpCall().complete();

            if (response.getOnSent() != null)
            {
                response.getOnSent().handle(response.getRequest(), response);
            }
        }
    }
}
