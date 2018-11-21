define(function(require, exports, module) {

	var CookieUtil = {
		deleteCookie: function(name, domain, path) {
			var value = this.getCookie(name);

			if (value) {
				var cookieString = name + '=' + value + 
					(domain ? '; domain=' + domain : '') +
					(path ? '; path=' + path : '; path=/') +
					'; expires=Thu, 01 Jan 1970 00:00:01 GMT';
					
				document.cookie = cookieString;
			}
		},

		getCookie: function(name) {
			if (!name) {
				return null;
			}

			var arr, 
				reg = new RegExp('(^| )' + name + '=([^;]*)(;|$)');

			if (arr = document.cookie.match(reg)) {
				return unescape(arr[2]);
			} else {
				return null;
			}
		}
	};

	module.exports = CookieUtil;
});