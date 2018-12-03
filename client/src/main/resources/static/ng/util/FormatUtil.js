define(function(require, exports, module) {


	var FormatUtil = {
		/**
		 * 解析日期字符串并格式化
		 *
		 * @param   {String}  dateTimeStr  待解析的日期时间字符串
		 * @param   {String}  formatStr    格式化字符串
		 *
		 * @return  {String}               格式化后的字符串
		 */
		parseAndFormatDate: function(dateTimeStr, formatStr) {
			if (!dateTimeStr) {
				return null;
			}

			var date = Date.create(dateTimeStr);

			return date.format(formatStr);
		},

		/**
		 * 格式化日期为 String 为 input datetime-local 组件使用
		 *
		 * @param   {String}          dateStr        [description]
		 *
		 * @return  {[type]}          [description]
		 * @deprecated @see FormatUtil.parseAndFormat
		 */
		formatForDateTimeLocal: function(dateStr) {

			if (!dateStr) {
				return null;
			}

			var date = Date.create(dateStr);

			return date.format('{yyyy}-{MM}-{dd}T{HH}:{mm}');
		},
		/**
		 * 格式化日期为 String 为 input date 组件使用
		 *
		 * @param   {String}          dateStr        [description]
		 *
		 * @return  {[type]}          [description]
		 */
		formatForDate: function(dateStr) {

			if (!dateStr) {
				return null;
			}

			var date = Date.create(dateStr);

			return date.format('{yyyy}-{MM}-{dd}');
		},

		/**
		 * 格式化日期
		 *
		 * @param   {Date}          date        [description]
		 *
		 * @return  {[type]}          [description]
		 */
		format2ISODate: function(date) {

			if (!date) {
				return null;
			}

			return date.format('{yyyy}-{MM}-{dd}');
		},

		/**
		 * 格式化日期为 String 为 input datetime-local 组件使用
		 *
		 * @param   {String}          dateStr        [description]
		 *
		 * @return  {[type]}          [description]
		 */
		formatDateWithSeconds: function(date) {

			if (!date) {
				return null;
			}

			return date.format('{yyyy}-{MM}-{dd} {HH}:{mm}:{ss}');
		},

		formatMonthDate: function(date){
			if (!date) {
				return null;
			}

			return date.format('{yyyy}-{MM}');
		}
	};

	module.exports = FormatUtil;
});