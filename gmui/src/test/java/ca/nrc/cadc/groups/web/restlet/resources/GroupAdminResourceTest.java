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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ca.nrc.cadc.auth.HttpPrincipal;
import ca.nrc.cadc.groups.web.WebGroupURI;
import org.junit.Test;
import org.restlet.Request;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StreamRepresentation;

import ca.nrc.cadc.ac.Group;
import ca.nrc.cadc.ac.User;
import ca.nrc.cadc.ac.client.GMSClient;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class GroupAdminResourceTest
        extends AbstractResourceTest<GroupAdminResource>
{
    @Test
    public void remove() throws Exception
    {
        setTestSubject(new GroupAdminResource()
        {
            /**
             * Obtain the Group ID. This is double decoded to support the double
             * encoding necessary to fool the browser into submitting an encoded
             * URI.
             *
             * @return The Group's ID in the current context.
             */
            @Override
            protected String getGroupName()
            {
                return "AGROUPXXX";
            }

            @Override
            protected String getAssociateID()
            {
//                return "CN=benjamin sisko,OU=nrc-cnrc.gc.ca,O=grid,C=ca";
                return "bsisko";
            }

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

            @Override
            protected String getAssociateType()
            {
                return "user";
            }
        });

        final Group ag = new Group(new WebGroupURI("AGROUPXXX"));

        User admin1 = new User();
        admin1.getIdentities().add(new HttpPrincipal("jlpicard"));
//        admin1.getIdentities().add(new X500Principal("CN=jean-luc picard,OU=nrc-cnrc.gc.ca,O=grid,C=ca"));
        ag.getUserAdmins().add(admin1);

        User admin2 = new User();
        admin2.getIdentities().add(new HttpPrincipal("bsisko"));
//        admin2.getIdentities().add(new X500Principal("CN=benjamin sisko,OU=nrc-cnrc.gc.ca,O=grid,C=ca"));
        ag.getUserAdmins().add(admin2);
        ag.getGroupAdmins().add(new Group(new WebGroupURI("ADMINGROUP")));

        final InputStream input =
                new ByteArrayInputStream("NOTUSED=NO".getBytes());

        final Representation payload = new StreamRepresentation(MediaType.ALL)
        {
            @Override
            public InputStream getStream() throws IOException
            {
                return input;
            }

            @Override
            public void write(final OutputStream outputStream) throws IOException
            {

            }
        };

        expect(getMockGMSClient().getGroup("AGROUPXXX")).andReturn(ag).once();
        expect(getMockGMSClient().updateGroup(ag)).andReturn(ag).once();

        replay(getMockGMSClient());

        getTestSubject().remove(payload);

        assertEquals("Should only have one user left.", 1,
                     ag.getUserAdmins().size());

        User jeanLuc = ag.getUserAdmins().iterator().next();
//        Set<X500Principal> x500Principals = jeanLuc.getIdentities(X500Principal.class);
//        assertFalse("No X500Principal's found", x500Principals.isEmpty());
        HttpPrincipal httpPrincipal = jeanLuc.getHttpPrincipal();
        assertFalse("No HttpPrincipal found", httpPrincipal == null);

//        String name = x500Principals.iterator().next().getName();
//        assertEquals("Remaining user is CN=jean-luc picard,OU=nrc-cnrc.gc.ca,O=grid,C=ca.",
//                     "CN=jean-luc picard,OU=nrc-cnrc.gc.ca,O=grid,C=ca", name);
        assertEquals("Remaining user is jlpicard", "jlpicard", httpPrincipal.getName());
        assertEquals("Should still have one group left.", 1,
                     ag.getGroupAdmins().size());

        verify(getMockGMSClient());
    }

    @Test
    public void getRequestAttributeValue() throws Exception
    {
        final Request mockRequest = createMock(Request.class);
        final ConcurrentMap<String, Object> c = new ConcurrentHashMap<>();

        c.put("ATTR1", "MYATTR1");
        c.put("ATTR2", "MYATTR2");
        c.put("ATTR3", "IT%20IS%20ENCODED");
        c.put("ATTR4", "MYATTR3+OOP");

        setTestSubject(new GroupAdminResource()
        {
            /**
             * Returns the handled request.
             *
             * @return The handled request.
             */
            @Override
            public Request getRequest()
            {
                return mockRequest;
            }
        });

        expect(mockRequest.getAttributes()).andReturn(c).times(5);

        replay(mockRequest);

        assertEquals("Wrong attribute value.", "MYATTR1",
                     getTestSubject().getRequestAttributeValue("ATTR1"));
        assertEquals("Wrong attribute value.", "IT%20IS%20ENCODED",
                     getTestSubject().getRequestAttributeValue("ATTR3"));
        assertEquals("Wrong attribute value.", "IT IS ENCODED",
                     getTestSubject().getRequestAttributeValue("ATTR3", true));
        assertEquals("Wrong attribute value.", "MYATTR3+OOP",
                     getTestSubject().getRequestAttributeValue("ATTR4", false));
        assertEquals("Wrong attribute value.", "MYATTR3 OOP",
                     getTestSubject().getRequestAttributeValue("ATTR4", true));

        verify(mockRequest);
    }
}
