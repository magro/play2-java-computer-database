/*global Spinner */

window.coda =  window.coda || {};

window.coda.AjaxInput = (function () {
  "use strict";

  // TODO remove in production. Only necessary to prevent
  // Chrome from optimizing away uncalled functions and properties
  // from the execution context, making live editing and debugging
  // difficult
  eval();
  
  var liveSearch = function liveSearch(event) {
      var q = $("#searchbox").val();
      $.ajax({
        type: "GET",
        url: "http://localhost:9000/computers/search.json?p=0&f=" + q,
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