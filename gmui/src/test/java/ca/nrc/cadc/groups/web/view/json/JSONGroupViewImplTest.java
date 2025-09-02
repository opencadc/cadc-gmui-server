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
 * 2/28/14 - 10:46 AM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */

package ca.nrc.cadc.groups.web.view.json;


import java.io.StringWriter;
import java.io.Writer;

import ca.nrc.cadc.ac.Group;
import ca.nrc.cadc.groups.AbstractGroupsWebTest;

import ca.nrc.cadc.groups.web.WebGroupURI;
import ca.nrc.cadc.util.ObjectUtil;
import org.json.JSONWriter;
import org.junit.Test;

import static org.junit.Assert.*;


public class JSONGroupViewImplTest extends AbstractGroupsWebTest<JSONGroupViewImpl> {
    @Test
    public void writeGroup() {
        final Group testGroup =
            new Group(new WebGroupURI("MYGROUP"));
        ObjectUtil.setField(testGroup, testOwner, "owner");
        final Writer writer = new StringWriter();
        final JSONWriter jsonWriter = new JSONWriter(writer);

        testGroup.description = "MY GROUP DESCRIPTION.";

        setTestSubject(new JSONGroupViewImpl(jsonWriter, true, true));

        getTestSubject().write(testGroup);

        assertEquals("JSON is wrong.", "{\"name\":\"MYGROUP\",\"owner_name\":\"CADC Test\",\"description\":\"MY GROUP" +
                         " DESCRIPTION.\",\"type\":\"GROUP\",\"OwnerRights\":\"true\",\"AdminRights\":\"true\"}",
                     writer.toString());
    }
}
