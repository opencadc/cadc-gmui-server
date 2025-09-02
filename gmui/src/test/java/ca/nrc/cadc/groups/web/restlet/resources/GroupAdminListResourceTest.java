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
 * 9/17/14 - 11:21 AM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */

package ca.nrc.cadc.groups.web.restlet.resources;

import javax.security.auth.x500.X500Principal;
import java.io.*;

import ca.nrc.cadc.ac.Role;
import ca.nrc.cadc.groups.web.WebGroupURI;
import ca.nrc.cadc.util.ObjectUtil;
import org.apache.log4j.Level;
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
import ca.nrc.cadc.util.Log4jInit;

import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;


public class GroupAdminListResourceTest extends AbstractResourceTest<GroupAdminListResource> {
    static {
        Log4jInit.setLevel("ca.nrc.cadc.groups", Level.INFO);
    }

    @Test
    public void representCSV() throws Exception {
        setTestSubject(new GroupAdminListResource() {
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
        ObjectUtil.setField(testGroup, secondTestOwner, "owner");

        final User admin1 = new User();
        admin1.getIdentities().add(new HttpPrincipal("CADCtest"));
        admin1.personalDetails = new PersonalDetails("CADC", "Test");
        testGroup.getUserAdmins().add(admin1);

        final User admin2 = new User();
        admin2.getIdentities().add(new HttpPrincipal("at88mph"));
        admin2.personalDetails = new PersonalDetails("Marty", "McFly");
        testGroup.getUserAdmins().add(admin2);

        testGroup.getGroupAdmins().add(new Group(new WebGroupURI("Supers")));

        expect(getMockGMSClient().getGroup("GROUP1")).andReturn(
            testGroup).once();
        expect(getMockGMSClient().getMembership("GROUP1", Role.ADMIN)).andReturn(testGroup).once();

        replay(getMockGMSClient());

        final Representation rep = getTestSubject().representCSV();
        final StringWriter writer = new StringWriter();

        rep.write(writer);

        // We can't perform a string comparison due to inconsistent Set ordering
        // between Java versions, so we'll compare the arrays here to ensure
        // that the first two entries (after the header) are USER entries, and
        // that the last one is a GROUP entry.
        final String resultCSV = writer.toString();
        final String expectedCSV =
            "ID,Name,AdminID,Type,OwnerRights,AdminRights\n"
                + "GROUP1,CADC Test,CADCtest,USER,false,true\n"
                + "GROUP1,Marty McFly,at88mph,USER,false,true\n"
                + "GROUP1,All members of Supers,Supers,GROUP,false,true\n";

        assertEquals("Wrong CSV", expectedCSV, resultCSV);

        verify(getMockGMSClient());
    }

    @Test
    public void representJSON() throws Exception {
        setTestSubject(new GroupAdminListResource() {
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

        final User admin1 = new User();
        admin1.personalDetails = new PersonalDetails("CADC", "Test");
        admin1.getIdentities()
              .add(new X500Principal("CN=admin1,OU=CADC,O=HIA,C=CA"));
        admin1.getIdentities().add(new HttpPrincipal("CADCtest"));
        testGroup.getUserAdmins().add(admin1);

        final User admin2 = new User();
        admin2.personalDetails = new PersonalDetails("Marty", "McFly");
        admin2.getIdentities()
              .add(new X500Principal("CN=admin2,OU=CADC,O=HIA,C=CA"));
        admin2.getIdentities().add(new HttpPrincipal("at88mph"));
        testGroup.getUserAdmins().add(admin2);

        testGroup.getGroupAdmins().add(new Group(new WebGroupURI("Supers")));

        expect(getMockGMSClient().getGroup("GROUP1")).andReturn(testGroup)
                                                     .once();
        expect(getMockGMSClient().getMembership("GROUP1", Role.ADMIN)).andReturn(null).once();

        replay(getMockGMSClient());

        final Representation rep = getTestSubject().representJSON();
        final StringWriter writer = new StringWriter();

        rep.write(writer);

        final JSONObject resultJSONObject = new JSONObject(writer.toString());
        final JSONObject expectedJSONObject =
            new JSONObject(
                "{\"groupName\":\"GROUP1\", \"groupRole\":\"\" ,\"admins\":[{\"id\":\"CADCtest\"," +
                    "\"username\":\"CADCtest\",\"name\":\"CADC Test\",\"type\":\"USER\",\"AdminRights\":\"false\"}," +
                    "{\"id\":\"at88mph\",\"username\":\"at88mph\",\"name\":\"Marty McFly\",\"type\":\"USER\"," +
                    "\"AdminRights\":\"false\"},{\"description\":null,\"name\":\"Supers\",\"type\":\"GROUP\"," +
                    "\"AdminRights\":\"false\"}]}");

        JSONAssert.assertEquals(expectedJSONObject, resultJSONObject, false);

        verify(getMockGMSClient());
    }

    @Test
    public void accept() throws Exception {
        final Group testGroup = new Group(new WebGroupURI("MYGROUP"));
        ObjectUtil.setField(testGroup, testOwner, "owner");

        setTestSubject(new GroupAdminListResource() {
            /**
             * Obtain the Group ID. This is double decoded to support the double
             * encoding necessary to fool the browser into submitting an encoded
             * URI.
             *
             * @return The Group's ID in the current context.
             */
            @Override
            protected String getGroupName() {
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

        final InputStream input =
            new ByteArrayInputStream(
                "assoc-id=NEWADMIN&assoc-type=USER".getBytes());

        final Representation payload = new StreamRepresentation(MediaType.ALL) {
            @Override
            public InputStream getStream() {
                return input;
            }

            @Override
            public void write(final OutputStream outputStream) { }
        };

        expect(getMockGMSClient().getGroup("MYGROUP")).andReturn(
            testGroup).once();

        expect(getMockGMSClient().updateGroup(testGroup)).andReturn(testGroup).
            once();

        replay(getMockGMSClient());

        getTestSubject().accept(payload);

        verify(getMockGMSClient());
    }
}
