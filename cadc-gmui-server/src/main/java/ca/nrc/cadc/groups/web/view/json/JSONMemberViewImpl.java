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
 * 2/28/14 - 10:42 AM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */

package ca.nrc.cadc.groups.web.view.json;

import ca.nrc.cadc.ac.User;
import ca.nrc.cadc.groups.web.UserInfoParser;
import ca.nrc.cadc.util.StringUtil;
import org.json.JSONException;
import org.json.JSONWriter;


public class JSONMemberViewImpl extends AbstractJSONViewImpl<User> {
    /**
     * Represent a User as JSON.
     *
     * @param _jsonWriter    The JSON Writer instance.
     * @param hasOwnerRights Whether the receiving user is the owner.
     * @param hasAdminRights Whether the receiving user has administrative privileges.
     */
    public JSONMemberViewImpl(final JSONWriter _jsonWriter, final boolean hasOwnerRights,
                              final boolean hasAdminRights) {
        super(_jsonWriter, JSONViewType.OBJECT, hasOwnerRights, hasAdminRights);
    }


    /**
     * Write this user.
     *
     * @param user The user to write.
     * @throws JSONException
     */
    @Override
    void writeJSON(final User user) throws JSONException {
        final String parsedName = UserInfoParser.getFullName(user);
        final String name = StringUtil.hasText(parsedName) ? parsedName : "";
        final String parsedUsername = UserInfoParser.getUsername(user);
        final String id;
        final String username;

        if (StringUtil.hasText(parsedUsername)) {
            id = parsedUsername;
            username = parsedUsername;
        } else {
            id = "";
            username = "";
        }

        write("id", id);
        write("username", username);
        write("name", name);
        write("type", "USER");
    }
}
