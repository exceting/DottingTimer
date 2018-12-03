define(function(require, exports, module) {

	var Template = require('component/tpl/Top.tpl');

	var CookieUtil = require('util/CookieUtil');

	var Top = Backbone.View.extend({
		el: '#top',
		template: Template,

		initialize: function(options) {

			this.$el.html(this.template);
			this.$el.find('[data-toggle="popover"]').popover();

			this.render();
		},

		events: {
			'click .j_sign-out': '_signOutClick'
		},

		render: function() {
			if(window.SHION != null && window.SHION != undefined){
                var user = window.SHION.currentUser;
                if(user != null && user != undefined){
                	this.$el.find('.login_area').hide();
                	var cu = this.$el.find('.current_user_area');
                	cu.show();
                	cu.html("<img src='"+user.avater+"' height='35px' width='35px'/>&nbsp;&nbsp;&nbsp;&nbsp;"+user.name);
				}
			}
		},

		_signOutClick: function() {
			if (confirm('是否退出诗音后台？')) {
                $.ajax({
                    url: "logout",
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        switch (data.code){
                            case 0 :
                                window.location.reload();
                                break;
                            default :
                                alert("迷之错误！");
                                break;
                        }
                    }
                });
			}
		},

		remove: function() {
			this.$el.find('[data-toggle="popover"]').popover('destroy');

			Backbone.View.prototype.remove.apply(this);
		}
	});

	module.exports = Top;
});
