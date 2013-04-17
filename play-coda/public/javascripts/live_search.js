window.coda =  window.coda || {};

window.coda.AjaxInput = (function () {
  "use strict";
  
  var liveSearch = function liveSearch(event) {
      var q = $("#searchbox").val();
      var sortBy = $(".computers").attr("data-sort-by");
      var sortOrder = $(".computers").attr("data-sort-order");
      jsRoutes.controllers.Application.liveSearch(null, sortBy, sortOrder, q).ajax({
        success: function(data, textStatus, jqXHR) {
        	$(document).off('keyup', '#searchbox');
        	window.ajaxUtils.updateHtmlBySelector(data);
            $(document).on('keyup', '#searchbox', liveSearch);
        },
        dataType: "json"
      });
  };

  var init = function init() {
  	  $("#searchbox").keyup(liveSearch);
  };

  return {
    init: init
  };
})();