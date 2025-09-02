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
 * Oct 7, 2010 - 11:07:11 AM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */
package ca.nrc.cadc.groups;

import javax.security.auth.x500.X500Principal;

import ca.nrc.cadc.auth.HttpPrincipal;
import org.junit.After;

import ca.nrc.cadc.ac.PersonalDetails;
import ca.nrc.cadc.ac.User;


public abstract class AbstractGroupsWebTest<T>
{
    private T testSubject;
    final protected User testOwner = new User();
    final protected User secondTestOwner = new User();

    /**
     * Initialize the Test Owner here.
     */
    protected AbstractGroupsWebTest()
    {
        testOwner.getIdentities().add(new HttpPrincipal("owner"));
        testOwner.getIdentities().add(new X500Principal("CN=owner,OU=CADC,O=HIA,C=CA"));
        testOwner.personalDetails = new PersonalDetails("CADC", "Test");

        secondTestOwner.getIdentities().add(new HttpPrincipal("secondowner"));
        secondTestOwner.getIdentities().add(new X500Principal("CN=secondowner,OU=CADC,O=HIA,C=CA"));
        secondTestOwner.personalDetails = new PersonalDetails("CADC", "SecondTest");
    }


    @After
    public void tearDown()
    {
        setTestSubject(null);
    }


    public T getTestSubject()
    {
        return testSubject;
    }

    public void setTestSubject(T testSubject)
    {
        this.testSubject = testSubject;
    }
}
