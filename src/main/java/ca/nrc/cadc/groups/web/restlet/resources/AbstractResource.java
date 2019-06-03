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
 * Sep 2, 2010 - 3:35:58 PM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */

package ca.nrc.cadc.groups.web.restlet.resources;

import java.io.IOException;
import java.security.AccessControlContext;
import java.security.AccessController;

import javax.security.auth.Subject;

import ca.nrc.cadc.ac.Role;
import ca.nrc.cadc.ac.UserNotFoundException;
import ca.nrc.cadc.ac.client.UserClient;
import ca.nrc.cadc.auth.IdentityType;
import ca.nrc.cadc.net.NetUtil;
import ca.nrc.cadc.reg.Standards;
import ca.nrc.cadc.reg.client.LocalAuthority;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;

import ca.nrc.cadc.ac.Group;
import ca.nrc.cadc.ac.GroupNotFoundException;
import ca.nrc.cadc.ac.User;
import ca.nrc.cadc.ac.client.GMSClient;
import ca.nrc.cadc.groups.web.Associate;
import ca.nrc.cadc.groups.web.AssociateType;


public abstract class AbstractResource extends ServerResource {
    private final static String GROUP_NAME_ATTRIBUTE_KEY = "groupName";

    /**
     * Constructor
     */
    AbstractResource() {
    }

    /**
     * Obtain a new Form with the given body.
     *
     * @param entity The Payload.
     * @return A new Form instance.
     */
    Form getForm(final Representation entity) {
        return new Form(entity);
    }

    /**
     * Convenience method for obtaining an attribute from the Restlet
     * Request.
     *
     * @param attributeName Key to access an attribute.
     * @return Object associated with the given key.
     */
    Object getRequestAttribute(final String attributeName) {
        return getRequest().getAttributes().get(attributeName);
    }

    /**
     * Convenience method for obtaining an attribute from the Restlet
     * Request.
     *
     * @param attributeName Key to access an attribute.
     * @return String value, or null if non existent.
     */
    String getRequestAttributeValue(final String attributeName) {
        return getRequestAttributeValue(attributeName, false);
    }

    /**
     * Convenience method for obtaining an attribute from the Restlet
     * Request.
     *
     * @param attributeName Key to access an attribute.
     * @param decodeValue   Flag to decode the value, or leave it alone if false.
     * @return String value, or null if non existent.
     */
    String getRequestAttributeValue(final String attributeName, final boolean decodeValue) {
        final Object o = getRequestAttribute(attributeName);
        return ((o == null) ? null
            : (decodeValue ? NetUtil.decode(o.toString())
            : o.toString()));
    }

    /**
     * Obtain the currently authenticated user.
     *
     * @return User instance.
     */
    User getCurrentUser() throws UserNotFoundException, IOException {
        return getUserClient().whoAmI();
    }

    final boolean currentUserHasAdminRights(final Group group)
        throws IOException, UserNotFoundException {
        return currentUserHasOwnerRights(group)
            || (getGMSClient().getMembership(group.getID().getName(), Role.ADMIN) != null);
    }

    final boolean currentUserHasOwnerRights(final Group group) throws IOException, UserNotFoundException {
        final User currentUser = getCurrentUser();
        return currentUser.equals(group.getOwner());
    }


    /**
     * Obtain the current Subject.  This is used to perform secure calls to the
     * VOSpace Web Service, while abstracting away the details of that to the
     * end User.
     *
     * @return The Subject object, or null if none found.
     */
    @SuppressWarnings("unchecked")
    protected Subject getSubject() {
        final AccessControlContext acContext = AccessController.getContext();
        final Subject subject = Subject.getSubject(acContext);

        return ((subject == null) || (subject.getPrincipals() == null)
            || subject.getPrincipals().isEmpty()) ? null : subject;
    }


    /**
     * Get the client for talking to GMS.  This will pull a new one each time
     * due to caching.
     *
     * @return the GMSClient instance
     */
    public GMSClient getGMSClient() {
        return new GMSClient(new LocalAuthority().getServiceURI(
            Standards.GMS_GROUPS_01.toString()));
    }

    public UserClient getUserClient() {
        return new UserClient(new LocalAuthority().getServiceURI(
            Standards.UMS_USERS_01.toString()));
    }

    /**
     * Obtain the associate that's being added.
     *
     * @param entity The Representation being added.
     * @return Associate instance.
     */
    Associate getAssociate(final Representation entity) {
        return getAssociate(new Form(entity), IdentityType.USERNAME);
    }

    /**
     * Obtain the associate from the given Form.
     *
     * @param form         The Form containing pertinent data.
     * @param identityType The ID type of the principal.
     * @return Associate instance.
     * @see ca.nrc.cadc.auth.IdentityType
     */
    final Associate getAssociate(final Form form,
                                 final IdentityType identityType) {
        final String principalName = form.getFirstValue("assoc-id");
        final String principalType = form.getFirstValue("assoc-type");
        final AssociateType associateType = AssociateType.valueOf(principalType.toUpperCase());
        final String name;

        if (identityType == IdentityType.USERNAME) {
            final int spaceDelimiterIndex = principalName.trim().indexOf(" ");

            if (spaceDelimiterIndex > 0) {
                name = principalName.substring(0, spaceDelimiterIndex);
            } else {
                name = principalName;
            }
        } else {
            name = principalName;
        }

        return new Associate(name, associateType);
    }

    /**
     * Obtain the Group ID. This is double decoded to support the double
     * encoding necessary to fool the browser into submitting an encoded
     * URI.
     *
     * @return The Group's ID in the current context.
     */
    protected String getGroupName() {
        return getRequestAttributeValue(GROUP_NAME_ATTRIBUTE_KEY, true);
    }

    /**
     * Obtain the Group in the current context, if any.
     *
     * @return The Group in the current context.
     * @throws ca.nrc.cadc.ac.GroupNotFoundException Group isn't found.
     * @throws java.io.IOException                   Server error.
     */
    protected Group getGroup() throws GroupNotFoundException, IOException {
        return getGMSClient().getGroup(getGroupName());
    }
}
