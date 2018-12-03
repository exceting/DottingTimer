define(function(require, exports, module) {

	var HttpUtil = {
		/**
		 * 统一的 Ajax 请求管理组件， 负责向服务端发起请求
		 * @param   {Object}  options  ajax 请求的选项
		 * @return  {$xhr}     jQuery ajax Request Object
		 */
		request: function(options) {
			var data = options.data,
				serialized;

			if (options.method && options.method.toUpperCase() == 'POST') {
				if (options.contentType && options.contentType.indexOf('urlencoded') >= 0) {
					serialized = $.param(data);
				} else {
					serialized = JSON.stringify(data);
				}
			}

			if (_.isUndefined(options.global)) {
				options.global = true;
			}

			return $.ajax({
				"url": options.url,
				"type": options.method,
				"contentType": options.contentType || "application/json; charset=UTF-8",
				"data": serialized || data,
				"dataType": "json",
				"global": options.global,
				"async": _.isUndefined(options.async) ? true : options.async
			}).always(function(resp, textStatus, $xhr) {
				if (!this.global) {
					return;
				}

				if(textStatus == 'error' && $xhr == ''){
					bootbox.alert('连接服务器失败！');
					return;
				}

				if(textStatus == 'error' && $xhr == 'Internal Server Error'){
					bootbox.alert('服务器内部错误！');
					return;
				}

				if(textStatus == 'error' && $xhr == 'Gateway Time-out'){
					bootbox.alert('请求超时！');
					return;
				}

				if (textStatus !== 'success') {
					console.log($xhr.responseJSON);
					bootbox.alert('请求异常！');
					return;
				}

				if (typeof resp.code != 'undefined' && resp.code != 0) {
					var hint = '请求失败（' + resp.code+"）：";
					(resp.message) && (hint += resp.message);
					bootbox.alert(hint);
					return;
				}

			});
		}
	};

	module.exports = HttpUtil;

});