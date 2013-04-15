/**
 * Utility methods for dealing with AJAX.
 */
$(function() {
    "use strict";

    /**
     * Performs DOM replacements according to the given json.
     * Expects json like 
     * <code>
     * {
     * "htmlBySelector" : {
     *     "title": '<title>Some title</title>',
     *     "#someId": '<div id="someId">...</div>' 
     *   }
     * }
     * </code> 
     * The keys of htmlBySelector are jquery selectors. The values
     * are the replacement HTML.
     */
    function updateHtmlBySelector(json) {
        var selector;
        if (json.htmlBySelector != null) {
            for (selector in json.htmlBySelector) {
                var content = json.htmlBySelector[selector];
            	if(selector.indexOf(".text") !== -1) {
            		selector = selector.substr(0, selector.indexOf(".text"));
                    console.log('updating text', selector, content);
                    $(selector).text(content);
            	}
            	else {
                    console.log('updating html', selector, content);
                    $(selector).replaceWith(content);
            	}
                
            }
        }
    }

    window.ajaxUtils = {
    		updateHtmlBySelector: updateHtmlBySelector
    };
});