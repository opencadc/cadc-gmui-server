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
 * 10/14/14 - 10:02 AM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */
package ca.nrc.cadc.groups.web.restlet.resources;

import ca.nrc.cadc.ac.Group;
import ca.nrc.cadc.ac.User;
import ca.nrc.cadc.groups.web.Associate;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;

import java.util.Iterator;


public class GroupAdminResource extends AbstractAssociateResource
{
    @Delete
    public void remove(final Representation payload) throws Exception
    {
        final Associate associate = getAssociate(payload);
        final Group group = getGroup();

        if (associate.isUser())
        {
            for (final Iterator<User> userAdminIter
                         = group.getUserAdmins().iterator();
                 userAdminIter.hasNext();)
            {
                if (userAdminIter.next().getHttpPrincipal().getName()
                    .equals(associate.getID()))
                {
                    userAdminIter.remove();
                }
            }
        }
        else
        {
            for (final Iterator<Group> groupAdminIter
                         = group.getGroupAdmins().iterator();
                 groupAdminIter.hasNext();)
            {
                if (groupAdminIter.next().getID().getName().equals(
                        associate.getID()))
                {
                    groupAdminIter.remove();
                }
            }
        }

        getGMSClient().updateGroup(group);
    }
}
