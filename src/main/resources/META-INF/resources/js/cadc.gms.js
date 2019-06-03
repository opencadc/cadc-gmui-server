;(function($, undefined) {
  'use strict'
  // register namespace
  $.extend(true, window, {
    cadc: {
      web: {
        gms: {
          i18n: {
            en: {
            	'400_reason': 'Bad request',
                '400_BAD_GROUP_NAME_message':
                  'Group name may not contain space ( ), slash (/), escape (\\), or percent (%)',
                '400_DUPLICATE_MEMBER_message':
                  'One or more members already exist',
                '400_BAD_MEMBER_message': 'One or more members cannot be added',
                '401_reason': 'Login required',
                '401_message':
                  'Authentication is required.  Please use the Login link.',
                '403_reason': 'Forbidden',
                '403_message':
                  'Insufficient permissions, please contact the Group owner.',
                '404_reason': 'Not found',
                '404_message': 'Group or User not found.',
                '404_NO_SUCH_USER_message': 'No such user',
                '404_NO_SUCH_GROUP_message': 'No such group',
                '409_Group_message': 'Group already exists, or it infringes on an existing Group namespace. (eg. JCMT-, CFHT-)',
                '409_Group_reason': 'Conflict',
                '409_Admin_reason': 'Conflict',
                '409_Admin_message':
                  'Administrator already exists, or it infringes on an existing Group namespace. (eg. JCMT-, CFHT-)',
                '409_Member_reason': 'Conflict',
                '409_Member_message':
                  'Member already exists, or it infringes on an existing Group namespace. (eg. JCMT-, CFHT-)',
                '500_reason': 'Server error',
                '500_message': 'Please try again later.',
                '503_reason': 'Unavailable',
                '503_message':
                  'This site is unavailable.  Please try again later.',
                label_required: 'required',
                delete_group_confirmation_title: 'Delete group?',
                edit_link: '/$1/groups/update.html',
                edit_link_label: 'Edit',
                list_header_name: 'Name',
                list_header_owner_name: 'Owner Name',
                list_header_description: 'Description',
                list_header_id: 'ID',
                list_header_members: 'Members',
                list_header_member_name: 'Member Name',
                list_header_remove: 'Remove',
                list_header_admin_name: 'Name',
                list_header_admins: 'Administrators',
                button_close: 'Close',
                button_reset: 'Reset',
                button_done: 'Done',
                button_delete: 'Delete',
                details_form_group_name_label: 'Name',
                details_form_owner_name_label: 'Owner Name',
                details_form_group_description_label: 'Description',
                details_form_group_members_label: 'Members of {1}',
                details_form_group_admins_label: 'Administrators of {1}',
                details_form_group_search_placeholder_members: 'Enter a name',
                details_form_group_search_placeholder_admins: 'Enter a name',
                details_form_add_members_button: 'Add member',
                details_form_add_admins_button: 'Add administrator',
                details_form_submit_button_update: 'Update',
                details_form_submit_button_delete: 'Delete',
                details_form_submit_button_create: 'Create',
                navigation_menu_header_label: 'Groups',
                navigation_menu_new_group_label: 'New Group'
            },
            fr: {
            	 '400_reason': 'Erreur de demande',
                 '400_BAD_GROUP_NAME_message':
                   "Nom du groupe ne peut pas contenir d'espace (), barre oblique (/), évasion (\\), ou pourcent (%)",
                 '400_DUPLICATE_MEMBER_message':
                   'Un ou plus de membres existe déja',
                 '400_BAD_MEMBER_message':
                   'Un ou plus de membres ne peuvent pas être ajouter',
                 '401_reason': 'Connexion requise',
                 '401_message':
                   "Authentification est obligatoire.  S'il vous plais faire connexion.",
                 '403_reason': 'Interdit',
                 '403_message':
                   'Autorisations insuffisantes, contacter le propriétaire du groupe.',
                 '404_reason': 'Non trouvé',
                 '404_message': "Groupe ou Utilisateur n'existe pas.",
                 '404_NO_SUCH_USER_message': "Utilisateur n'existe pas",
                 '404_NO_SUCH_GROUP_message': "Groupe n'existe pas",
                 '409_Group_message': 'Le groupe existe déjà, ou elle porte atteinte à un espace de noms existant. (eg. JCMT-, CFHT-)',
                 '409_Group_reason': 'Contradiction',
                 '409_Member_reason': 'Conflict',
                 '409_Member_message':
                   'Le membre existe déjà, ou elle porte atteinte à un espace de noms existant. (eg. JCMT-, CFHT-)',
                 '409_Admin_reason': 'Conflict',
                 '409_Admin_message':
                   'Le membre existe déjà, ou elle porte atteinte à un espace de noms existant. (eg. JCMT-, CFHT-)',              
                 '500_reason': 'Erreur',
                 '500_message': "S'il vous plais essayer encore plus tard.",
                 '503_reason': 'Indisponible',
                 '503_message':
                   "Le site est indisponible.  S'il vous plais essayer encore plus tard.",
                 label_required: 'obligatoire',
                 delete_group_confirmation_title: 'Effacer la groupe?',
                 edit_link: '/fr/groupes/miseajour.html',
                 edit_link_label: 'Modifier',
                 list_header_name: 'Nom',
                 list_header_owner_name: 'Nom du propriétaire',
                 list_header_description: 'Description',
                 list_header_id: 'Identifiant',
                 list_header_no_of_members: 'Nombre de membres',
                 list_header_members: 'Membres',
                 list_header_member_name: 'Nom du membre',
                 list_header_remove: 'Supprimer',
                 list_header_admin_name: 'Nom',
                 list_header_admins: 'Administrateurs',
                 button_close: 'Fermer',
                 button_reset: 'Réinitialiser',
                 button_done: 'Complet',
                 details_form_group_name_label: 'Nom',
                 details_form_owner_name_label: 'Nom du propriétaire',
                 details_form_group_description_label: 'Description',
                 details_form_group_add_me_label: 'Ajoutez-moi comme membre',
                 details_form_group_members_label: 'Membres de {1}',
                 details_form_group_admins_label: 'Administrateurs de {1}',
                 details_form_group_search_placeholder_members: 'Entrer un nom',
                 details_form_group_search_placeholder_admins: 'Entrer un nom',
                 details_form_add_members_button: 'Ajouter membre',
                 details_form_add_admins_button: 'Ajouter administrateur',
                 details_form_submit_button_update: 'Mise à jour',
                 details_form_submit_button_delete: 'Effacer',
                 details_form_submit_button_create: 'Créer',
                 navigation_menu_header_label: 'Groupes',
                 navigation_menu_new_group_label: 'Créer une groupe'
            }
          },
          resource: 'service',
          Group: Group,
          GroupManager: GroupManager,

          // Events
          events: {
            onGroupLoaded: new jQuery.Event('gms:onGroupLoaded'),
            onGroupLoadedError: new jQuery.Event('gms:onGroupLoadedError'),
            onGroupCreated: new jQuery.Event('gms:onGroupCreated'),
            onGroupCreatedError: new jQuery.Event('gms:onGroupCreatedError'),
            onGroupUpdated: new jQuery.Event('gms:onGroupUpdated'),
            onGroupUpdatedError: new jQuery.Event('gms:onGroupUpdatedError'),
            onGroupDeleted: new jQuery.Event('gms:onGroupDeleted'),
            onGroupDeletedError: new jQuery.Event('gms:onGroupDeletedError'),
            onGroupsLoaded: new jQuery.Event('gms:onGroupsLoaded'),
            onGroupsLoadedError: new jQuery.Event('gms:onGroupsLoadedError'),
            onMemberAdded: new jQuery.Event('gms:onMemberAdded'),
            onMemberAddedError: new jQuery.Event('gms:onMemberAddedError'),
            onMemberDeleted: new jQuery.Event('gms:onMemberDeleted'),
            onMemberDeletedError: new jQuery.Event('gms:onMemberDeletedError'),
            onAdminAdded: new jQuery.Event('gms:onAdminAdded'),
            onAdminAddedError: new jQuery.Event('gms:onAdminAddedError'),
            onAdminDeleted: new jQuery.Event('gms:onAdminDeleted'),
            onAdminDeletedError: new jQuery.Event('gms:onAdminDeletedError'),
            onMembersLoaded: new jQuery.Event('gms:onMembersLoaded'),
            onMembersLoadedError: new jQuery.Event('gms:onMembersLoadedError'),
            onAdminsLoaded: new jQuery.Event('gms:onAdminsLoaded'),
            onAdminsLoadedError: new jQuery.Event('gms:onAdminsLoadedError')
          }
        }
      }
    }
  })

  /**
   *
   * @param _resourceEndpoint     Endpoint for RESTful calls.
   * @constructor
   */
  function GroupManager(_resourceEndpoint) {
    var _selfGroupManager = this
    this.resourceEndpoint = _resourceEndpoint

    function init() {
      // Initialize, using the lang attribute of the html document.
      var i18n = $.i18n()
      i18n.load(cadc.web.gms.i18n[i18n.locale], i18n.locale)
    }

    function getResourceEndpoint() {
      return _selfGroupManager.resourceEndpoint
    }

    function translateField(_entry) {
      return $.i18n(_entry)
    }

    function translateLink(_entry) {
      var dataRole = $('html').data('role')
      if (!dataRole) {
        dataRole = $.i18n().locale
      }

      return $.i18n(_entry, dataRole)
    }

    function getError(_statusCode) {
      return translateField(_statusCode)
    }

    function getErrorReason(_statusCode) {
      return translateField(_statusCode + '_reason')
    }

    function getErrorMessage(jqXHR, key_append) {
      var statusCode = jqXHR.status
      var key = ''  +  (key_append ? key_append : ' ')	// add status code "statusCode +" ?
      														
      // Simplify some messages.
      if (statusCode === 404 || statusCode === 400) {
        key += ' ' + jqXHR.responseText
      } else if (
        statusCode !== 401 &&
        statusCode !== 403 &&
        statusCode !== 409 &&
        statusCode !== 500
      ) {
        key +=  jqXHR.statusText
      }

       key += ' error'

      return translateField(key)
    }

    /**
     * Show the error dialog.
     * @param reason    Reason text.
     * @param message   Message text.
     */
    function showError(reason, message) {
      alert('Error from server: \nReason: ' + reason + '\nMessage: ' + message)
    }

    /**
     * Query the service for a given group.
     *
     * @param _groupName    The unique name of the Group to find.
     */
    function getGroup(_groupName) {
      // Curse the Hash in the Group ID.
      $.getJSON(getResourceEndpoint() + '/group/' + _groupName)
        .done(function(data) {
          loadGroup(data)
        })
        .fail(function(jqxhr, textStatus, error) {
          trigger(cadc.web.gms.events.onGroupLoadedError, {
            textStatus: textStatus,
            error: error,
            request: jqxhr,
            reason: getErrorReason(jqxhr.status),
            message: getErrorMessage(jqxhr)
          })
        })

      subscribe(cadc.web.gms.events.onGroupLoadedError, function(e, data) {
        showError(data.reason, data.message)
      })
    }

    /**
     * Load a single group from the single.
     * @param data
     */
    function loadGroup(data) {
      var g = new Group(data)

      trigger(cadc.web.gms.events.onGroupLoaded, {
        group: g,
        endpoint: getResourceEndpoint() + '/group/' + g.getName()
      })
    }

    /**
     * Render the list of administrators for the given Group ID.
     *
     * @param _groupName    The Group ID to query on.
     * @param _input      The input options to the VOTable Viewer.
     * @param _options    The Grid options to the VOTable Viewer.
     */
    function getAdmins(_groupName, _input, _options) {
      var viewer = new cadc.vot.Viewer(_options.targetNodeSelector, _options)
      viewer.build(
        _input,
        function() {
          loadAdmins(viewer, _groupName)
        },
        function(jqxhr, textStatus, error) {
          var responseText = jqxhr.responseText
          var statusText

          if (responseText.indexOf('NO_SUCH_USER') >= 0) {
            statusText = 'NO_SUCH_USER'
          } else {
            statusText = responseText
          }

          trigger(cadc.web.gms.events.onAdminsLoadedError, {
            textStatus: textStatus,
            error: error,
            request: jqxhr,
            reason: getErrorReason(jqxhr.status),
            message: getErrorMessage({
              status: jqxhr.status,
              responseText: statusText,
              statusText: statusText
            })
          })
        }
      )

      subscribe(cadc.web.gms.events.onAdminsLoadedError, function(e, data) {
        alert(
          'Unable to get administrators: \nReason:' +
            data.reason +
            '\nMessage: ' +
            data.message
        )
      })
    }

    /**
     * Admins load complete.
     *
     * @param _viewer {cadc.vot.Viewer}   VOTable viewer object.
     * @param _groupName   The ID of the group.
     */
    function loadAdmins(_viewer, _groupName) {
      _viewer.render()
      trigger(cadc.web.gms.events.onAdminsLoaded, {
        groupName: _groupName
      })
    }

    /**
     * Add an administrator for the Group whose ID matches the given ID.
     *
     * @param _groupName    The Group ID to add an administrator to.
     * @param _data       The JSON translated from the form for the Admin to
     *                    add.
     */
    function addAdmin(_groupName, _data) {
      $.post(
        getResourceEndpoint() +
          '/group/' +
          encodeURIComponent(_groupName) +
          '/admins',
        _data
      )
        .done(function(data) {
          adminAdded(data)
          colorSuccess() 
          resetForm("admin")
        })
        .fail(function(jqxhr, textStatus, error) {
        	var responseText = jqxhr.responseText
        	var statusText 
        	if (responseText.indexOf('NO_SUCH_USER') >= 0) {
        		statusText = 'NO_SUCH_USER'
        		} else if (responseText.indexOf('NO_SUCH_GROUP') >= 0) {
        			statusText = 'NO_SUCH_GROUP'
                } else {
                  statusText = responseText
                }
        	
          colorFail()
           	
          trigger(cadc.web.gms.events.onAdminAddedError, {
            textStatus: textStatus,
            error: error,
            request: jqxhr,
            reason: getErrorReason(jqxhr.status + '_Admin'),
             message: getErrorMessage({
              status: jqxhr.status,
              responseText: statusText,
              statusText: statusText
            }, 'Admin')
          })
        })
    }

    /**
     * Post success operations.
     *
     * @param _adminData    The data after the call to the server.
     */
    function adminAdded(_adminData) {
      trigger(cadc.web.gms.events.onAdminAdded, {
        admin: _adminData
      })
    }

    /**
     * Remove an administrator from a Group.
     *
     * @param _groupName    The ID of the group to modify.
     * @param _adminID    The ID of the administrator to remove.
     * @param _adminType  The type of the admin to remove.
     */
    function deleteAdmin(_groupName, _adminID, _adminType) {
      var deleteURL =
        getResourceEndpoint() +
        '/group/' +
        _groupName +
        '/admins/' +
        _adminID +
        '?' +
        $.param({ assoc_type: _adminType })

      $.ajax({
        url: deleteURL,
        method: 'DELETE'
      })
        .done(function(message) {
          adminDeleted(_adminID)
        })
        .fail(function(xhr, options, error) {
          trigger(cadc.web.gms.events.onAdminDeletedError, {
            textStatus: options,
            error: error,
            request: xhr,
            reason: 'Not found',
            message: 'No such User or Group Admin'
          })
        })
    }

    /**
     * Admin delete complete.
     *
     * @param _adminID    The ID of the removed Admin.
     */
    function adminDeleted(_adminID) {
      trigger(cadc.web.gms.events.onAdminDeleted, {
        adminID: _adminID
      })
    }

    function getMembers(_groupName, _input, _options) {
      var viewer = new cadc.vot.Viewer(_options.targetNodeSelector, _options)
      viewer.build(
        _input,
        function() {
          loadMembers(viewer, _groupName)
        },
        function(jqxhr, textStatus, error) {
          var responseText = jqxhr.responseText
          var statusText

          if (responseText.indexOf('NO_SUCH_USER') >= 0) {
            statusText = 'NO_SUCH_USER'
          } else if (responseText.indexOf('NO_SUCH_GROUP') >= 0) {
            statusText = 'NO_SUCH_GROUP'
          } else {
            statusText = responseText
          }

          trigger(cadc.web.gms.events.onMembersLoadedError, {
            textStatus: textStatus,
            error: error,
            request: jqxhr,
            reason: getErrorReason(jqxhr.status),
            message: getErrorMessage({
              status: jqxhr.status,
              responseText: statusText,
              statusText: statusText
            })
          })
        }
      )

      subscribe(cadc.web.gms.events.onMembersLoadedError, function(e, data) {
        $('.loader_container').hide()
        alert(
          'Unable to get members: \nReason:' +
            data.reason +
            '\nMessage: ' +
            data.message
        )
      })
    }

    /**
     * Member load complete.
     *
     * @param _viewer {cadc.vot.Viewer}   VOTable viewer object.
     * @param groupName   The ID of the group.
     */
    function loadMembers(_viewer, groupName) {
      _viewer.render()
      trigger(cadc.web.gms.events.onMembersLoaded, {
        groupName: groupName
      })
    }
    
    function resetForm(formType){
    	if(formType==="member") 					 	
    	{
    		$('#add_groups_form').trigger("reset");	
    		 
    		$('#add_users_form').trigger("reset");
    	}    
    	else{
    		$('#add_user_admins_form').trigger("reset");
    		$('#add_group_admins_form').trigger("reset");
    	}
    	//if($('span.text-success').visible)					// if successful, goes back to red after complete.
    
    }
    
    function colorSuccess(){

    	$('span.text-danger').hide()
    	$('span.text-success').text("success").show()
    }

    function colorFail(){
    	$('span.text-success').hide()
    	$('span.text-danger').show()
    }

    /**
     * Add a member to the Group whose ID matches the given one.
     *
     * @param _groupName    The Group ID to add to.
     * @param _data       The JSON that makes up the Member information.
     */
    function addMember(_groupName, _data) {
      $.post(
        getResourceEndpoint() +
          '/group/' +
          encodeURIComponent(_groupName) +
          '/members',
        _data
      )
        .done(function(data) {
           	colorSuccess()
            memberAdded(data)
            resetForm("member")
        })
        .fail(function(jqxhr, textStatus, error) {
          var responseText = jqxhr.responseText
          var statusText

          if (responseText.indexOf('NO_SUCH_USER') >= 0) {
            statusText = 'NO_SUCH_USER'
          } else if (responseText.indexOf('NO_SUCH_GROUP') >= 0) {
            statusText = 'NO_SUCH_GROUP'
          } else {
            statusText = responseText
          } 
          colorFail()		 

          trigger(cadc.web.gms.events.onMemberAddedError, {
            textStatus: textStatus,
            error: error,
            request: jqxhr,
            reason: getErrorReason(jqxhr.status + '_Member'),
            message: getErrorMessage({
              status: jqxhr.status,
              responseText: statusText,
              statusText: statusText
            }, 'Member')
          })
        })
    }

    /**
     * Member added successfully.
     *
     * @param data    JSON data about the member.
     */
    function memberAdded(data) {
      trigger(cadc.web.gms.events.onMemberAdded, {
        member: data
      })
    }

    /**
     * Remove the given member from the group whose ID matches the given one.
     *
     * @param _groupName      The ID of the Group to remove a member from.
     * @param _memberID     The ID of the member to delete.
     * @param _memberType   The type of the member to delete.
     */
    function deleteMember(_groupName, _memberID, _memberType) {
      var deleteURL =
        getResourceEndpoint() +
        '/group/' +
        _groupName +
        '/members/' +
        _memberID +
        '?' +
        $.param({ assoc_type: _memberType })

      $.ajax({
        url: deleteURL,
        method: 'DELETE'
      })
        .done(function(message) {
          memberDeleted(_memberID)
        })
        .fail(function(xhr, options, error) {
          trigger(cadc.web.gms.events.onMemberDeletedError, {
            textStatus: options,
            error: error,
            request: xhr,
            reason: 'Not found',
            message: 'No such User or Group Member'
          })
        })
    }

    /**
     * Group member successfully deleted.
     * @param _memberID   ID of the member deleted.
     */
    function memberDeleted(_memberID) {
      trigger(cadc.web.gms.events.onMemberDeleted, {
        memberID: _memberID
      })
    }

    /**
     * Call the service to get the list of groups.
     */
    function getGroups(input, options) {
      subscribe(cadc.web.gms.events.onGroupsLoadedError, function(e, data) {
        $('.loader_container').hide()

        console.log('Error is ' + data.request.status)

        // Ugly hack to get the right login page!
        if (data.request.status === 401 || data.request.status === 403) {
          $('#auth_modal').modal('show')
        } else {
          showError(data.reason, data.message)
        }
      })

      var viewer = new cadc.vot.Viewer(options.targetNodeSelector, options)
      viewer.build(
        input,
        function() {
          loadGroups(viewer)
        },
        function(jqxhr, textStatus, error) {
          var responseText = jqxhr.responseText
          var statusText =
            responseText.indexOf('NO_SUCH_USER') >= 0
              ? 'NO_SUCH_USER'
              : responseText

          trigger(cadc.web.gms.events.onGroupsLoadedError, {
            textStatus: textStatus,
            error: error,
            request: jqxhr,
            reason: getErrorReason(jqxhr.status),
            message: getErrorMessage({
              status: jqxhr.status,
              responseText: statusText,
              statusText: statusText
            })
          })
        }
      )
    }

    /**
     * Successful group list loading.
     *
     * @param _viewer   The VOTable Viewer to render.
     */
    function loadGroups(_viewer) {
      _viewer.render()
      trigger(cadc.web.gms.events.onGroupsLoaded, {})
    }

    /**
     * PUT to the service to create a new group.
     *
     * @param _data      The serialized group data.
     */
    function createGroup(_data) {
      $.post(getResourceEndpoint() + '/groups', _data)
        .done(function(data) {
          groupCreated(data)
        })
        .fail(function(jqxhr, options, error) {
          trigger(cadc.web.gms.events.onGroupCreatedError, {
            textStatus: options,
            error: error,
            request: jqxhr,
            reason: getErrorReason(jqxhr.status + '_Group'),
            message: getErrorMessage(jqxhr, '_Group')
          })
        })
    }

    /**
     * Successful group creation.
     *
     * @param groupData   The JSON Group data.
     */
    function groupCreated(groupData) {
      trigger(cadc.web.gms.events.onGroupCreated, {
        group: new Group(groupData)
      })
    }

    /**
     * POST to update a group.
     *
     * @param _groupName    The ID of the group to modify.
     * @param data        The new Group data to POST.
     */
    function updateGroup(_groupName, data) {
      var updateURL = getResourceEndpoint() + '/group/' + _groupName

      $.post(updateURL, data)
        .done(function() {
          groupUpdated(_groupName)
        })
        .fail(function(xhr, options, error) {
          trigger(cadc.web.gms.events.onGroupUpdatedError, {
            textStatus: options,
            error: error,
            request: xhr,
            reason: getError(xhr.status + '_reason'),
            message: getErrorMessage(xhr)
          })
        })
    }

    /**
     * Successful group modification.
     *
     * @param _groupName    The ID of the group modified.
     */
    function groupUpdated(_groupName) {
      trigger(cadc.web.gms.events.onGroupUpdated, {
        groupName: _groupName
      })
    }

    /**
     * Delete the given group.
     *
     * @param _groupName    The ID of the group to remove.
     */
    function deleteGroup(_groupName) {
      var deleteURL = getResourceEndpoint() + '/group/' + _groupName

      $.ajax({
        url: deleteURL,
        method: 'DELETE'
      })
        .done(function() {
          trigger(cadc.web.gms.events.onGroupDeleted, {
            groupName: _groupName
          })
        })
        .fail(function(xhr, options, error) {
          trigger(cadc.web.gms.events.onGroupDeletedError, {
            textStatus: options,
            error: error,
            request: xhr,
            reason: getError(xhr.status + '_reason'),
            message: getError(xhr.status + '_message')
          })
        })
    }

    /*
     * Parse the url and build a object or parameter keys and values.
     */
    function getQueryParameters() {
      var parameters = {}
      window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(
        m,
        key,
        value
      ) {
        parameters[key] = value
      })
      return parameters
    }

    function subscribe(event, eHandler) {
      $(_selfGroupManager).on(event.type, eHandler)
    }

    function unsubscribe(event) {
      $(_selfGroupManager).unbind(event.type)
    }

    function trigger(event, eventData) {
      $(_selfGroupManager).trigger(event, eventData)
    }

    // Initialize.
    init()

    $.extend(this, {
      subscribe: subscribe,
      unsubscribe: unsubscribe,
      getGroup: getGroup,
      getMembers: getMembers,
      getAdmins: getAdmins,
      getGroups: getGroups,
      createGroup: createGroup,
      updateGroup: updateGroup,
      deleteGroup: deleteGroup,
      deleteMember: deleteMember,
      deleteAdmin: deleteAdmin,
      addMember: addMember,
      addAdmin: addAdmin,
      getQueryParameters: getQueryParameters,
      translateField: translateField,
      translateLink: translateLink,
      showError: showError,

      // Here for testing.
      loadGroups: loadGroups,
      loadGroup: loadGroup,
      getErrorReason: getErrorReason,
      getErrorMessage: getErrorMessage
    })
  }

  /**
   * Group object.
   */
  function Group(data) {
    var _selfGroup = this

    this.id = data.id
    this.name = data.name
    this.ownerName = data.owner_name
    this.description = data.description

    function getID() {
      return _selfGroup.id
    }

    function getName() {
      return _selfGroup.name
    }

    function getOwnerName() {
      return _selfGroup.ownerName
    }

    function getDescription() {
      return _selfGroup.description
    }

    $.extend(this, {
      getID: getID,
      getName: getName,
      getOwnerName: getOwnerName,
      getDescription: getDescription
    })
  }
})(jQuery)
