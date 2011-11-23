(function($) {
    var accentMap = {
        "à" : "a",
        "â" : "a",
        "ä" : "a",
        "é" : "e",
        "è" : "e",
        "ê" : "e",
        "ë" : "e",
        "î" : "i",
        "ï" : "i",
        "ô" : "o",
        "ö" : "o",
        "ü" : "u"
    };
    var normalize = function(term) {
        var ret = "";
        for ( var i = 0; i < term.length; i++) {
            ret += accentMap[term.charAt(i)] || term.charAt(i);
        }
        return ret;
    };
    $.widget("ui.combobox", {
        _create : function() {
            var self = this;
            var select = this.element.hide();
            var selected = select.children(":selected");
            var noSelectionText = select.children("option")[0].value ? "" : select.children("option")[0].text;
            var value = selected.val() ? selected.text() : noSelectionText;       
            var input = this.input = $("<input>").insertAfter(select).val(value)
                    .autocomplete(
                            {
                                delay : 0,
                                minLength : 0,
                                source : function(request, response) {
                                    var matcher = new RegExp($.ui.autocomplete.escapeRegex(request.term), "i");
                                    response(select.children("option").map(
                                            function() {
                                                var text = $(this).text();
                                                var exactMatch = matcher.test(text);
                                                if (this.value
                                                        && (!request.term || exactMatch || matcher
                                                                .test(normalize(text))))
                                                    return {
                                                        label : (function() {
                                                            var regexp = new RegExp("(?![^&;]+;)(?!<[^<>]*)("
                                                                    + $.ui.autocomplete.escapeRegex(request.term)
                                                                    + ")(?![^<>]*>)(?![^&;]+;)", "gi");
                                                            var result;
                                                            if (exactMatch) {
                                                                result = regexp.exec(text);
                                                            } else {
                                                                result = regexp.exec(normalize(text));
                                                            }
                                                            return text.substring(0, result.index)
                                                                    + "<strong>"
                                                                    + text.substring(result.index, result.index
                                                                            + result[0].length) + "</strong>"
                                                                    + text.substring(result.index + result[0].length);
                                                        })(),
                                                        value : text,
                                                        option : this
                                                    };
                                            }));
                                },
                                select : function(event, ui) {
                                    ui.item.option.selected = true;
                                    self._trigger("selected", event, {
                                        item : ui.item.option
                                    });
                                },
                                change : function(event, ui) {
                                    if (!ui.item) {
                                        var matcher = new RegExp("^" + $.ui.autocomplete.escapeRegex($(this).val())
                                                + "$", "i"), valid = false;
                                        select.children("option").each(function() {
                                            if ($(this).text().match(matcher)) {
                                                this.selected = valid = true;
                                                return false;
                                            }
                                        });
                                        if (!valid) {
                                            // remove invalid value, as it
                                            // didn't match anything
                                            $(this).val("");
                                            select.val("");
                                            input.data("autocomplete").term = "";
                                            return false;
                                        }
                                    }
                                }
                            }).addClass("ui-widget ui-widget-content ui-corner-left");

            input.data("autocomplete")._renderItem = function(ul, item) {
                return $("<li></li>").data("item.autocomplete", item).append("<a style='font-weight: normal'>" + item.label + "</a>").appendTo(ul);
            };
            
            /*
            input.focus(function(){
                // Select input field contents
                this.select();
            });
            */

            this.button = $("<button type='button'>&nbsp;</button>").attr("tabIndex", -1).attr("title",
                    "Show All Items").insertAfter(input).button({
                icons : {
                    primary : "ui-icon-triangle-1-s"
                },
                text : false
            }).removeClass("ui-corner-all").addClass("ui-corner-right ui-button-icon").click(function() {
                // close if already visible
                if (input.autocomplete("widget").is(":visible")) {
                    input.autocomplete("close");
                    return;
                }

                // work around a bug (likely same cause as #5265)
                $(this).blur();

                // pass empty string as value to search for, displaying all
                // results
                input.autocomplete("search", "");
                input.focus();
            });
        },

        destroy : function() {
            this.input.remove();
            this.button.remove();
            this.element.show();
            $.Widget.prototype.destroy.call(this);
        }
    });
})(jQuery);