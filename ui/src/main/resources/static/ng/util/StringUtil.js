define(function(require, exports, module) {


	var StringUtil = {

		/**
		 * 判断是否是合法的数字
		 *
		 * @param   {String}   value  	 原始字符串
		 * @param 	{Boolean}  signed	 是否需要正负号
		 * @param   {Number}   max    	 最大值，如果不传则不校验范围
		 * @param   {Number}   min    	 最小值，如果不传则不校验范围
		 *
		 * @return  {Boolean}         是否通过校验
		 */
		isValidNumber: function(value, signed, floated, max, min) {
			if (!value) {
				return false;
			}

			var regexp;

			if (!signed) { // 无符号数
				if (!floated) { // 无浮点
					regexp = /^\d+$/;
				} else { // 有浮点
					regexp = /^\d+(\.\d+)?$/;
				}
			} else { // 有符号数
				if (!floated) { // 无浮点
					regexp = /^(\+|\-)?\d+$/;
				} else { // 有浮点
					regexp = /^(\+|\-)?\d+(\.\d+)?$/;
				}
			}

			if (!regexp.test(value)) {
				return false;
			}

			var num = Number(value);
			if (!_.isUndefined(max) && (num > max)) {
				return false;
			}

			if (!_.isUndefined(min) && (num < min)) {
				return false;
			}

			return true;
		},

		isValidId: function(value) {
			return this.isValidNumber(value, false, false, STATEMENT.validation.NUMBER_MAX_ID, 1);
		},
		/**
		 * 该字符串是否在首部或者尾部包含空白字符。
		 * whitespace characters (space, tab, no-break space, etc.) and all the line terminator characters (LF, CR, etc.).
		 *
		 * @param   {[type]}   value  [description]
		 *
		 * @return  {Boolean}         [description]
		 */
		isWhiteSpaceAheadOrTailed: function(value) {
			if (!value) {
				return false;
			}

			if (/^\s/.test(value) || /\s$/.test(value)) {
				return true;
			}
		},

		isWhiteSpaceOnly: function(value) {
			if (!value) {
				return false;
			}

			if (/^\s+$/.test(value)) {
				return true;
			}
		},

		isEmpty: function(value){
			if(!value){
				return true;
			}
			if (typeof (value) == 'string' && /^\s+$/.test(value)) {
				return true;
			}
		},

		isValidContractNumber: function(value){
			if(!value){
				return false;
			}
			if(/^\s*[a-zA-Z0-9\\-]+\s*$/.test(value)){
				return true;
			}
		},

		trim:function(object){
			var view = this;

			if(typeof(object) == 'string'){
				if(StringUtil.isWhiteSpaceAheadOrTailed(object)){
					return object.trim();
				}
			}

			if(typeof(object) == 'object'){
				for(var key in object){
					if(typeof(object[key]) == 'object'){
						view.trim(key);
					}
					if(StringUtil.isWhiteSpaceAheadOrTailed(object[key])){
						object[key] = object[key].trim();
					}
				}
			}
			return object;
		},
	};

	module.exports = StringUtil;
});