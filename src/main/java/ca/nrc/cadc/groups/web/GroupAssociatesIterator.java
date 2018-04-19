/*
 ************************************************************************
 *******************  CANADIAN ASTRONOMY DATA CENTRE  *******************
 **************  CENTRE CANADIEN DE DONNÉES ASTRONOMIQUES  **************
 *
 *  (c) 2016.                            (c) 2016.
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

package ca.nrc.cadc.groups.web;

import ca.nrc.cadc.ac.Group;
import ca.nrc.cadc.ac.GroupURI;
import ca.nrc.cadc.ac.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GroupAssociatesIterator implements Iterator<List<Object>>
{
    private final boolean[] currentIteratorIsUserContainer =
            new boolean[]{true};

    private final Iterator<User> userIterator;
    private final Iterator<Group> groupIterator;
    private final GroupURI groupID;
    private final boolean hasOwnerRights;


    public GroupAssociatesIterator(final Iterator<User> userIterator,
                                   final Iterator<Group> groupIterator,
                                   final GroupURI groupID,
                                   final boolean hasOwnerRights)
    {
        this.userIterator = userIterator;
        this.groupIterator = groupIterator;
        this.groupID = groupID;
        this.hasOwnerRights = hasOwnerRights;
    }


    @Override
    public boolean hasNext()
    {
        final boolean n;

        if (currentIteratorIsUserContainer[0])
        {
            if (userIterator.hasNext())
            {
                n = true;
            }
            else
            {
                currentIteratorIsUserContainer[0] =
                        false;
                n = hasNext();
            }
        }
        else
        {
            n = groupIterator.hasNext();
        }

        return n;
    }

    @Override
    public List<Object> next()
    {
        final List<Object> row = new ArrayList<>();
        final String nextID;
        final String nextType;

        row.add(groupID.getName());

        if (currentIteratorIsUserContainer[0])
        {
            final User nextUser =
                    userIterator.next();

            row.add(nextUser.personalDetails.getFirstName() + " "
                    + nextUser.personalDetails.getLastName());

            nextID = nextUser.getHttpPrincipal().getName();
            nextType = "USER";
        }
        else
        {
            nextID = groupIterator.next().getID().getName();
            nextType = "GROUP";
            row.add("All members of " + nextID);
        }

        row.add(nextID);
        row.add(nextType);
        row.add(hasOwnerRights);

        return row;
    }

    @Override
    public void remove()
    {
        if (currentIteratorIsUserContainer[0])
        {
            userIterator.remove();
        }
        else
        {
            groupIterator.remove();
        }
    }
}
