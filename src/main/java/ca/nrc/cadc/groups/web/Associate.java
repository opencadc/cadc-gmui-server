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
 * 9/24/14 - 9:16 AM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */
package ca.nrc.cadc.groups.web;

import ca.nrc.cadc.util.StringUtil;


/**
 * An associate as it pertains to storing and viewing members and/or
 * administrators to a user.
 */
public class Associate
{
    private final String associateID;
    private final AssociateType associateType;


    /**
     * Constructor.
     *
     * @param associateID       The Associate ID can have multiple meanings.
     *                          In the case where the user is storing a new
     *                          Member and/or Administrator to a Group, this ID
     *                          is the Principal ID, in the case of a User, or
     *                          the Group ID in the case of a Group.  If a
     *                          client is viewing, then the ID represents how
     *                          this Associate appears in a list.
     * @param associateType     Be it a Group or a User.
     */
    public Associate(final String associateID,
                     final AssociateType associateType)
    {
        if (!StringUtil.hasText(associateID))
        {
            throw new IllegalArgumentException(
                    "ID of the Group or User is required.");
        }

        this.associateID = associateID;
        this.associateType = associateType;
    }


    public boolean isUser()
    {
        return associateType == AssociateType.USER;
    }

    public String getID()
    {
        return associateID;
    }
}
