define(function(require, exports, module) {

	seajs.use('./css/lib/bootstrap-datetimepicker/4.17.37/bootstrap-datetimepicker.css');

	require('lib/plugin/moment/2.11.1/moment-with-locales');
	require('lib/plugin/bootstrap-datetimepicker/4.17.37/bootstrap-datetimepicker');

	var DateTimePicker = {

		/**
		 * 绑定 DateTimePicker Listener 至该 DOM
		 *
		 * @param   {jQuery Object}  $el  目标 input 组件
		 * @param   {Boolean}       是否监听 reset 事件
		 * @param   {Boolean}       是否监听 calendar 事件
		 *
		 */
		wrap: function($el, options) {
			if (!options) {
				$el.datetimepicker({
					format: 'YYYY-MM-DD HH:mm',
					locale: 'zh-cn',
					useCurrent: false,
					showClear: false,
					ignoreReadonly: true,
					widgetPositioning: {
						horizontal: 'auto',
						vertical: 'bottom'
					},
					toolbarPlacement: 'bottom'
				});
				return $el;
			}

			$el.datetimepicker({
				format: options.format || 'YYYY-MM-DD HH:mm',
				locale: 'zh-cn',
				useCurrent: false,
				showClear: Boolean(options.nullable),
				ignoreReadonly: true,
				widgetPositioning: {
					horizontal: 'auto',
					vertical: options.vertical || 'bottom'
				},
				toolbarPlacement: 'bottom'
			});

			if (_.isFunction(options.changed)) {
				$el.on('dp.change', options.changed);
			}

			return $el;
		},

		destroy: function($el) {
			$el.each(function() {
				var $this = $(this);
				if ($this.data('DateTimePicker')) {
					$this.data('DateTimePicker').destroy();
				}
			});
		}
	};

	module.exports = DateTimePicker;
});