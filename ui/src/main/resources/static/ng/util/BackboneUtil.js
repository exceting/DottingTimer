define(function(require, exports, module) {

	var BackboneUtil = {
		/**
		 * [GENERATE_TAG description]
		 *
		 * @param  {[type]}  options  [description]
		 */
		generateTag: function(options) {
			var tagName = options.tagName || 'div',
				prefix = options.prefix || '',
				className = options.className || '',
				objects = options.objects;

			var arr = [],
				index = 0;

			return options.objects ? _byIds() : null;

			function _byIds() {
				var ids = _.pluck(objects, 'id');
				for (; index < ids.length; index++) {
					arr.push('<' + tagName + ' id=\"' + prefix + ids[index] + '\" ' + (className ? ('class=\"' + className + '\"') : '') + '/>');
				}
				return arr.join('');
			}
		},
		queryStringToObject: function(queryString) {
			var params = {};
			if (queryString) {
				_.each(
					_.map(decodeURI(queryString).split(/&/g), function(el, i) {
						var aux = el.split('='),
							o = {};
						if (aux.length >= 1) {
							var val = undefined;
							if (aux.length == 2)
								val = aux[1];
							o[aux[0]] = val;
						}
						return o;
					}),
					function(o) {
						_.extend(params, o);
					}
				);
			}
			return params;
		}
	};


	module.exports = BackboneUtil;
});