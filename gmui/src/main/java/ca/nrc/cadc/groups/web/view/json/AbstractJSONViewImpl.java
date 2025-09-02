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
 * 10/15/14 - 2:30 PM
 *
 *
 *
 ****  C A N A D I A N   A S T R O N O M Y   D A T A   C E N T R E  *****
 ************************************************************************
 */

package ca.nrc.cadc.groups.web.view.json;


import org.json.JSONException;
import org.json.JSONWriter;

import ca.nrc.cadc.groups.web.view.GMSView;


/**
 * Common JSON view mechanism.
 */
public abstract class AbstractJSONViewImpl<T> implements GMSView<T> {
    private final JSONWriter jsonWriter;
    private final JSONViewType viewType;
    private final boolean hasOwnerRights;
    private final boolean hasAdminRights;


    AbstractJSONViewImpl(final JSONWriter jsonWriter, final JSONViewType viewType, final boolean hasOwnerRights,
                         final boolean hasAdminRights) {
        this.jsonWriter = jsonWriter;
        this.viewType = viewType;
        this.hasOwnerRights = hasOwnerRights;
        this.hasAdminRights = hasAdminRights;
    }


    /**
     * Write a JSON Entry.
     *
     * @param key   The Key to write.
     * @param value The Value of the key.
     * @throws JSONException Any writing error.
     */
    void write(final String key, final String value) throws JSONException {
        jsonWriter.key(key).value(value);
    }

    /**
     * Write this group.
     *
     * @param model View item to write.
     * @throws JSONException Anything that can go wrong.
     */
    @Override
    public final void write(final T model) throws JSONException {
        if (viewType == JSONViewType.OBJECT) {
            writeObject(model);
        }
    }

    /**
     * Write a JSON object.
     *
     * @param model The item to write.
     * @throws JSONException Any JSON writing errors.
     */
    private void writeObject(final T model) throws JSONException {
        jsonWriter.object();

        try {
            writeJSON(model);
            write("OwnerRights", Boolean.toString(hasOwnerRights));
            write("AdminRights", Boolean.toString(hasAdminRights));
        } finally {
            jsonWriter.endObject();
        }
    }

    /**
     * Write the JSON contents.
     *
     * @param model The item to write.
     * @throws JSONException Any JSON writing exceptions.
     */
    abstract void writeJSON(final T model) throws JSONException;


    protected enum JSONViewType {
        OBJECT, ARRAY
    }
}
