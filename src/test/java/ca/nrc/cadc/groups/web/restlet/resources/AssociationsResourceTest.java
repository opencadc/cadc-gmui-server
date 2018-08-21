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
 * 10/15/14 - 1:33 PM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */

package ca.nrc.cadc.groups.web.restlet.resources;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import ca.nrc.cadc.ac.PersonalDetails;
import ca.nrc.cadc.ac.User;
import ca.nrc.cadc.ac.client.UserClient;
import ca.nrc.cadc.auth.HttpPrincipal;
import org.json.JSONObject;
import org.junit.Test;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.skyscreamer.jsonassert.JSONAssert;

import ca.nrc.cadc.ac.client.GMSClient;
import ca.nrc.cadc.groups.web.AssociateCaseInsensitiveComparator;
import ca.nrc.cadc.groups.web.suggest.AssociateMatcherImpl;
import ca.nrc.cadc.groups.web.suggest.RegexSuggesterImpl;

import javax.security.auth.Subject;

import static org.easymock.EasyMock.*;


public class AssociationsResourceTest
    extends AbstractResourceTest<AssociationsResource> {
    @Test
    public void representJSON() throws Exception {
        final Form mockQueryForm = createMock(Form.class);
        final List<User> listResults = new ArrayList<>();

        final User userOne = new User();
        userOne.getIdentities().add(new HttpPrincipal("CADCtest"));
        userOne.personalDetails = new PersonalDetails("CADCtest", "User");

        final User userTwo = new User();
        userTwo.getIdentities().add(new HttpPrincipal("at88mph"));
        userTwo.personalDetails = new PersonalDetails("Marty", "McFly");

        listResults.add(userOne);
        listResults.add(userTwo);

        final List<String> groupNameResults = new ArrayList<>();

        groupNameResults.add("TimeTravellers");
        groupNameResults.add("Temps");

        expect(getMockUserClient().getDisplayUsers()).andReturn(listResults).times(1);
        expect(getMockGMSClient().getGroupNames()).andReturn(groupNameResults).once();

        expect(mockQueryForm.getFirstValue("q")).andReturn("te").once();

        replay(mockQueryForm, getMockGMSClient(), getMockUserClient());

        setTestSubject(
            new AssociationsResource(new RegexSuggesterImpl<>(
                new AssociateMatcherImpl(),
                Integer.MAX_VALUE,
                new AssociateCaseInsensitiveComparator())) {
                /**
                 * Returns the resource reference's optional query. Note that modifications
                 * to the returned {@link org.restlet.data.Form} object aren't reported to the underlying
                 * reference.
                 *
                 * @return The resource reference's optional query.
                 * @see org.restlet.data.Reference#getQueryAsForm()
                 */
                @Override
                public Form getQuery() {
                    return mockQueryForm;
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

                @Override
                public UserClient getUserClient() {
                    return getMockUserClient();
                }

                @Override
                Subject getAuthorizedUser() {
                    return new Subject();
                }
            });

        getTestSubject().refresh();

        final Representation rep = getTestSubject().representJSON();
        final StringWriter writer = new StringWriter();

        rep.write(writer);

        final JSONObject resultJSONObject = new JSONObject(writer.toString());
        final JSONObject expectedJSONObject =
            new JSONObject(
                "{\"matches\": [{\"id\":\"CADCtest - CADCtest User\",\"type\":\"USER\",\"OwnerRights\":\"false\"," +
                    "\"AdminRights\":\"false\"},"
                    + "{\"id\":\"Temps\",\"type\":\"GROUP\",\"OwnerRights\":\"false\",\"AdminRights\":\"false\"}]}");

        JSONAssert.assertEquals(expectedJSONObject, resultJSONObject, true);

        verify(mockQueryForm, getMockGMSClient(), getMockUserClient());
    }

    @Test
    public void representJSONCountLimit() throws Exception {
        final Form mockQueryForm = createMock(Form.class);
        final List<User> listResults = new ArrayList<>();

        final User userOne = new User();
        userOne.getIdentities().add(new HttpPrincipal("CADCtest"));
        userOne.personalDetails = new PersonalDetails("CADCtest", "User");

        final User userTwo = new User();
        userTwo.getIdentities().add(new HttpPrincipal("at88mph"));
        userTwo.personalDetails = new PersonalDetails("Marty", "McFly");

        listResults.add(userOne);
        listResults.add(userTwo);

        final List<String> groupNameResults = new ArrayList<>();

        groupNameResults.add("TimeTravellers");
        groupNameResults.add("Temps");

        expect(getMockUserClient().getDisplayUsers()).andReturn(listResults).times(1);
        expect(getMockGMSClient().getGroupNames()).andReturn(
            groupNameResults).once();

        expect(mockQueryForm.getFirstValue("q")).andReturn("te").once();

        replay(mockQueryForm, getMockGMSClient(), getMockUserClient());

        setTestSubject(
            new AssociationsResource(new RegexSuggesterImpl<>(
                new AssociateMatcherImpl(), 1,
                new AssociateCaseInsensitiveComparator())
            ) {
                /**
                 * Returns the resource reference's optional query. Note
                 * that modifications to the returned
                 * {@link org.restlet.data.Form} object aren't reported to
                 * the underlying reference.
                 *
                 * @return The resource reference's optional query.
                 * @see org.restlet.data.Reference#getQueryAsForm()
                 */
                @Override
                public Form getQuery() {
                    return mockQueryForm;
                }

                /**
                 * Get the client for talking to GMS.  This will pull a new
                 * one each time due to caching.
                 *
                 * @return the GMSClient instance
                 */
                @Override
                public GMSClient getGMSClient() {
                    return getMockGMSClient();
                }

                @Override
                public UserClient getUserClient() {
                    return getMockUserClient();
                }

                @Override
                Subject getAuthorizedUser() {
                    return new Subject();
                }
            });

        getTestSubject().refresh();

        final Representation rep = getTestSubject().representJSON();
        final StringWriter writer = new StringWriter();

        rep.write(writer);

        final JSONObject resultJSONObject = new JSONObject(writer.toString());
        final JSONObject expectedJSONObject =
            new JSONObject(
                "{\"matches\": [{\"id\":\"CADCtest - CADCtest User\",\"type\":\"USER\",\"OwnerRights\":\"false\"," +
                    "\"AdminRights\":\"false\"}],\"remaining\":1}");

        JSONAssert.assertEquals(expectedJSONObject, resultJSONObject, true);

        verify(mockQueryForm, getMockGMSClient(), getMockUserClient());
    }
}
