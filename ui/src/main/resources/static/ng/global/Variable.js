define(function(require, exports) {

	var ContractRouter = require('router/ContractRouter');
	var ContractApp = require('app/ContractApp');

	if (!window.STATEMENT) {
		STATEMENT = {};
	}

	STATEMENT.root = window.location.pathname; // ajax 请求的 Context Path

	STATEMENT.validation = {
		NUMBER_MAX_ID: 999999999
	};

	// 业务模块常量
	STATEMENT.modules = {
		reserve: {
			itemTypes: {
				"FALL": 0,
				"BANNER": 1,
				"STATIC": 2,
			}
		}
	};

	STATEMENT.imageTypes = ['image/jpg', 'image/jpeg', 'image/gif', 'image/png'];

	if (!window.$DOCUMENT) {
		$DOCUMENT = $(document);
	}

	if (!window.ROUTER) {
		ROUTER = new ContractRouter();
	}

	if (!window.APP) {
		APP = new ContractApp();
		ROUTER.setApp(APP);
	}

	if (!window.$PROCESSING_MODAL) {
		$PROCESSING_MODAL = $('#processingModal');
		$PROCESSING_MODAL.modal({
			backdrop: 'static',
			show: false
		});
		$PROCESSING_MODAL.on('hidden.bs.modal', function(event) {
			// 如果此时还有其他模态框是 shown 状态，需要为 body 添加 'modal-open' class
			if ($('.modal-backdrop').size() > 0) {
				$('body').toggleClass('modal-open', true);
			}
		});
	}
});
