/*
 ************************************************************************
 *******************  CANADIAN ASTRONOMY DATA CENTRE  *******************
 **************  CENTRE CANADIEN DE DONNÉES ASTRONOMIQUES  **************
 *
 *  (c) 2019.                            (c) 2019.
 *  Government of Canada                 Gouvernement du Canada
 *  National Research Council            Conseil national de recherches
 *  Ottawa, Canada, K1A 0R6              Ottawa, Canada, K1A 0R6
 *  All rights reserved                  Tous droits réservés
 *
 *  NRC disclaims any warranties,        Le CNRC dénie toute garantie
 *  expressed, implied, or               énoncée, implicite ou légale,
 *  statutory, of any kind with          de quelque nature que ce
 *  respect to the software,             soit, concernant le logiciel,
 *  including without limitation         y compris sans restriction
 *  any warranty of merchantability      toute garantie de valeur
 *  or fitness for a particular          marchande ou de pertinence
 *  purpose. NRC shall not be            pour un usage particulier.
 *  liable in any event for any          Le CNRC ne pourra en aucun cas
 *  damages, whether direct or           être tenu responsable de tout
 *  indirect, special or general,        dommage, direct ou indirect,
 *  consequential or incidental,         particulier ou général,
 *  arising from the use of the          accessoire ou fortuit, résultant
 *  software.  Neither the name          de l'utilisation du logiciel. Ni
 *  of the National Research             le nom du Conseil National de
 *  Council of Canada nor the            Recherches du Canada ni les noms
 *  names of its contributors may        de ses  participants ne peuvent
 *  be used to endorse or promote        être utilisés pour approuver ou
 *  products derived from this           promouvoir les produits dérivés
 *  software without specific prior      de ce logiciel sans autorisation
 *  written permission.                  préalable et particulière
 *                                       par écrit.
 *
 *  This file is part of the             Ce fichier fait partie du projet
 *  OpenCADC project.                    OpenCADC.
 *
 *  OpenCADC is free software:           OpenCADC est un logiciel libre ;
 *  you can redistribute it and/or       vous pouvez le redistribuer ou le
 *  modify it under the terms of         modifier suivant les termes de
 *  the GNU Affero General Public        la “GNU Affero General Public
 *  License as published by the          License” telle que publiée
 *  Free Software Foundation,            par la Free Software Foundation
 *  either version 3 of the              : soit la version 3 de cette
 *  License, or (at your option)         licence, soit (à votre gré)
 *  any later version.                   toute version ultérieure.
 *
 *  OpenCADC is distributed in the       OpenCADC est distribué
 *  hope that it will be useful,         dans l’espoir qu’il vous
 *  but WITHOUT ANY WARRANTY;            sera utile, mais SANS AUCUNE
 *  without even the implied             GARANTIE : sans même la garantie
 *  warranty of MERCHANTABILITY          implicite de COMMERCIALISABILITÉ
 *  or FITNESS FOR A PARTICULAR          ni d’ADÉQUATION À UN OBJECTIF
 *  PURPOSE.  See the GNU Affero         PARTICULIER. Consultez la Licence
 *  General Public License for           Générale Publique GNU Affero
 *  more details.                        pour plus de détails.
 *
 *  You should have received             Vous devriez avoir reçu une
 *  a copy of the GNU Affero             copie de la Licence Générale
 *  General Public License along         Publique GNU Affero avec
 *  with OpenCADC.  If not, see          OpenCADC ; si ce n’est
 *  <http://www.gnu.org/licenses/>.      pas le cas, consultez :
 *                                       <http://www.gnu.org/licenses/>.
 *
 *
 ************************************************************************
 */

package ca.nrc.cadc.groups.web.integration;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


class GMUIPage extends AuthenticatedPage {

    private static final By EDIT_LINK = By.xpath("//button[contains(text(),'Edit')]");
    private static final By FIRST_AUTOCOMPLETE = By.id("ui-id-1");
    private static final By FIRST_AUTOCOMPLETE_DROPDOWN = By.xpath("//ul[@id='ui-id-1']//li[@class='ui-menu-item']");
    private static final By GRID_HEADER_LABEL_SELECTOR = By.cssSelector("span.grid-header-label");
    private static final By GROUP_UPDATE_BUTTON_SELECTOR = By.id("edit_group_update_button");
    private static final By GROUP_DELETE_BUTTON_SELECTOR = By.id("edit_group_delete_button");
    private static final By LOADING_CONTAINER = By.className("loader_container");
    private static final By NAME_FILTER = By.id("Name_filter");
    private static final By NAME_SELECTOR_ADMINS = By.id("admins-search");
    private static final By NAME_SELECTOR_MEMBERS = By.id("members-search");
    private static final By NEW_GROUP_GROUP_NAME_SELECTOR = By.id("add_group_name");
    private static final By NEW_GROUP_SUBMIT_BUTTON_SELECTOR = By.id("add_group_update_button");
    private static final String NEW_GROUP_XPATH = "//a[@data-assoc-id='%s']";
    private static final By NO_ACCESS = By.className("gmui-no-access");
    private static final By SECOND_AUTOCOMPLETE = By.id("ui-id-2");
    private static final By SECOND_AUTOCOMPLETE_DROPDOWN = By.xpath("//ul[@id='ui-id-2']//li[@class='ui-menu-item']");
    private static final By VIEW_LINK = By.xpath("//button[contains(text(),'View')]");

