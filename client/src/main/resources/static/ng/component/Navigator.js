define(function(require, exports, module) {

	var BackboneUtil = require('util/BackboneUtil');

	var Template = require('component/tpl/Navigator.tpl');

	var Navigator = Backbone.View.extend({
		template: Template.trim(),

		el: '.sidebar',
		$locationTip: $('#locationTip'),

		initialize: function() {
			this.listenTo(window.ROUTER, 'route', this.refresh);

			this.render();
		},

		render: function() {
			var $ul = $(this.template).filter('ul');
			this.$el.html($ul);
		},

		refresh: function(route) {
			this.reset();

			var hash = Backbone.history.getHash(),
				cleanFragment = hash.split('?')[0],
				params = BackboneUtil.queryStringToObject(hash.split('?')[1]);

			if (!hash) {
				this.renderDefault();
				return;
			}

			this.refreshLeftMenu(cleanFragment, params);
			this.refreshLocationTip(cleanFragment, params);
		},

		reset: function() {
			$('#main-nav li ul li a').removeClass('active');
			this.$locationTip.empty();
		},

		refreshLeftMenu: function(fragment, params) {
			var layers = fragment.split('/'),
				hash;
			switch (layers.length) {
				case 1:
					{ // fragement is ''
						hash = '#' + layers[0];
						break;
					}
				case 2:
					{
						if (['statement', 'season'].indexOf(layers[0]) > -1) {

							if (layers[0] == 'statement' && layers[1] == 'tool') {
								hash = '#' + fragment;
							} else {
								hash = '#statement/list';
							}

						} else {
							if (['wrapper', 'item', 'fallitem'].indexOf(layers[0]) > -1) {
								hash = '#wrapper/grid?regionId=' + params.regionId;
							} else {
								hash = '#' + fragment;
							}
						}
						break;
					}
				case 3:
					{ // fragement is like: 'statement/eidt/2772', 'reserve/position/grid', 'wrapper/grid/statement'
						switch (layers[0]) {
                            case 'music_list':
                            {
                                hash='#music_list/list';
                                break;
                            }
                            case 'music':
                            {
                                hash='#music/list';
                                break;
                            }
                            case 'video':
                            {
                                hash='#video/list';
                                break;
                            }
                            case 'video_add':
                            {
                                hash='#video_add/add';
                                break;
                            }
						}
						break;
					}
				default:
					{
						throw new Error('illegal hash for refreshNavigator');
					}
			}
			var $atag = this.$el.find('a[href=\"' + hash + '\"]');
			$atag.addClass('active');
			// 展开目录区域
			$atag.parent().parent().collapse('show');
		},

		refreshLocationTip: function(fragment, params) {
			var layers = fragment.split('/');
			var lis = [];
			switch (layers.length) {
				case 2:
					{
						switch (layers[0]) {
                            case 'music_list':
                            {
                                lis.push('<li class="active">网易乐单</li>');
                                break;
                            }
                            case 'music':
                            {
                                lis.push('<li class="active">音乐列表</li>');
                                break;
                            }
                            case 'video':
                            {
                                lis.push('<li class="active">视频列表</li>');
                                break;
                            }
                            case 'video_add':
                            {
                                lis.push('<li class="active">视频摄入</li>');
                                break;
                            }
						}
						break;
					}
				case 3:
					{
						switch (layers[0]) {

							/*case 'contract2':
							{
								lis.push('<li><a href="#contract2/list">合同管理</a></li>');
								lis.push('<li class="active">(合同ID) ' + layers[2] + '</li>');
								break;
							}*/

						}
						break;
					}
			}

			this.$locationTip.html(lis.join(''));
		},

		renderDefault: function() {
			this.$el.find('a[href="#music_list/list"]').addClass('active');

			this.$locationTip.html('<li class="active">诗音编辑</li>');
		}
	});

	module.exports = Navigator;
})