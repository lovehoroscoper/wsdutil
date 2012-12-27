/**  * emptyValue 默认关键字效果  */
(function($) {
	$.fn.emptyValue = function(arg) {
		this.each(function() {
			var input = $(this);
			var options = arg;
			if (typeof options == "string") {
				options = {
					empty: options
				}
			}
			options = jQuery.extend({
				empty: input.attr("data-empty") || "",
				className: "gray"
			},
			options);
			return input.focus(function() {
				$(this).removeClass(options.className);
				if ($(this).val() == options.empty) {
					$(this).val("");
				}
			}).blur(function() {
				if ($(this).val() == "") {
					$(this).val(options.empty);
				}
				$(this).addClass(options.className);
			}).blur();
		});
	};
})(jQuery);