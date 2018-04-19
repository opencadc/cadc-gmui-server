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
 * 10/13/14 - 3:25 PM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */
package ca.nrc.cadc.groups.web.restlet.resources;

import javax.security.auth.x500.X500Principal;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ca.nrc.cadc.auth.HttpPrincipal;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StreamRepresentation;

import org.junit.Test;

import ca.nrc.cadc.ac.client.GMSClient;

import static org.easymock.EasyMock.*;


public class GroupMemberResourceTest
        extends AbstractResourceTest<GroupMemberResource>
{
    @Test
    public void remove() throws Exception
    {
        setTestSubject(new GroupMemberResource()
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
                return "MYGROUPXXX";
            }

            @Override
            protected String getAssociateID()
            {
                return "james kirk";
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

        getMockGMSClient().removeUserMember("MYGROUPXXX",
                                            new HttpPrincipal("james kirk"));
        expectLastCall().once();

        replay(getMockGMSClient());

        getTestSubject().remove(payload);

        verify(getMockGMSClient());
    }
}
