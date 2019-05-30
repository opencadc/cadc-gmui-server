;(function($, window, undefined) {
  'use strict'

  // register namespace
  $.extend(true, window, {
    App: GMUI
  })

  /**
   * GMUI Listing application
   * @constructor
   * @param {} _options
   *        _options{grid_offset: 100}  Offset for the grid to the bottom of the page.
   */
  function GMUI(_options) {
    /**
     * Start the application.
     */
    function loadGroups() {
      var LOADER_CONTAINER_SELECTOR = '.loader_container'
      var GRID_OUTER_CONTAINER_SELECTOR = '#content_column_main_inner'
      var CSS_INVISIBLE = 'sr-only'

      var groupManager = new cadc.web.gms.GroupManager(cadc.web.gms.resource)

      var $associatesContainers = $('div.associates')
      var $groupContainers = $('div.group_container')
      var $editMembersContainer = $('#edit_members_container')
      var $editAdminsContainer = $('#edit_admins_container')
      var $newGroupContainer = $('#new_group_container')
      var $editGroupContainer = $('#edit_group_container')
      var $groupForm = $("form[name='groupForm']")

      $('#add_group_button')
        .find('span.button_text')
        .text(groupManager.translateField('navigation_menu_new_group_label'))

      // Add Group modal
      $('#add_group_modal_label').text(
        groupManager.translateField('navigation_menu_new_group_label')
      )
      $('#add_group_name_label')
        .text(
          groupManager.translateField('details_form_group_name_label') + ': '
        )
        .append(
          '<strong> (' +
            groupManager.translateField('label_required') +
            ')</strong>'
        )
      $('#add_group_description_label').text(
        groupManager.translateField('details_form_group_description_label')
      )
      $('#add_group_update_button').text(
        groupManager.translateField('details_form_submit_button_create')
      )
      $('#add_group_reset_button').text(
        groupManager.translateField('button_reset')
      )
      $('#add_group_close_button').text(
        groupManager.translateField('button_close')
      )

      // Edit Group modal
      $('#edit_group_name_label')
        .text(
          groupManager.translateField('details_form_group_name_label') + ': '
        )
        .append(
          '<strong> (' +
            groupManager.translateField('label_required') +
            ')</strong>'
        )
      $('#edit_group_description_label').text(
        groupManager.translateField('details_form_group_description_label')
      )
      $('#edit_group_update_button').text(
        groupManager.translateField('details_form_submit_button_update')
      )
      $('#edit_group_delete_button').text(
        groupManager.translateField('details_form_submit_button_delete')
      )
      $('#edit_group_reset_button').text(
        groupManager.translateField('button_reset')
      )
      $('#edit_group_close_button').text(
        groupManager.translateField('button_close')
      )

      var groupName = groupManager.translateField('list_header_name')
      var groupOwner = groupManager.translateField('list_header_owner_name')
      var groupAdmins = groupManager.translateField('list_header_admins')
      var groupMembers = groupManager.translateField('list_header_members')
      var groupDescription = groupManager.translateField(
        'list_header_description'
      )

      // Options for the three VOTable Viewers.
      var actionColumnName = '_' + groupName

      // Fields for main Group List grid.
      var fields = [
        new cadc.vot.Field(
          groupName,
          groupName,
          null,
          'gms.group_name',
          null,
          null,
          new cadc.vot.Datatype('char'),
          '*',
          null,
          groupName
        ),
        new cadc.vot.Field(
          groupOwner,
          groupOwner,
          null,
          'gms.group_owner',
          null,
          null,
          new cadc.vot.Datatype('char'),
          '*',
          null,
          groupOwner
        ),
        new cadc.vot.Field(
          groupAdmins,
          groupAdmins,
          null,
          'gms.group_admins',
          null,
          null,
          new cadc.vot.Datatype('char'),
          '*',
          null,
          groupAdmins
        ),
        new cadc.vot.Field(
          groupMembers,
          groupMembers,
          null,
          'gms.group_members',
          null,
          null,
          new cadc.vot.Datatype('char'),
          '*',
          null,
          groupMembers
        ),
        new cadc.vot.Field(
          groupDescription,
          groupDescription,
          null,
          'gms.group_description',
          null,
          null,
          new cadc.vot.Datatype('char'),
          '*',
          null,
          groupDescription
        ),
        new cadc.vot.Field(
          'OwnerRights',
          'OwnerRights',
          null,
          'gms.group_owner_rights',
          null,
          null,
          new cadc.vot.Datatype('char'),
          '*',
          null,
          'OwnerRights'
        ),
        new cadc.vot.Field(
          'AdminRights',
          'AdminRights',
          null,
          'gms.group_admin_rights',
          null,
          null,
          new cadc.vot.Datatype('char'),
          '*',
          null,
          'AdminRights'
        )
      ]

      var memberName = groupManager.translateField('list_header_member_name')
      var memberFields = [
        new cadc.vot.Field(
          ' ',
          actionColumnName,
          null,
          'gms.member_remove_link',
          null,
          null,
          new cadc.vot.Datatype('char'),
          '*',
          null,
          ''
        ),
        new cadc.vot.Field(
          memberName,
          memberName,
          null,
          'gms.member_name',
          null,
          null,
          new cadc.vot.Datatype('char'),
          '*',
          null,
          memberName
        ),
        new cadc.vot.Field(
          'MemberID',
          'MemberID',
          null,
          'gms.member_id',
          null,
          null,
          new cadc.vot.Datatype('char'),
          '*',
          null,
          'MemberID'
        ),
        new cadc.vot.Field(
          'Type',
          'Type',
          null,
          'gms.member_type',
          null,
          null,
          new cadc.vot.Datatype('char'),
          '*',
          null,
          'Type'
        ),
        new cadc.vot.Field(
          'OwnerRights',
          'OwnerRights',
          null,
          'gms.group_owner_rights',
          null,
          null,
          new cadc.vot.Datatype('char'),
          '*',
          null,
          'OwnerRights'
        ),
        new cadc.vot.Field(
          'AdminRights',
          'AdminRights',
          null,
          'gms.group_admin_rights',
          null,
          null,
          new cadc.vot.Datatype('char'),
          '*',
          null,
          'AdminRights'
        )
      ]

      var adminName = groupManager.translateField('list_header_admin_name')
      var adminFields = [
        new cadc.vot.Field(
          ' ',
          actionColumnName,
          null,
          'gms.admin_remove_link',
          null,
          null,
          new cadc.vot.Datatype('char'),
          '*',
          null,
          ''
        ),
        new cadc.vot.Field(
          adminName,
          adminName,
          null,
          'gms.admin_name',
          null,
          null,
          new cadc.vot.Datatype('char'),
          '*',
          null,
          adminName
        ),
        new cadc.vot.Field(
          'AdminID',
          'AdminID',
          null,
          'gms.admin_id',
          null,
          null,
          new cadc.vot.Datatype('char'),
          '*',
          null,
          'AdminID'
        ),
        new cadc.vot.Field(
          'Type',
          'Type',
          null,
          'gms.admin_type',
          null,
          null,
          new cadc.vot.Datatype('char'),
          '*',
          null,
          'Type'
        ),
        new cadc.vot.Field(
          'OwnerRights',
          'OwnerRights',
          null,
          'gms.group_owner_rights',
          null,
          null,
          new cadc.vot.Datatype('char'),
          '*',
          null,
          'OwnerRights'
        ),
        new cadc.vot.Field(
          'AdminRights',
          'AdminRights',
          null,
          'gms.group_admin_rights',
          null,
          null,
          new cadc.vot.Datatype('char'),
          '*',
          null,
          'AdminRights'
        )
      ]

      var metaData = new cadc.vot.Metadata()
      metaData.setFields(fields)

      var memberMetaData = new cadc.vot.Metadata()
      memberMetaData.setFields(memberFields)

      var adminMetaData = new cadc.vot.Metadata()
      adminMetaData.setFields(adminFields)

      var input = {
        tableMetadata: metaData,
        url: 'service/groups',
        useRelativeURL: true,
        pageSize: 100
      }

      var memberInput = {
        tableMetadata: memberMetaData,
        useRelativeURL: true,
        pageSize: 100
      }

      var adminInput = {
        tableMetadata: adminMetaData,
        useRelativeURL: true,
        pageSize: 100
      }

      var removeMemberLinkOptions = {
        fitMax: false,
        width: 20,
        sortable: false,
        filterable: false,
        formatter: function(row, cell, value, columnDef, dataContext) {
          var stringHTML

          if (dataContext['AdminRights'] === 'true') {
            stringHTML =
              '<span class="cellValue"><a href="#" class="remove_assoc_link" data-association="members" data-assoc-id="' +
              dataContext['MemberID'] +
              '" ' +
              'data-assoc-type="' +
              dataContext['Type'] +
              '" ' +
              '" data-group-name="' +
              dataContext[groupName] +
              '" ' +
              'title="Remove ' +
              dataContext['MemberID'] +
              ' from this Group.">' +
              '<span class="glyphicon glyphicon-remove text-danger"></span></a></span>'
          } else {
            stringHTML = '<span class="cellValue"></span>'
          }

          return stringHTML
        }
      }

      var removeAdminLinkOptions = {
        fitMax: false,
        width: 20,
        sortable: false,
        filterable: false,
        formatter: function(row, cell, value, columnDef, dataContext) {
          var stringHTML

          if (dataContext['AdminRights'] === 'true') {
            stringHTML =
              '<span class="cellValue"><a href="#" class="remove_assoc_link" data-association="admins" data-assoc-id="' +
              dataContext['AdminID'] +
              '" ' +
              'data-assoc-type="' +
              dataContext['Type'] +
              '" ' +
              '" data-group-name="' +
              dataContext[groupName] +
              '" ' +
              'title="Remove ' +
              dataContext['AdminID'] +
              ' from this Group.">' +
              '<span class="glyphicon glyphicon-remove text-danger"></span></a></span>'
          } else {
            stringHTML = '<span class="cellValue"></span>'
          }

          return stringHTML
        }
      }

      var groupAdminsLinkOptions = {
        sortable: false,
        resizeable: false,
        filterable: false,
        formatter: function(row, cell, value, columnDef, dataContext) {
          return (
            '<span class="cellValue">' +
            '<button class="btn btn-link btn-xs edit_admins_link" data-toggle="modal" data-target="#edit_admins_modal" ' +
            'data-group-admin="' +
            (dataContext[groupAdmins] === 'true') +
            '" ' +
            'data-group-name="' +
            dataContext[groupName] +
            '">' +
            'View </button></span>'
          )
        }
      }

      var groupMembersLinkOptions = {
        sortable: false,
        resizeable: false,
        filterable: false,
        formatter: function(row, cell, value, columnDef, dataContext) {
          return (
            '<span class="cellValue">' +
            '<button class="btn btn-link btn-xs edit_members_link" data-toggle="modal" data-target="#edit_members_modal" ' +
            'data-group-admin="' +
            (dataContext[groupAdmins] === 'true') +
            '" ' +
            'data-group-name="' +
            dataContext[groupName] +
            '">' +
            'View </button></span>'
          )
        }
      }

      var groupNameLinkOptions = {
        formatter: function(row, cell, value, columnDef, dataContext) {
          return (
            '<span class="cellValue">' +
            '<button class="btn btn-link btn-sm edit_group_link" data-toggle="modal" data-target="#edit_group_modal" ' +
            'data-group-admin="' +
            (dataContext[groupAdmins] === 'true') +
            '" ' +
            'data-group-owner="' +
            (dataContext['OwnerRights'] === 'true') +
            '" ' +
            'data-group-name="' +
            dataContext[groupName] +
            '">' +
            value +
            '</button></span>'
          )
        }
      }

      var colOpts = {}
      colOpts[groupAdmins] = groupAdminsLinkOptions
      colOpts[groupMembers] = groupMembersLinkOptions
      colOpts[groupName] = groupNameLinkOptions

      var memberColOpts = {}
      memberColOpts[actionColumnName] = removeMemberLinkOptions
      memberColOpts[memberName] = {
        width: 535
      }

      var adminColOpts = {}
      adminColOpts[actionColumnName] = removeAdminLinkOptions
      adminColOpts[adminName] = {
        width: 535
      }

      var genericOptions = {
        editable: false,
        enableAddRow: false,
        showHeaderRow: true,
        enableCellNavigation: true,
        asyncEditorLoading: true,
        enableAsyncPostRender: true,
        explicitInitialization: true,
        fullWidthRows: true,
        headerRowHeight: 30,
        showTopPanel: false,
        defaultColumnWidth: 50,
        pager: false,
        multiSelect: true,
        leaveSpaceForNewRows: false,
        sortDir: 'asc'
      }

      var options = $.extend(
        true,
        {
          sortColumn: groupName, // ID of
          // the
          // default
          // sort
          // column.
          defaultColumnIDs: [
            groupName,
            groupOwner,
            groupAdmins,
            groupMembers,
            groupDescription
          ],
          heightOffset: _options.grid_offset,
          variableViewportHeight: true,
          targetNodeSelector: '#group_list_grid',
          columnManager: {
            filterable: true,
            fitMax: true,
            forceFitColumns: true,
            forceFitColumnMode: 'max',
            resizable: true
          },
          columnOptions: colOpts
        },
        genericOptions
      )

      var memberViewOptions = $.extend(
        true,
        {
          sortColumn: memberName, // ID of the default sort column.
          defaultColumnIDs: [actionColumnName, memberName],
          targetNodeSelector: '#members_grid',
          columnManager: {
            filterable: true,
            fitMax: false,
            forceFitColumns: false,
            // forceFitColumnMode:
            // "max",
            resizable: true
          },
          columnOptions: memberColOpts
        },
        genericOptions
      )

      var adminViewOptions = $.extend(
        true,
        {
          sortColumn: adminName, // ID of the default sort column.
          defaultColumnIDs: [actionColumnName, adminName],
          targetNodeSelector: '#admins_grid',
          columnManager: {
            filterable: true,
            fitMax: true,
            forceFitColumns: true,
            forceFitColumnMode: 'max',
            resizable: true
          },
          columnOptions: adminColOpts
        },
        genericOptions
      )

      // End options for the three VOTable Viewers.
      $('input:reset').click(function(event) {
        event.preventDefault()
        var $thisForm = $(this).parents('form')

        $thisForm.find('#submit_error').empty()
        $thisForm.find('input:text:enabled').val('')
        $thisForm.find('textarea:enabled').val('')
      })

      $groupForm.find(':submit').click(function(event) {
        $groupForm.val($(event.target).val())
      })

      // Edit, create, or delete a group.
      $groupForm.submit(function(event) {
        event.preventDefault()
        var $thisForm = $(this)
        var buttonValue = $thisForm.val()
        clearMessageContainer($thisForm)
        var $formContainer = $thisForm.parents('.group_container')
        var operation = $formContainer.data('operate')
        $formContainer.find(LOADER_CONTAINER_SELECTOR).show()

        if (operation === 'update') {
          if (buttonValue === 'DELETE') {
            if (
              confirm(
                groupManager.translateField('delete_group_confirmation_title')
              )
            ) {
              groupManager.deleteGroup($formContainer.data('group-name'))
            }
          } else {
            groupManager.updateGroup(
              $formContainer.data('group-name'),
              $thisForm.serialize()
            )
          }
        } else {
          groupManager.createGroup($thisForm.serialize())
        }
      })

      // Handle the container opening.
      function containerOpen($container) {
        $container.find(LOADER_CONTAINER_SELECTOR).show()

        var association = $container.data('association')
        $container.find('#group-' + association).empty()
      }

      function setAutocompleteMessageText(_text) {
        $('span.ui-autocomplete-message').text(_text)
      }

      function clearAutompleteLoading($autocompleteInput) {
        $autocompleteInput.removeClass('ui-autocomplete-loading')
      }

      function clearMessageContainer(_$form) {
        var $messageContainer = _$form.find('.form-message')
        $messageContainer.removeClass('color-accent')
        $messageContainer.removeClass('color-attention')
        $messageContainer.addClass('color-attention')
        $messageContainer.text('')
      }

      function containerClose($container) {
        var $editForm = $container.find('form')
        var association = $container.data('association')

        clearMessageContainer($editForm)

        $editForm.trigger('reset')
        $editForm.find('textarea').text('')
        $container.find('.group-name-title').text('')
        $editForm.find('#group-' + association).empty()
        $container.find('.associate_grid').empty()
        $container.find('.grid-header-label').text('')
        clearAutompleteLoading($container.find('.ui-autocomplete-input'))

        setAutocompleteMessageText('')
      }

      function editLinkClicked($link, $container) {
        $container.data('group-name', $link.data('group-name'))
        $container.data('group-admin', $link.data('group-admin'))
        $container.data('group-owner', $link.data('group-owner'))
      }

      // Pass this group's ID to the popup.
      $(document).on('click', '.edit_group_link', function() {
        editLinkClicked($(this), $editGroupContainer)
      })

      $(document).on('click', '.remove_assoc_link', function() {
        var $thisLink = $(this)
        var assocation = $thisLink.data('association')
        var assocType = $thisLink.data('assoc-type')
        var associationID = $thisLink.data('assoc-id')

        if (assocation === 'members') {
          $editMembersContainer.find(LOADER_CONTAINER_SELECTOR).show()

          groupManager.deleteMember(
            $editMembersContainer.data('group-name'),
            associationID,
            assocType
          )
        } else {
          $editAdminsContainer.find(LOADER_CONTAINER_SELECTOR).show()

          groupManager.deleteAdmin(
            $editAdminsContainer.data('group-name'),
            associationID,
            assocType
          )
        }
      })

      // Pass this group's ID to the popup.
      $(document).on('click', '.edit_members_link', function() {
        editLinkClicked($(this), $editMembersContainer)
      })

      // Pass this group's ID to the popup.
      $(document).on('click', '.edit_admins_link', function() {
        editLinkClicked($(this), $editAdminsContainer)
      })

      // Setup the new group dialog.
      $groupContainers.on('show.bs.modal', function(event) {
        var $thisContainer = $(event.target)
        var operation = $thisContainer.data('operate')
        $thisContainer
          .find('label.group-name_label')
          .text(
            groupManager.translateField('details_form_group_name_label') + ': '
          )
          .append(
            '<strong> (' +
              groupManager.translateField('label_required') +
              ')</strong>'
          )
        $thisContainer
          .find('label.groupDescription_label')
          .text(
            groupManager.translateField(
              'details_form_group_description_label'
            ) + ':'
          )

        // Buttons
        $thisContainer
          .find('button.group-form-delete')
          .text(
            groupManager.translateField('details_form_submit_button_delete')
          )
        $thisContainer
          .find('button.group-form-submit')
          .text(
            groupManager.translateField(
              'details_form_submit_button_' + operation
            )
          )
        $thisContainer
          .find('.close_form')
          .text(groupManager.translateField('button_close'))
        $thisContainer
          .find('.reset_form')
          .val(groupManager.translateField('button_reset'))
      })

      $groupContainers.on('hidden.bs.modal', function() {
        var $thisContainer = $(this)
        var $thisForm = $thisContainer.find('form')

        $thisForm.trigger('reset')
        $thisForm.find("input[type='hidden']").val('')
        $thisForm.find('textarea').text('')
        clearMessageContainer($thisForm)
      })

      // Setup dialogs to view/edit members and administrators.
      $associatesContainers.on('show.bs.modal', function(event) {
        var $thisContainer = $(this)
        var association = $thisContainer.data('association')
        var stringUtil = new org.opencadc.StringUtil()
        var translatedMessage = groupManager.translateField(
          'details_form_group_' + association + '_label'
        )
        var groupName = $(event.relatedTarget).data('group-name')

        $thisContainer
          .find('.modal-title')
          .text(stringUtil.format(translatedMessage, [groupName]))

        $thisContainer
          .find('#' + association + '-search')
          .attr(
            'placeholder',
            groupManager.translateField(
              'details_form_group_search_placeholder_' + association
            )
          )

        // Buttons
        $thisContainer
          .find('#done_form_' + association)
          .text(groupManager.translateField('button_done'))
        $thisContainer
          .find('#add_button_' + association)
          .text(
            groupManager.translateField(
              'details_form_add_' + association + '_button'
            )
          )
      })

      // Ensure the form submission event is only attached once!
      // WebRT 75162
      // jenkinsd 2018.03.12
      //
      $associatesContainers.find('form').submit(function(event) {
        event.preventDefault()

        var $thisForm = $(this)
        var $thisContainer = $thisForm.parents('div.associates')
        var groupName = $thisContainer.data('group-name')
        var $thisFormInput = $thisForm.find('input.assoc-search')
        var type = $thisForm.find("input[name='assoc-type']").val()

        if (type) {
          //$thisForm.find("input[name='assoc-type']").val(type)
          $thisContainer.find(LOADER_CONTAINER_SELECTOR).show()

          if ($thisContainer.data('association') === 'members') {
            groupManager.addMember(groupName, $thisForm.serialize())
          } else {
            groupManager.addAdmin(groupName, $thisForm.serialize())
          }
        }
      })

      $associatesContainers.on('hidden.bs.modal', function() {
        containerClose($(this))
      })

      $editMembersContainer.on('shown.bs.modal', function(event) {
        var $thisTarget = $(event.relatedTarget)
        var $thisContainer = $(this)

        containerOpen($thisContainer)

        var groupName = $thisTarget.data('group-name')
        var isAdmin = $thisTarget.data('group-admin')
        var $thisForm = $thisContainer.find('form')

        if (isAdmin === true) {
          $thisForm.removeClass(CSS_INVISIBLE)
        } else {
          $thisForm.addClass(CSS_INVISIBLE)
        }

        memberInput.url =
          cadc.web.gms.resource +
          '/group/' +
          groupName +
          '/members?media=text/csv'
        groupManager.getMembers(groupName, memberInput, memberViewOptions)
      })

      $editAdminsContainer.on('shown.bs.modal', function(event) {
        var $thisTarget = $(event.relatedTarget)
        var $thisContainer = $(this)

        containerOpen($thisContainer)

        var groupName = $thisTarget.data('group-name')
        var isAdmin = $thisTarget.data('group-admin')
        var $thisForm = $thisContainer.find('form')

        if (isAdmin === true) {
          $thisForm.removeClass(CSS_INVISIBLE)
        } else {
          $thisForm.addClass(CSS_INVISIBLE)
        }

        adminInput.url =
          cadc.web.gms.resource +
          '/group/' +
          groupName +
          '/admins?media=text/csv'
        groupManager.getAdmins(groupName, adminInput, adminViewOptions)
      })

      $editGroupContainer.on('hidden.bs.modal', function() {
        clearMessageContainer($(this).find('form'))
      })

      $editGroupContainer.on('show.bs.modal', function(event) {
        var $thisContainer = $(this)
        containerOpen($thisContainer)
        $thisContainer
          .find('.modal-title')
          .text($(event.relatedTarget).data('group-name'))
      })

      $editGroupContainer.on('shown.bs.modal', function(event) {
        var $thisTarget = $(event.relatedTarget)
        var $thisContainer = $(this)
        var $thisForm = $thisContainer.find('form')
        var isOwner = $thisTarget.data('group-owner')

        if (isOwner === true) {
          $thisForm.find('.action-button-group').removeClass(CSS_INVISIBLE)
          $thisForm
            .find("textarea[name='group-description']")
            .prop('disabled', false)
        } else {
          $thisForm.find('.action-button-group').addClass(CSS_INVISIBLE)
          $thisForm
            .find("textarea[name='group-description']")
            .prop('disabled', true)
        }

        groupManager.getGroup($thisTarget.data('group-name'))
      })

      $newGroupContainer.on('shown.bs.modal', function(event) {
        var $thisContainer = $(event.relatedTarget)
        $thisContainer.find(LOADER_CONTAINER_SELECTOR).hide()
        containerOpen($thisContainer)
        $thisContainer.find('ul.delete-button-group').addClass(CSS_INVISIBLE)
        $thisContainer
          .find('form')
          .find("input[name='group-name']")
          .prop('disabled', false)
      })

      /**
       * For group errors, report it back to the form.
       */
      groupManager.subscribe(cadc.web.gms.events.onGroupCreatedError, function(
        e,
        data
      ) {
        $newGroupContainer.find(LOADER_CONTAINER_SELECTOR).hide()
        $newGroupContainer
          .find('form')
          .find('.form-message')
          .text(data.message)
      })

      groupManager.subscribe(cadc.web.gms.events.onGroupDeleted, function() {
        $editGroupContainer.find(LOADER_CONTAINER_SELECTOR).hide()
        window.location.reload()
      })

      /**
       * For group errors, report it back to the form.
       */
      groupManager.subscribe(cadc.web.gms.events.onGroupCreated, function() {
        $newGroupContainer.find(LOADER_CONTAINER_SELECTOR).hide()
        window.location.reload()
      })

      groupManager.subscribe(cadc.web.gms.events.onGroupLoaded, function(
        e,
        data
      ) {
        $editGroupContainer.find(LOADER_CONTAINER_SELECTOR).hide()

        var $form = $editGroupContainer.find('form')

        var $groupNameInput = $form.find("input[name='group-name']")

        $groupNameInput.val(data.group.getName())

        var desc = data.group.getDescription()

        if (desc) {
          $form.find("textarea[name='group-description']").text(desc)
        }

        $editGroupContainer.find(LOADER_CONTAINER_SELECTOR).hide()
      })

      groupManager.subscribe(cadc.web.gms.events.onGroupsLoaded, function() {
        var $mainContainer = $('#content_column_main')

        $('#add_group_button').prop('disabled', false)

        $mainContainer.find(LOADER_CONTAINER_SELECTOR).hide()
        $mainContainer
          .find(GRID_OUTER_CONTAINER_SELECTOR)
          .removeClass(CSS_INVISIBLE)
      })

      /*
                       * On update, when the Group is loaded, populate some known fields.
                       */
      groupManager.subscribe(cadc.web.gms.events.onMembersLoaded, function(
        e,
        data
      ) {
        $editMembersContainer.find(LOADER_CONTAINER_SELECTOR).hide()
        $editMembersContainer
          .find('.content_column_main_inner')
          .removeClass(CSS_INVISIBLE)

        // Be careful not to do anything
        // with the
        // popup(before|after)position
        // events with this method here!
        // This is here because of the
        // dynamic content in the dialog
        // causes it to change size, so
        // we ask jQuery Mobile to
        // nicely reposition it once
        // more. jenkinsd 2014.09.12
        $editMembersContainer.modal('handleUpdate')
      })

      groupManager.subscribe(cadc.web.gms.events.onMemberAdded, function() {
        clearMessageContainer($editMembersContainer.find('form'))

        groupManager.getMembers(
          $editMembersContainer.data('group-name'),
          memberInput,
          memberViewOptions
        )
      })

      groupManager.subscribe(cadc.web.gms.events.onMemberDeleted, function() {
        $editMembersContainer.find(LOADER_CONTAINER_SELECTOR).hide()
        clearMessageContainer($editMembersContainer.find('form'))

        groupManager.getMembers(
          $editMembersContainer.data('group-name'),
          memberInput,
          memberViewOptions
        )
      })

      groupManager.subscribe(cadc.web.gms.events.onMemberDeletedError, function(
        e,
        data
      ) {
        $editMembersContainer.find(LOADER_CONTAINER_SELECTOR).hide()
        $editMembersContainer
          .find('form')
          .find('.form-message')
          .text(data.message)
      })

      groupManager.subscribe(cadc.web.gms.events.onAdminAdded, function() {
        clearMessageContainer($editAdminsContainer.find('form'))

        groupManager.getAdmins(
          $editAdminsContainer.data('group-name'),
          adminInput,
          adminViewOptions
        )
      })

      groupManager.subscribe(cadc.web.gms.events.onAdminDeletedError, function(
        e,
        data
      ) {
        $editMembersContainer.find(LOADER_CONTAINER_SELECTOR).hide()
        clearMessageContainer($editAdminsContainer.find('form'))

        groupManager.getAdmins(
          $editAdminsContainer.data('group-name'),
          adminInput,
          adminViewOptions
        )
      })

      groupManager.subscribe(cadc.web.gms.events.onAdminDeleted, function() {
        $editAdminsContainer.find(LOADER_CONTAINER_SELECTOR).hide()
        clearMessageContainer($editAdminsContainer.find('form'))

        groupManager.getAdmins(
          $editAdminsContainer.data('group-name'),
          adminInput,
          adminViewOptions
        )
      })

      groupManager.subscribe(cadc.web.gms.events.onMemberAddedError, function(
        e,
        data
      ) {
        $editMembersContainer.find(LOADER_CONTAINER_SELECTOR).hide()
        setAutocompleteMessageText(data.message)
      })

      groupManager.subscribe(cadc.web.gms.events.onAdminAddedError, function(
        e,
        data
      ) {
        $editAdminsContainer.find(LOADER_CONTAINER_SELECTOR).hide()
        setAutocompleteMessageText(data.message)
      })

      groupManager.subscribe(
        cadc.web.gms.events.onMembersLoadedError,
        function() {
          $editMembersContainer.find(LOADER_CONTAINER_SELECTOR).hide()
        }
      )

      groupManager.subscribe(
        cadc.web.gms.events.onAdminsLoadedError,
        function() {
          $editAdminsContainer.find(LOADER_CONTAINER_SELECTOR).hide()
        }
      )

      // On update, when the Group is loaded, populate some known fields.
      groupManager.subscribe(cadc.web.gms.events.onAdminsLoaded, function(
        e,
        data
      ) {
        $editAdminsContainer.find(LOADER_CONTAINER_SELECTOR).hide()
        $editAdminsContainer.find('.group-name-title').text(data.groupName)
        $editAdminsContainer
          .find('.content_column_main_inner')
          .removeClass(CSS_INVISIBLE)

        // Be careful not to do anything
        // with the
        // popup(before|after)position
        // events with this method here!
        // This is here because of the
        // dynamic content in the dialog
        // causes it to change size, so
        // we ask jQuery Mobile to
        // nicely reposition it once
        // more. jenkinsd 2014.09.12
        $editAdminsContainer.modal('handleUpdate')
      })

      groupManager.subscribe(cadc.web.gms.events.onGroupUpdatedError, function(
        e,
        data
      ) {
        alert(data.request.status)
      })
      groupManager.subscribe(cadc.web.gms.events.onGroupUpdated, function(
        e,
        data
      ) {
        $editGroupContainer.find(LOADER_CONTAINER_SELECTOR).hide()

        var $thisForm = $editGroupContainer.find('form')
        var $messageContainer = $thisForm.find('.form-message')

        $messageContainer.removeClass('color-attention')
        $messageContainer.addClass('color-accent')
        $messageContainer.text('Update complete.')
      })

      groupManager.getGroups(input, options)

      $('div.group-search input[type=text]').autocomplete({
        // Define the minimum search string length
        // before the suggested values are shown.
        minLength: 2,

        // Define callback to format results
        source: function(req, callback) {
          // Reset each time as they type.
          var suggestionKeys = []

          // Pass request to server
          $.getJSON(cadc.web.gms.resource + '/associations', {
            q: req.term
          }).done(function(data) {
            if (data.matches.length > 0) {
              if (data.remaining) {
                setAutocompleteMessageText(
                  data.remaining + ' more not shown here.'
                )
              } else {
                setAutocompleteMessageText('')
              }

              // Process
              // response
              $.each(data.matches, function(i, suggestionEntry) {
                var entryID = suggestionEntry.id
                var entryType = suggestionEntry.type
                var display

                if (entryType === 'GROUP') {
                  display = 'All members of ' + entryID
                } else {
                  display = entryID
                }

                suggestionKeys.push(display)
              })
            } else {

            	$('span.text-success').hide()
            	$('span.text-danger').show()
              setAutocompleteMessageText('No Group with that name.')
            }

            // Pass array to
            // callback
            callback(suggestionKeys)
          })
        },
       
        select: function(event, ui) {
          var val = ui.item.value
          
          setAutocompleteMessageText('')

          // This doesn't always get removed properly.
          clearAutompleteLoading($(ui.item))
        }
        
      })
    }

    $.extend(this, {
      loadGroups: loadGroups
    })
  }
})(jQuery, window)
