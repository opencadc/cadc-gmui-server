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
 * 9/24/14 - 9:18 AM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */
package ca.nrc.cadc.groups.web;

import org.junit.Test;
import static org.junit.Assert.*;

import ca.nrc.cadc.groups.AbstractGroupsWebTest;
import ca.nrc.cadc.groups.web.Associate;
import ca.nrc.cadc.groups.web.AssociateType;


public class AssociateTest extends AbstractGroupsWebTest<Associate>
{
    @Test
    public void isUser() throws Exception
    {
        setTestSubject(new Associate("MYID", AssociateType.GROUP));

        assertFalse("Should not be a user.", getTestSubject().isUser());
    }

    @Test
    public void construct() throws Exception
    {
        try
        {
            setTestSubject(new Associate(null, AssociateType.USER));
            fail("Should throw IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Good!
            assertEquals("Wrong message.",
                         "ID of the Group or User is required.",
                         e.getMessage());
        }
    }
}
