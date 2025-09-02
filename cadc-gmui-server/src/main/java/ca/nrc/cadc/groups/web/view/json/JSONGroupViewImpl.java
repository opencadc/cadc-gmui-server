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
 * 2/28/14 - 10:30 AM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */

package ca.nrc.cadc.groups.web.view.json;

import ca.nrc.cadc.ac.Group;
import ca.nrc.cadc.ac.User;
import ca.nrc.cadc.groups.web.UserInfoParser;
import org.json.JSONException;
import org.json.JSONWriter;


public class JSONGroupViewImpl extends AbstractJSONViewImpl<Group> {
    /**
     * JSON View of a Group.
     *
     * @param jsonWriter     The JSONWriter to write to.
     * @param hasOwnerRights Whether the receiving user is the owner.
     * @param hasAdminRights Whether the receiving user has administrative privileges.
     */
    public JSONGroupViewImpl(final JSONWriter jsonWriter, final boolean hasOwnerRights, boolean hasAdminRights) {
        super(jsonWriter, JSONViewType.OBJECT, hasOwnerRights, hasAdminRights);
    }


    /**
     * Write the Group JSON contents.
     *
     * @param group The item to write.
     * @throws org.json.JSONException Any JSON writing exceptions.
     */
    @Override
    void writeJSON(final Group group) throws JSONException {
        write("name", group.getID().getName());

        final User groupOwner = group.getOwner();

        if (groupOwner != null) {
            write("owner_name", UserInfoParser.getFullName(groupOwner));
        }

        write("description", group.description);
        write("type", "GROUP");
    }
}
