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
 * Oct 13, 2010 - 1:55:21 PM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */

package ca.nrc.cadc.groups.web.restlet.resources;

import javax.security.auth.x500.X500Principal;
import java.io.*;

import ca.nrc.cadc.ac.MemberAlreadyExistsException;
import ca.nrc.cadc.ac.Role;
import ca.nrc.cadc.groups.web.WebGroupURI;
import ca.nrc.cadc.util.ObjectUtil;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StreamRepresentation;
import org.skyscreamer.jsonassert.JSONAssert;

import ca.nrc.cadc.ac.Group;
import ca.nrc.cadc.ac.PersonalDetails;
import ca.nrc.cadc.ac.User;
import ca.nrc.cadc.ac.client.GMSClient;
import ca.nrc.cadc.auth.HttpPrincipal;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;


public class GroupMemberListResourceTest extends AbstractResourceTest<GroupMemberListResource> {
    @Test
    public void representCSV() throws Exception {
        setTestSubject(new GroupMemberListResource() {
            /**
             * Obtain the Group ID. This is double decoded to support the double
             * encoding necessary to fool the browser into submitting an encoded
             * URI.
             *
             * @return The Group's ID in the current context.
             */
            @Override
            protected String getGroupName() {
                return "GROUP1";
            }

            /**
             * Obtain the currently authenticated user.
             *
             * @return User instance.
             */
            @Override
            User getCurrentUser() {
                return new User();
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

        final Group testGroup = new Group(new WebGroupURI("GROUP1"));
        ObjectUtil.setField(testGroup, testOwner, "owner");

        final User member1 = new User();
        member1.getIdentities().add(new HttpPrincipal("member1"));
        member1.personalDetails = new PersonalDetails("CADC", "Test");
        member1.getIdentities().add(new HttpPrincipal("CADCtest"));
        testGroup.getUserMembers().add(member1);

        final User member2 = new User();
        member2.getIdentities().add(new HttpPrincipal("member2"));
        member2.personalDetails = new PersonalDetails("Marty", "McFly");
        member2.getIdentities().add(new HttpPrincipal("at88mph"));
        testGroup.getUserMembers().add(member2);

        testGroup.getGroupMembers().add(new Group(new WebGroupURI("TimeTravellers")));

        expect(getMockGMSClient().getGroup("GROUP1")).andReturn(testGroup).once();
        expect(getMockGMSClient().getMembership("GROUP1", Role.ADMIN)).andReturn(null).once();

        replay(getMockGMSClient());

        final Representation rep = getTestSubject().representCSV();
        final StringWriter writer = new StringWriter();

        rep.write(writer);

        final String resultCSV = writer.toString();

        final String expectedCSV =
            "ID,Name,MemberID,Type,OwnerRights,AdminRights\n"
                + "GROUP1,CADC Test,member1,USER,false,false\n"
                + "GROUP1,Marty McFly,member2,USER,false,false\n"
                + "GROUP1,TimeTravellers,TimeTravellers,GROUP,false,false\n";


        assertEquals("Wrong CSV", expectedCSV, resultCSV);

        verify(getMockGMSClient());
    }

    @Test
    public void representJSON() throws Exception {
        setTestSubject(new GroupMemberListResource() {
            /**
             * Obtain the Group ID. This is double decoded to support the double
             * encoding necessary to fool the browser into submitting an encoded
             * URI.
             *
             * @return The Group's ID in the current context.
             */
            @Override
            protected String getGroupName() {
                return "GROUP1";
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

        final Group testGroup = new Group(new WebGroupURI("GROUP1"));
        ObjectUtil.setField(testGroup, testOwner, "owner");

        final User member1 = new User();
        member1.getIdentities().add(new X500Principal("CN=member1,OU=CADC,O=HIA,C=CA"));
        member1.personalDetails = new PersonalDetails("CADC", "Test");
        member1.getIdentities().add(new HttpPrincipal("CADCtest"));
        testGroup.getUserMembers().add(member1);

        final User member2 = new User();
        member2.getIdentities().add(new X500Principal("CN=member2,OU=CADC,O=HIA,C=CA"));
        member2.personalDetails = new PersonalDetails("Marty", "McFly");
        member2.getIdentities().add(new HttpPrincipal("at88mph"));
        testGroup.getUserMembers().add(member2);

        testGroup.getGroupMembers().add(new Group(new WebGroupURI("TimeTravellers")));

        expect(getMockGMSClient().getGroup("GROUP1")).andReturn(testGroup).once();

        replay(getMockGMSClient());

        final Representation rep = getTestSubject().representJSON();
        final StringWriter writer = new StringWriter();

        rep.write(writer);

        final JSONObject resultJSONObject = new JSONObject(writer.toString());
        final JSONObject expectedJSONObject =
            new JSONObject("{\"groupName\":\"GROUP1\", \"members\": [{\"id\":\"CADCtest\",\"username\":\"CADCtest\"," +
                               "\"name\":\"CADC Test\",\"type\":\"USER\",\"AdminRights\":\"true\"}," +
                               "{\"id\":\"at88mph\",\"username\":\"at88mph\",\"name\":\"Marty McFly\"," +
                               "\"type\":\"USER\",\"AdminRights\":\"true\"},{\"name\":\"TimeTravellers\"," +
                               "\"type\":\"GROUP\",\"AdminRights\":\"true\"}]}");

        JSONAssert.assertEquals(expectedJSONObject, resultJSONObject, false);

        verify(getMockGMSClient());
    }

    @Test
    public void updateMembers() throws Exception {
        setTestSubject(new GroupMemberListResource() {
            /**
             * Obtain the Group ID. This is double decoded to support the double
             * encoding necessary to fool the browser into submitting an encoded
             * URI.
             *
             * @return The Group's ID in the current context.
             */
            @Override
            protected String getGroupName() {
                return "GROUP1";
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

        getMockGMSClient().addUserMember("GROUP1",
                                         new HttpPrincipal("jenkinsd"));
        expectLastCall().once();

        final InputStream input =
            new ByteArrayInputStream(
                "assoc-id=jenkinsd%20(Dustin)&assoc-type=USER".
                                                                  getBytes());

        final Representation payload = new StreamRepresentation(MediaType.ALL) {
            @Override
            public InputStream getStream() throws IOException {
                return input;
            }

            @Override
            public void write(final OutputStream outputStream) throws IOException {

            }
        };

        replay(getMockGMSClient());
        getTestSubject().accept(payload);
        verify(getMockGMSClient());
    }

    @Test
    public void addMemberAlreadyExists() throws Exception {
        setTestSubject(new GroupMemberListResource() {
            /**
             * Obtain the Group ID. This is double decoded to support the double
             * encoding necessary to fool the browser into submitting an encoded
             * URI.
             *
             * @return The Group's ID in the current context.
             */
            @Override
            protected String getGroupName() {
                return "GROUP1";
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

        getMockGMSClient().addUserMember("GROUP1",
                                         new HttpPrincipal("jenkinsd"));
        expectLastCall().andThrow(new IOException("(409) Conflict")).once();

        final InputStream input =
            new ByteArrayInputStream(
                "assoc-id=jenkinsd%20(Dustin)&assoc-type=USER".
                                                                  getBytes());

        final Representation payload = new StreamRepresentation(MediaType.ALL) {
            @Override
            public InputStream getStream() throws IOException {
                return input;
            }

            @Override
            public void write(final OutputStream outputStream) throws IOException {

            }
        };

        replay(getMockGMSClient());
        try {
            getTestSubject().accept(payload);
            fail("Should throw MemberAlreadyExistsException.");
        } catch (MemberAlreadyExistsException e) {
            // Good!
        }
        verify(getMockGMSClient());
    }

    @Test
    public void updateGroupMember() throws Exception {
        setTestSubject(new GroupMemberListResource() {
            /**
             * Obtain the Group ID. This is double decoded to support the double
             * encoding necessary to fool the browser into submitting an encoded
             * URI.
             *
             * @return The Group's ID in the current context.
             */
            @Override
            protected String getGroupName() {
                return "GROUP1";
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

        getMockGMSClient().addGroupMember("GROUP1", "GRPMEM1");
        expectLastCall().once();

        final InputStream input =
            new ByteArrayInputStream(
                "assoc-id=GRPMEM1&assoc-type=GROUP".
                                                                            getBytes());

        final Representation payload = new StreamRepresentation(MediaType.ALL) {
            @Override
            public InputStream getStream() throws IOException {
                return input;
            }

            @Override
            public void write(final OutputStream outputStream) throws IOException {

            }
        };

        replay(getMockGMSClient());
        getTestSubject().accept(payload);
        verify(getMockGMSClient());
    }
}
