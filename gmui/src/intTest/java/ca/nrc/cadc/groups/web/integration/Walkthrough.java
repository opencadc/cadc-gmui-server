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
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */

package ca.nrc.cadc.groups.web.integration;

import ca.nrc.cadc.web.selenium.AbstractWebApplicationIntegrationTest;
import org.apache.commons.text.RandomStringGenerator;
import org.junit.Assert;
import org.junit.Test;


public class Walkthrough extends AbstractWebApplicationIntegrationTest {

    public Walkthrough() {
        super();
        setFailOnTimeout(true);
    }

    @Test
    public void walkThrough() throws Exception {
        final AnonymousPage groupManagementPage = goToMain(AnonymousPage.class);
        final String testuser = "doiadmin";
        final String testGroup = "CANFAR-staff";

        GMUIPage authGroupManagementPage = groupManagementPage.doLogin(username, password);

        final String newGroupName = generateGroupName();
        authGroupManagementPage = authGroupManagementPage.createGroup(newGroupName);

        // Check display for groups with various permissions
        Assert.assertTrue(authGroupManagementPage.checkAdminGroup(newGroupName));
        Assert.assertTrue(authGroupManagementPage.checkMemberGroup(testGroup));

        // Add users to groups
        authGroupManagementPage = authGroupManagementPage.addMemberToGroup(newGroupName, testuser);
        authGroupManagementPage = authGroupManagementPage.addAdminToGroup(newGroupName, testuser);

        // Delete users from groups
        authGroupManagementPage = authGroupManagementPage.deleteUserFromMembersGroup(newGroupName, testuser);
        authGroupManagementPage = authGroupManagementPage.deleteUserFromAdminsGroup(newGroupName, testuser);

        // Clean up test group
        authGroupManagementPage.deleteGroup(newGroupName);
    }

    private String generateGroupName() {
        return new RandomStringGenerator.Builder().withinRange('A', 'Z').build().generate(16);
    }
}