    @FindBy(id = "add_group_button")
    WebElement addGroupButton;

    @FindBy(id = "edit_group_delete_button")
    WebElement deleteGroupButton;

    @FindBy(className = "edit_group_link")
    WebElement groupSelectButton;

    @FindBy(className = "edit_admins_link")
    WebElement adminViewButton;

    @FindBy(className = "edit_members_link")
    WebElement memberViewButton;

    @FindBy(id = "close_admins_modal")
    WebElement adminModalClose;

    @FindBy(id = "close_members_modal")
    WebElement memberModalClose;

    @FindBy(id = "add_button_admins")
    WebElement addAdminButton;

    @FindBy(id = "add_button_members")
    WebElement addMemberButton;


    GMUIPage(final WebDriver _driver) throws Exception {
        super(_driver);
        waitForElementPresent(GRID_HEADER_LABEL_SELECTOR);

        PageFactory.initElements(driver, this);
    }

    /**
     * Add a new group.
     *
     * @param newGroupName New group name to use.
     * @return The refreshed page.
     *
     * @throws Exception Any errors.
     */
    GMUIPage createGroup(final String newGroupName) throws Exception {
        click(addGroupButton);

        waitForElementVisible(NEW_GROUP_GROUP_NAME_SELECTOR);
        waitForElementClickable(NEW_GROUP_SUBMIT_BUTTON_SELECTOR);

        inputTextValue(NEW_GROUP_GROUP_NAME_SELECTOR, newGroupName);
        click(NEW_GROUP_SUBMIT_BUTTON_SELECTOR);

        waitForElementInvisible(NEW_GROUP_SUBMIT_BUTTON_SELECTOR);

        return new GMUIPage(driver);
    }

    /**
     * Open Group Update Panel for specificed group name.
     *
     * @param groupName New group name to use.
     * @return The refreshed page.
     *
     * @throws Exception Any errors.
     */
    void openGroupUpdatePanel(final String groupName) throws Exception {
        filterName(groupName.substring(0, 5));

        // Seems to need the first click to get focus into the lower portion of the table viewer... :(
        click(groupSelectButton);
        click(groupSelectButton);
        waitForElementVisible(GROUP_UPDATE_BUTTON_SELECTOR);
    }

    /**
     * Delete a group.
     *
     * @param groupName Name of group to delete.
     * @return The refreshed page.
     *
     * @throws Exception Any errors.
     */
    GMUIPage deleteGroup(final String groupName) throws Exception {
        openGroupUpdatePanel(groupName);
        waitForElementVisible(GROUP_DELETE_BUTTON_SELECTOR);
        click(deleteGroupButton);

        // Acknowledge alert box that comes up, then switch
        // back to main window
        String windowHandle = driver.getWindowHandle();
        driver.switchTo().alert().accept();
        driver.switchTo().window(windowHandle);

        return new GMUIPage(driver);
    }

    /**
     * Use Name filter on main page.
     *
     * @param groupName
     * @throws Exception
     */
    void filterName(final String groupName) throws Exception {
        waitForElementVisible(NAME_FILTER);
        inputTextValue(NAME_FILTER, groupName);
    }

    /**
     * Scroll a container (e.g. div) until the element with elementID is
     * visible.
     *
     * @param selector            The ID of the element to find.
     * @param containerToScrollID The container to scroll.
     */
    protected void scrollVerticallyIntoView(final String selector, final String containerToScrollID) {
        final String script = "var myElement = document.querySelector(\"" + selector + "\");"
                + "var topPos = 35;"
                + "while (myElement === null) {"
                + "topPos += 35;"
                + "document.getElementById('" + containerToScrollID + "').scrollTop = topPos;"
                + "myElement = document.querySelector(\"" + selector + "\");"
                + "}";

        ((JavascriptExecutor) driver).executeScript(script);
    }

    /**
     * Open Administrators modal for the named group.
     *
     * @param groupName
     * @throws Exception
     */
    void selectAdminsPanel(final String groupName) throws Exception {

        filterName(groupName.substring(0, 5));

        // Seems to need the first click to get focus into the lower portion of the table viewer... :(
        click(adminViewButton);
        click(adminViewButton);
    }

