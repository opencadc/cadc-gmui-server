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
 * 10/16/14 - 11:16 AM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */
package ca.nrc.cadc.groups.web.suggest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import ca.nrc.cadc.groups.AbstractGroupsWebTest;
import ca.nrc.cadc.groups.web.Associate;
import ca.nrc.cadc.groups.web.AssociateCaseInsensitiveComparator;
import ca.nrc.cadc.groups.web.AssociateType;

import static org.junit.Assert.assertEquals;


public class RegexSuggesterImplTest
        extends AbstractGroupsWebTest<RegexSuggesterImpl<Associate>>
{
    @Test
    public void search() throws Exception
    {
        final Set<Associate> testData = new HashSet<Associate>();

        testData.add(new Associate("ORGANIZATION ADMINISTRATORS",
                                   AssociateType.GROUP));
        testData.add(new Associate("DEVELOPERS", AssociateType.GROUP));
        testData.add(new Associate("Delivery", AssociateType.GROUP));
        testData.add(new Associate("Managers", AssociateType.GROUP));
        testData.add(new Associate("Project Vixen", AssociateType.GROUP));
        testData.add(new Associate("mmcfly - Marty McFly", AssociateType.USER));
        testData.add(new Associate("ebrown - Emmett Brown",
                                   AssociateType.USER));
        testData.add(new Associate("mrjedi - Luke Skywalker",
                                   AssociateType.USER));
        testData.add(new Associate("resistanceisfutile - Jean-Luc Picard",
                                   AssociateType.USER));
        testData.add(new Associate("squashbowser - Super Mario",
                                   AssociateType.USER));
        testData.add(new Associate("ShinyCoke - John DeLorean",
                                   AssociateType.USER));
        testData.add(new Associate("worstdelinquent - Alex DeLarge",
                                   AssociateType.USER));
        testData.add(new Associate("needssaving - Princess Peach",
                                   AssociateType.USER));
        testData.add(new Associate("afternooneverybody - Norm Peterson",
                                   AssociateType.USER));

        setTestSubject(new RegexSuggesterImpl<Associate>(
                new AssociateMatcherImpl(), Integer.MAX_VALUE,
                new AssociateCaseInsensitiveComparator()));

        final SuggestionResults<Associate> results =
                getTestSubject().search("del", testData);

        assertEquals("Should have 3 results.", 3, results.getResults().size());
    }

    @Test
    public void searchMax() throws Exception
    {
        final Set<Associate> testData = new HashSet<Associate>();

        testData.add(new Associate("ORGANIZATION ADMINISTRATORS",
                                   AssociateType.GROUP));
        testData.add(new Associate("DEVELOPERS", AssociateType.GROUP));
        testData.add(new Associate("Delivery", AssociateType.GROUP));
        testData.add(new Associate("Managers", AssociateType.GROUP));
        testData.add(new Associate("Project Vixen", AssociateType.GROUP));
        testData.add(new Associate("mmcfly - Marty McFly", AssociateType.USER));
        testData.add(new Associate("ebrown - Emmett Brown",
                                   AssociateType.USER));
        testData.add(new Associate("mrjedi - Luke Skywalker",
                                   AssociateType.USER));
        testData.add(new Associate("resistanceisfutile - Jean-Luc Picard",
                                   AssociateType.USER));
        testData.add(new Associate("squashbowser - Super Mario",
                                   AssociateType.USER));
        testData.add(new Associate("ShinyCoke - John DeLorean",
                                   AssociateType.USER));
        testData.add(new Associate("worstdelinquent - Alex DeLarge",
                                   AssociateType.USER));
        testData.add(new Associate("needssaving - Princess Peach",
                                   AssociateType.USER));
        testData.add(new Associate("afternooneverybody - Norm Peterson",
                                   AssociateType.USER));

        setTestSubject(new RegexSuggesterImpl<Associate>(
                new AssociateMatcherImpl(), 2,
                new AssociateCaseInsensitiveComparator()));

        final SuggestionResults<Associate> results =
                getTestSubject().search("del", testData);

        assertEquals("Should have 2 results.", 2, results.getResults().size());
    }
}
