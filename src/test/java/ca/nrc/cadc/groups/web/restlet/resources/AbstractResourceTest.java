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
 * Oct 7, 2010 - 11:06:33 AM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */
package ca.nrc.cadc.groups.web.restlet.resources;


import static org.easymock.EasyMock.createMock;

import ca.nrc.cadc.ac.client.UserClient;
import org.restlet.data.Form;

import ca.nrc.cadc.ac.client.GMSClient;
import ca.nrc.cadc.groups.AbstractGroupsWebTest;


public abstract class AbstractResourceTest<T extends AbstractResource>
        extends AbstractGroupsWebTest<T>
{
    private Form mockForm = createMock(Form.class);
    private final GMSClient mockGmsClient = createMock(GMSClient.class);
//    private final UserClient mockUserClient = createMock(UserClient.class);


    public GMSClient getMockGMSClient()
    {
        return mockGmsClient;
    }

    public Form getMockForm()
    {
        return mockForm;
    }

}
