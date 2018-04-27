/*
 ************************************************************************
 *******************  CANADIAN ASTRONOMY DATA CENTRE  *******************
 **************  CENTRE CANADIEN DE DONNÉES ASTRONOMIQUES  **************
 *
 *  (c) 2018.                            (c) 2018.
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
 *  $Revision: 4 $
 *
 ************************************************************************
 */

(function ($) {
  // register namespace
  $.extend(true, window, {
    "ca": {
      "nrc": {
        "cadc": {
          "Loginform": LoginForm
        }
      }
    }
  });


  /**
   * Basic Login utility class.
   *
   * @constructor
   */
  function LoginForm()
  {
    this._curUsername = null;
    this._curFormId = null;
    this._curForm = null;

    this.defaultOptions = {
      "formId": "loginForm",
      "submitId": "submitLogin"
    };

    this.init = function (options) {
      if (options && options.hasOwnProperty("formId"))
      {
        this._curFormId = options.formId;
      }
      else
      {
        this._curFormId = this.defaultOptions.formId;
      }

      this.setCurrentForm();
      this.attachListeners();
    };

    /**
     * Obtain whether the given string has any length (i.e. > 0).

     * @returns {boolean}
     */
    this.isLoggedIn = function () {
      return ((this._curUsername !== null) && (this._curUsername.length > 0));
    };

    this.resetLoginFormErrors = function () {
      var $loginFailContainer = this._curForm.find("#login_fail");

      this._curForm.removeClass("has-error");
      $loginFailContainer.text("");
    };

    this.attachListeners = function () {
      this._curForm.find("input.form-control").off().change(function () {
        this.resetLoginFormErrors();
      }.bind(this))
    };

    this.authorizationComplete = function (redirectURL) {
      window.location.replace(redirectURL);
    };

    var $_logout = $("#as-logout");
    if ($_logout)
    {
      $_logout.attr("href", $_logout.attr("href") + "?target="
                            + encodeURIComponent(new cadc.web.util.currentURI().getURI()));
    }

    var requestURI = new cadc.web.util.currentURI();
    var hashValue = requestURI.getHash();

    if (hashValue.indexOf("PASSWORD_RESET_SUCCESS") >= 0)
    {
      var $successMessageContainer = $("#success_message_container");
      $successMessageContainer.parent().removeClass("hidden");
    }

    this.logout = function () {

    };

    /**
     * Getters & setters
     */
    this.getCurrentUsername = function () {
      return this._curUsername;
    };

    this.setCurrentUsername = function (username) {
      this._curUsername = username;
    };

    this.getCurrentFormId = function () {
      return this._curFormId;
    };

    this.setCurrentFormId = function (formId) {
      this._curFormId = formId;
    };

    this.getCurrenfForm = function () {
      return this._curForm;
    };


    this.setCurrentForm = function () {
      this._curForm = $("#" + this._curFormId);

      // Turn submit button into an ajax call
      this._curForm.submit(function () {
        var $_form = this._curForm;
        var $_this = this;
        var formData = $_form.serialize();
        if (formData.indexOf("target=") < 0)
        {
          formData += "&target=" + encodeURIComponent(new cadc.web.util.currentURI().getURI());
        }

        $.ajax(
            {
              url: $_form.attr("action"),
              method: "POST",
              data: formData
            })
            .done(function (message) {
              $_this.authorizationComplete(message);
            })
            .fail(function () {
              // clear the password field and show an error message
              $_form.find("#login_fail").text(
                  "The username or password you entered is incorrect.");
            });

        return false;
      }.bind(this));

    };
  }

})(jQuery);
