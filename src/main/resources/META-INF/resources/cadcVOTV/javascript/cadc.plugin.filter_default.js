/**
 * CADC VOTable viewer plugin to hook into the filter input boxes to suggest
 * data from the grid as the user types.
 *
 * @param _viewer     The cadc.vot.Viewer object containing data.
 *
 * jenkinsd 2014.12.01
 */
;(function($, undefined) {
  'use strict'
  // register namespace
  $.extend(true, $.fn, {
    cadcVOTV_filter_default: cadcVOTV_filter_default
  })

  /**
   * Default filter for the Grid results.
   *
   * @param _viewer       The VOTable viewer object.
   * @constructor
   */
  function cadcVOTV_filter_default(_viewer) {
    const $inputField = $(this)
    const columnID = $inputField.data('columnId')

    $inputField.on('input', function(e) {
      const trimmedVal = $.trim($inputField.val())

      if (!trimmedVal || trimmedVal === '') {
        _viewer.getColumnFilters()[columnID] = ''
      }

      _viewer.doFilter(trimmedVal || '', columnID)

      const grid = _viewer.getGrid()
      grid.invalidateAllRows()
      grid.resizeCanvas()
    })

    return this
  }
})(jQuery)