    /**
     * Open the Members modal for the named group.
     *
     * @param groupName
     * @throws Exception
     */
    void selectMembersPanel(final String groupName) throws Exception {

        filterName(groupName.substring(0, 5));

        // Seems to need the first click to get focus into the lower portion of the table viewer... :(
        click(memberViewButton);
        click(memberViewButton);
    }

    /**
     * Find a member name in the Member or Admin list for a group. Requires that list modal be open.
     *
     * @param memberName
     * @throws Exception
     */
    void findMember(final String memberName) throws Exception {
        // Verify member was added
        By newMemberXpath = By.xpath(String.format(NEW_GROUP_XPATH, memberName));
        waitForElementVisible(newMemberXpath);
    }

    /**
     * Add the member (user or group name) to the members list to the group.
     *
     * @param groupName
     * @param memberName
     * @return
     *
     * @throws Exception
     */
    GMUIPage addMemberToGroup(final String groupName, final String memberName) throws Exception {
        selectMembersPanel(groupName);

        waitForElementClickable(addMemberButton);

        inputTextValue(NAME_SELECTOR_MEMBERS, memberName);
        waitForElementVisible(FIRST_AUTOCOMPLETE);
        click(FIRST_AUTOCOMPLETE_DROPDOWN);
        click(addMemberButton);
        waitForElementInvisible(LOADING_CONTAINER);

        // Verify member was added
        findMember(memberName);

        click(memberModalClose);
        return new GMUIPage(driver);
    }

    /**
     * Add the member (user or group name) to the administrators list for the named group.
     *
     * @param groupName
     * @param memberName
     * @throws Exception
     */
    GMUIPage addAdminToGroup(final String groupName, final String memberName) throws Exception {
        selectAdminsPanel(groupName);
        waitForElementClickable(addAdminButton);

        inputTextValue(NAME_SELECTOR_ADMINS, memberName);
        waitForElementVisible(SECOND_AUTOCOMPLETE);

        click(SECOND_AUTOCOMPLETE_DROPDOWN);
        click(addAdminButton);
        waitForElementInvisible(LOADING_CONTAINER);

        // Verify member was added
        findMember(memberName);

        click(adminModalClose);
        return new GMUIPage(driver);
    }

    /**
     * Verify the current user has administrator access to this group.
     *
     * @param groupName
     * @return
     *
     * @throws Exception
     */
    boolean checkAdminGroup(final String groupName) throws Exception {
        filterName(groupName);

        List<WebElement> actionLinks = driver.findElements(EDIT_LINK);

        // For the selected group, there should be 2 Edit links
        if (actionLinks.size() == 2) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verify the current user only has member access to this group.
     *
     * @param groupName
     * @return
     *
     * @throws Exception
     */
    boolean checkMemberGroup(final String groupName) throws Exception {
        filterName(groupName);

        List<WebElement> editLinks = driver.findElements(NO_ACCESS);
        List<WebElement> viewLinks = driver.findElements(VIEW_LINK);

        // For the selected group, there should be 1 Edit link and 1 View link
        if (editLinks.size() == 1 && viewLinks.size() == 1) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Delete member from group (administrator or member). Requires modal panel be open.
     *
     * @param memberName
     * @throws Exception
     */
    void deleteGroupMember(final String memberName) throws Exception {
        By newMemberXpath = By.xpath(String.format(NEW_GROUP_XPATH, memberName));

        // Verify member is in this group
        findMember(memberName);
        waitForElementClickable(newMemberXpath);
        click(newMemberXpath);

        // Member name hangs around a small bit after the loading
        // spinner is cleared.
        waitForElementInvisible(LOADING_CONTAINER);
        waitForElementInvisible(newMemberXpath);
    }

    /**
     * Delete the member (user or group name) from the members list of the group.
     *
     * @param groupName
     * @param memberName
     * @return
     *
     * @throws Exception
     */
    GMUIPage deleteUserFromMembersGroup(final String groupName, final String memberName) throws Exception {
        selectMembersPanel(groupName);
        deleteGroupMember(memberName);
        click(memberModalClose);
        return new GMUIPage(driver);
    }

    /**
     * Delete the member (user or group name) from the administrators list of the group.
     *
     * @param groupName
     * @param memberName
     * @return
     *
     * @throws Exception
     */
    GMUIPage deleteUserFromAdminsGroup(final String groupName, final String memberName) throws Exception {
        selectAdminsPanel(groupName);
        deleteGroupMember(memberName);
        click(adminModalClose);
        return new GMUIPage(driver);
    }

}
