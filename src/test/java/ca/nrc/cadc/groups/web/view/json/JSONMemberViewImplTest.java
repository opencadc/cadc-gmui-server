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
 * 9/17/14 - 12:54 PM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */
package ca.nrc.cadc.groups.web.view.json;

import java.io.StringWriter;
import java.io.Writer;

import org.json.JSONWriter;

import org.junit.Test;
import static org.junit.Assert.*;

import ca.nrc.cadc.ac.PersonalDetails;
import ca.nrc.cadc.ac.User;
import ca.nrc.cadc.groups.AbstractGroupsWebTest;


public class JSONMemberViewImplTest
        extends AbstractGroupsWebTest<JSONMemberViewImpl>
{
    @Test
    public void writeMember() throws Exception
    {
        final Writer writer = new StringWriter();
        final JSONWriter jsonWriter = new JSONWriter(writer);
        final User user = testOwner;

        user.personalDetails = new PersonalDetails("CADC", "Test");

        setTestSubject(new JSONMemberViewImpl(jsonWriter, false));

        getTestSubject().write(user);

        assertEquals("Wrong JSON for a Member.", "{\"id\":\"owner\",\"username\":\"owner\",\"name\":\"CADC Test\",\"type\":\"USER\",\"OwnerRights\":\"false\"}",
                     writer.toString());
    }
}
