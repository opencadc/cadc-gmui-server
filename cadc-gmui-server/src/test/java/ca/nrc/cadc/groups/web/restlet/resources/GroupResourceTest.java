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
 * Oct 12, 2010 - 12:57:08 PM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */

package ca.nrc.cadc.groups.web.restlet.resources;

import ca.nrc.cadc.ac.User;
import ca.nrc.cadc.groups.web.WebGroupURI;
import ca.nrc.cadc.util.ObjectUtil;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.io.Writer;

import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.skyscreamer.jsonassert.JSONAssert;

import ca.nrc.cadc.ac.Group;
import ca.nrc.cadc.ac.client.GMSClient;


public class GroupResourceTest extends AbstractResourceTest<GroupResource> {
    @Test
    public void represent() throws Exception {
        final Group currentTestGroup = new Group(new WebGroupURI("CURR_GROUP"));
        ObjectUtil.setField(currentTestGroup, testOwner, "owner");

        currentTestGroup.description = "TEST CURRENT GROUP";

        // TEST CREATE
        setTestSubject(new GroupResource() {
            /**
             * Obtain the Group ID.
             *
             * @return The Group's ID in the current context.
             */
            @Override
            public String getGroupName() {
                return "CURR_GROUPID";
            }

            /**
             * Obtain the currently authenticated user.
             *
             * @return User instance.
             */
            @Override
            User getCurrentUser() {
                return testOwner;
            }

            /**
             * Get the client for talking to GMS.  This will pull a new one each time
             * due to caching.
             *
             * @return the GMSClient instance
             */
            @Override
            public GMSClient getGMSClient() {
                return getMockGMSClient();
            }
        });

        expect(getMockGMSClient().getGroup("CURR_GROUPID")).andReturn(
            currentTestGroup).once();

        replay(getMockGMSClient());

        final Representation rep = getTestSubject().represent();
        final Writer stringWriter = new StringWriter();

        rep.write(stringWriter);

        assertNotNull("Representation should not be null.", rep);
        assertEquals("Media type should be application/json",
                     MediaType.APPLICATION_JSON, rep.getMediaType());

        final JSONObject resultJSONObject =
            new JSONObject(stringWriter.toString());
        final JSONObject expectedJSONObject =
            new JSONObject("{\"name\":\"CURR_GROUP\",\"owner_name\":\"CADC Test\",\"description\":\"TEST CURRENT " +
                               "GROUP\",\"type\":\"GROUP\",\"OwnerRights\":\"true\",\"AdminRights\":\"true\"}");

        JSONAssert.assertEquals(expectedJSONObject, resultJSONObject, true);

        verify(getMockGMSClient());
    }

    @Test
    public void update() throws Exception {
        final Group testGroup = new Group(new WebGroupURI("TEST_GROUP"));
        ObjectUtil.setField(testGroup, testOwner, "owner");
        testGroup.description = "DESCRIPTION BEFORE UPDATE.";

        setTestSubject(new GroupResource() {
            /**
             * Obtain the Group ID. This is double decoded to support the double
             * encoding necessary to fool the browser into submitting an encoded
             * URI.
             *
             * @return The Group's ID in the current context.
             */
            @Override
            protected String getGroupName() {
                return "GROUP_TO_UPDATE";
            }

            /**
             * Get the client for talking to GMS.  This will pull a new one each time
             * due to caching.
             *
             * @return the GMSClient instance
             */
            @Override
            public GMSClient getGMSClient() {
                return getMockGMSClient();
            }
        });

        expect(getMockGMSClient().getGroup("GROUP_TO_UPDATE")).andReturn(
            testGroup).once();
        expect(getMockForm().getFirstValue(
            GroupResource.GROUP_DESCRIPTION_FIELD)).andReturn(
            "NEW DESC").once();

        expect(getMockGMSClient().updateGroup(testGroup)).andReturn(testGroup).
            once();

        replay(getMockForm(), getMockGMSClient());

        getTestSubject().update(getMockForm());

        assertEquals("Wrong description after update.", "NEW DESC",
                     testGroup.description);

        verify(getMockForm(), getMockGMSClient());
    }

    @Test
    public void delete() throws Exception {
        setTestSubject(new GroupResource() {
            /**
             * Obtain the Group ID.
             *
             * @return The Group's ID in the current context.
             */
            @Override
            public String getGroupName() {
                return "MYGROUP";
            }

            /**
             * Get the client for talking to GMS.  This will pull a new one each time
             * due to caching.
             *
             * @return the GMSClient instance
             */
            @Override
            public GMSClient getGMSClient() {
                return getMockGMSClient();
            }
        });

        getMockGMSClient().deleteGroup("MYGROUP");
        expectLastCall().once();

        replay(getMockGMSClient());
        getTestSubject().remove();
        verify(getMockGMSClient());
    }
}
