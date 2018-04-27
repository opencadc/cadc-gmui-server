/*
 ************************************************************************
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 *
 * (c) 2009.                         (c) 2009.
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
 * May 14, 2009 - 11:21:10 AM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */

package ca.nrc.cadc.groups.web.restlet.services;


import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.service.StatusService;
import org.restlet.data.*;
import org.restlet.Request;
import org.restlet.Response;

import java.io.UnsupportedEncodingException;
import java.security.AccessControlException;

import ca.nrc.cadc.ac.GroupAlreadyExistsException;
import ca.nrc.cadc.ac.GroupNotFoundException;
import ca.nrc.cadc.ac.MemberAlreadyExistsException;
import ca.nrc.cadc.ac.MemberNotFoundException;
import ca.nrc.cadc.ac.UserNotFoundException;
import ca.nrc.cadc.groups.web.FormValidationException;
import ca.nrc.cadc.groups.web.WriterIOException;
import ca.nrc.cadc.groups.web.IllegalInputException;
import ca.nrc.cadc.util.StringUtil;


/**
 * Status Service to intercept errors for the Access Control Web Application.
 */
public class GroupsWebStatusService extends StatusService {
    /**
     * Constructor.
     */
    public GroupsWebStatusService() {
        this(true);
    }

    /**
     * Constructor.
     *
     * @param enabled True if the service has been enabled.
     */
    public GroupsWebStatusService(boolean enabled) {
        super(enabled);
        setOverwriting(true);
    }

    /**
     * Returns a representation for the given status.<br>
     * In order to customize the default representation, this method can be
     * overridden. It returns null by default.
     *
     * @param status   The status to represent.
     * @param request  The request handled.
     * @param response The response updated.
     * @return The representation of the given status.
     */
    @Override
    public Representation getRepresentation(final Status status, final Request request, final Response response) {
        return new StringRepresentation(status.getReasonPhrase());
    }

    /**
     * Returns a status for a given exception or error. By default it unwraps
     * the status of {@link org.restlet.resource.ResourceException}. For other exceptions or errors,
     * it returns an {@link org.restlet.data.Status#SERVER_ERROR_INTERNAL} status.<br>
     * <br>
     * In order to customize the default behavior, this method can be
     * overridden.
     *
     * @param throwable The exception or error caught.
     * @param request   The request handled.
     * @param response  The response updated.
     * @return The representation of the given status.
     */
    @Override
    public Status getStatus(final Throwable throwable, final Request request,
                            final Response response) {
        return translateStatus(throwable);
    }

    /**
     * Translate the given Throwable into a Status code.
     *
     * @param throwable The Throwable found.
     * @return Status instance.
     */
    protected Status translateStatus(final Throwable throwable) {
        final Status status;

        if (throwable instanceof WriterIOException) {
            status = translateStatus(throwable.getCause());
        }
        // Group already exists, so throw a 409 Conflict.
        else if (throwable instanceof GroupAlreadyExistsException
            || throwable instanceof MemberAlreadyExistsException) {
            status = new Status(Status.CLIENT_ERROR_CONFLICT.getCode(), throwable);
        } else if (throwable instanceof FormValidationException) {
            status = Status.CLIENT_ERROR_NOT_ACCEPTABLE;
        } else if (throwable instanceof ResourceException) {
            final Throwable cause = throwable.getCause();

            if (cause == null) {
                status = new Status(Status.SERVER_ERROR_INTERNAL.getCode(), throwable);
            } else {
                status = translateStatus(cause);
            }
        } else if ((throwable instanceof UnsupportedEncodingException)
            || (throwable instanceof IllegalArgumentException)) {
            if (!StringUtil.hasText(throwable.getMessage())) {
                status = new Status(Status.CLIENT_ERROR_BAD_REQUEST.getCode(), throwable);
            } else {
                status = new Status(Status.CLIENT_ERROR_BAD_REQUEST.getCode(),
                                    throwable, null, throwable.getMessage(), null);
            }
        } else if (throwable instanceof IllegalInputException) {
            status = new Status(Status.CLIENT_ERROR_BAD_REQUEST.getCode(), throwable.getMessage(),
                                throwable.getMessage(), null);
        } else if (throwable instanceof GroupNotFoundException) {
            status = new Status(Status.CLIENT_ERROR_NOT_FOUND.getCode(),
                                throwable, null, "NO_SUCH_GROUP",
                                null);
        } else if ((throwable instanceof UserNotFoundException)
            || (throwable instanceof MemberNotFoundException)) {
            status = new Status(Status.CLIENT_ERROR_NOT_FOUND.getCode(),
                                throwable, null, "NO_SUCH_USER", null);
        } else if (throwable instanceof AccessControlException) {
            final String message = throwable.getMessage();

            if (StringUtil.hasText(message) && message.startsWith("authorization")) {
                status = new Status(Status.CLIENT_ERROR_FORBIDDEN.getCode(),
                                    throwable, "FORBIDDEN", "FORBIDDEN", null);
            } else {
                status = new Status(Status.CLIENT_ERROR_UNAUTHORIZED.getCode(),
                                    throwable, null, throwable.getMessage(), null);
            }
        } else if (throwable instanceof IllegalStateException) {
            final Throwable cause = throwable.getCause();

            if (cause == null) {
                status = new Status(Status.SERVER_ERROR_INTERNAL.getCode(), throwable);
            } else {
                final Status coreStatus = translateStatus(cause);
                status = new Status(coreStatus.getCode(), throwable.getMessage(), null, null);
            }
        } else {
            status = new Status(Status.SERVER_ERROR_INTERNAL.getCode(), throwable);
        }

        return status;
    }
}
