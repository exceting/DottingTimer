define(function(require, exports) {
	if (window.jQuery) {
		/*jQuery.ajaxSetup({
			cache: false
		});*/

		$DOCUMENT.ajaxStart(function(event) {
			$PROCESSING_MODAL.modal('show');
		});

		$DOCUMENT.ajaxStop(function(event) {
			$PROCESSING_MODAL.modal('hide');
		});
	}

	$DOCUMENT.ready(function(readyEvent) {
		/*
		 * 增加额外的属性可以触发 backbone navigate 事件
		 */
		$DOCUMENT.on('click.Context', '.j_router', function(event) {
			var dom = event.currentTarget,
				hash = dom.getAttribute('href'),
				index = hash.indexOf('#'),
				fragment = hash.substr(index + 1);

			event.preventDefault();

			ROUTER.navigate(fragment, {
				trigger: true
			});
		});

		/*
		 * 消除 input[type=number] 上的滚轮事件 
		 * TODO 如果程序中没有 input[type=number] 可以删除
		 */
		$DOCUMENT.on('focus.Context', 'input[type=number]', function(focusEvent) {
			$(this).on('mousewheel.Context', function(wheelEvent) {
				wheelEvent.preventDefault();
			})
		});

		$DOCUMENT.on('blur.Context', 'input[type=number]', function(blurEvent) {
			$(this).off('mousewheel.Context');
		});

		/*
		 * 数字输入校验，如果输入其它字符则改变 form-group 的样式
		 */
		$DOCUMENT.on('input.Context', '.form-group input[data-number-only]', function(inputEvent) {
			var input = inputEvent.currentTarget,
				$input = $(input),
				value = input.value;

			$input.parents('.form-group').toggleClass('has-error', !/^\d*$/.test(value));
		});
	});

	if (xiuxiu) {
		xiuxiu.onBeforeUpload = function(data, id) {
			var size = data.size;
			if (size > 1 * 1024 * 1024) {
				bootbox.alert('图片不能超过1M');
				return false;
			}

			var tip = '图片尺寸不符，请使用左侧“裁剪”功能\n\n';

			var expectedWidth = xiuxiu.flashvars[id].maxFinalWidth,
				expectedHeight = xiuxiu.flashvars[id].maxFinalHeight,
				realWidth = data.width,
				realHeight = data.height;

			if (expectedWidth) {
				if (Math.abs(realWidth - expectedWidth) >= 10) {
					tip += '宽度实际值: ' + realWidth + ', 期望值为: ' + expectedWidth;
					bootbox.alert(tip);
					return false;
				}
			}

			if (expectedHeight) {
				if (Math.abs(realHeight - expectedHeight) >= 10) {
					tip += '宽度实际值: ' + realHeight + ', 期望值为: ' + expectedHeight;
					bootbox.alert(tip);
					return false;
				}
			}

			if (expectedWidth && expectedHeight) {
				if (Math.abs(realWidth / realHeight - expectedWidth / expectedHeight) >= 0.1) {
					tip += '长宽比并不正确';
					bootbox.alert(tip);
					return false;
				}
			}

			return true;
		};
	}

});