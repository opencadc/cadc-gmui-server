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
 * Oct 8, 2010 - 2:18:57 PM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */
package ca.nrc.cadc.groups.web.restlet.resources;


import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;
import javax.security.auth.x500.X500Principal;

import ca.nrc.cadc.auth.HttpPrincipal;
import ca.nrc.cadc.groups.web.WebGroupURI;
import ca.nrc.cadc.util.ObjectUtil;
import org.apache.log4j.Level;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

import ca.nrc.cadc.ac.*;
import ca.nrc.cadc.ac.client.GMSClient;
import ca.nrc.cadc.util.Log4jInit;

import org.junit.Test;


public class GroupListResourceTest
        extends AbstractResourceTest<GroupListResource>
{
    static
    {
        Log4jInit.setLevel("ca.nrc.cadc.groups", Level.INFO);
    }


    @Test
    public void representCSVEmpty() throws Exception
    {
        final List<Group> memberGroups = new ArrayList<>();
        final List<Group> adminGroups = new ArrayList<>();
        final List<Group> ownerGroups = new ArrayList<>();

        setTestSubject(new GroupListResource()
        {
            /**
             * Get the client for talking to GMS.  This will pull a new one each time
             * due to caching.
             *
             * @return the GMSClient instance
             */
            @Override
            public GMSClient getGMSClient()
            {
                return getMockGMSClient();
            }

            /**
             * Obtain the current Subject.  This is used to perform secure calls to the
             * VOSpace Web Service, while abstracting away the details of that to the
             * end User.
             *
             * @return The Subject object, or null if none found.
             */
            @Override
            protected Subject getSubject()
            {
                return new Subject();
            }
        });

        expect(getMockGMSClient().getMemberships(Role.MEMBER)).andReturn(
                memberGroups).once();
        expect(getMockGMSClient().getMemberships(Role.ADMIN)).andReturn(
                adminGroups).once();
        expect(getMockGMSClient().getMemberships(Role.OWNER)).andReturn(
                ownerGroups).once();

        replay(getMockGMSClient());

        try
        {
            final Representation rep = getTestSubject().representCSV();
            final Writer writer = new StringWriter();

            rep.write(writer);

            assertEquals("Should be CSV.", MediaType.TEXT_CSV,
                         rep.getMediaType());
            assertEquals("Group list CSV is wrong.",
                         "Name,Owner,Admin,Member,Description,OwnerRights,AdminRights\n",
                         writer.toString());
        }
        finally
        {
            verify(getMockGMSClient());
        }
    }

    @Test
    public void representCSV() throws Exception
    {
        final List<Group> memberGroups = new ArrayList<>();
        final List<Group> adminGroups = new ArrayList<>();
        final List<Group> ownerGroups = new ArrayList<>();

        final Group MYGROUP1 = new Group(new WebGroupURI("MYGROUP1"));
        ObjectUtil.setField(MYGROUP1, testOwner, "owner");
        MYGROUP1.description = "";

        User MYUSER1 = new User();
        MYUSER1.getIdentities().add(new HttpPrincipal("mmcfly_88"));
        MYUSER1.getIdentities()
                .add(new X500Principal("CN=mmcfly_88,OU=CADC,O=HIA,C=CA"));
        MYGROUP1.getUserMembers().add(MYUSER1);

        final Group MYGROUP3 = new Group(new WebGroupURI("MYGROUP3"));
        ObjectUtil.setField(MYGROUP3, testOwner, "owner");
        MYGROUP3.description = "THIS IS MY\n\r GROUP 3!";

        User MYUSER3 = new User();
        MYUSER3.getIdentities().add(new HttpPrincipal("debrown_88"));
        MYUSER3.getIdentities()
                .add(new X500Principal("CN=debrown_88,OU=CADC,O=HIA,C=CA"));
        MYGROUP3.getUserMembers().add(MYUSER3);

        final Group MYGROUP4 = new Group(new WebGroupURI("BGROUP1"));
        ObjectUtil.setField(MYGROUP4, testOwner, "owner");
        MYGROUP4.description = "DO NOT DELETE";

        ownerGroups.add(MYGROUP1);
        ownerGroups.add(MYGROUP3);
        ownerGroups.add(MYGROUP4);

        final Group AGROUP2 = new Group(new WebGroupURI("AGROUP2"));
        User AUSER2 = new User();
        AUSER2.getIdentities().add(new HttpPrincipal("debrown_88"));
        AUSER2.getIdentities()
                .add(new X500Principal("CN=debrown_88,OU=CADC,O=HIA,C=CA"));
        ObjectUtil.setField(AGROUP2, AUSER2, "owner");
        AGROUP2.description = null;

        adminGroups.add(AGROUP2);

        final Group MEMGROUP2 = new Group(new WebGroupURI("MEMGROUP2"));
        User MEMUSER2 = new User();
        MEMUSER2.getIdentities().add(new HttpPrincipal("someoneelse_4"));
        MEMUSER2.getIdentities()
                .add(new X500Principal("CN=someoneelse_4,OU=CADC,O=HIA,C=CA"));
        ObjectUtil.setField(MEMGROUP2, MEMUSER2, "owner");


        final Group MEMGROUP3 = new Group(new WebGroupURI("MEMGROUP3"));
        User MEMUSER3 = new User();
        MEMUSER3.getIdentities().add(new HttpPrincipal("someoneelse_4"));
        MEMUSER3.getIdentities()
                .add(new X500Principal("CN=someoneelse_4,OU=CADC,O=HIA,C=CA"));
        ObjectUtil.setField(MEMGROUP3, MEMUSER3, "owner");
        MEMGROUP3.description = "Member group";

        memberGroups.add(MEMGROUP2);
        memberGroups.add(MEMGROUP3);


        setTestSubject(new GroupListResource()
        {
            /**
             * Get the client for talking to GMS.  This will pull a new one each time
             * due to caching.
             *
             * @return the GMSClient instance
             */
            @Override
            public GMSClient getGMSClient()
            {
                return getMockGMSClient();
            }

            /**
             * Obtain the current Subject.  This is used to perform secure calls to the
             * VOSpace Web Service, while abstracting away the details of that to the
             * end User.
             *
             * @return The Subject object, or null if none found.
             */
            @Override
            protected Subject getSubject()
            {
                return new Subject();
            }
        });

        expect(getMockGMSClient().getMemberships(Role.MEMBER)).andReturn(
                memberGroups).once();
        expect(getMockGMSClient().getMemberships(Role.ADMIN)).andReturn(
                adminGroups).once();
        expect(getMockGMSClient().getMemberships(Role.OWNER)).andReturn(
                ownerGroups).once();

        replay(getMockGMSClient());

        final Representation rep = getTestSubject().representCSV();
        final Writer writer = new StringWriter();

        rep.write(writer);

        assertEquals("Should be CSV.", MediaType.TEXT_CSV, rep.getMediaType());

        final String expectedCSV =
                "Name,Owner,Admin,Member,Description,OwnerRights,AdminRights\n" +
                "AGROUP2,debrown_88,true,true,,false,true\n"
                + "BGROUP1,CADC Test,true,true,DO NOT DELETE,true,true\n"
                + "MEMGROUP2,someoneelse_4,false,true,,false,false\n"
                + "MEMGROUP3,someoneelse_4,false,true,Member group,false,false\n"
                + "MYGROUP1,CADC Test,true,true,,true,true\n"
                + "MYGROUP3,CADC Test,true,true,THIS IS MY GROUP 3!,true,true\n";

        final String resultCSV = writer.toString();

        assertEquals("Wrong CSV", expectedCSV, resultCSV);

        verify(getMockGMSClient());
    }
}
