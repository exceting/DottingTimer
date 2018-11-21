define(function (require, exports, module) {

    var SpanInfoView = require('module/span/view/SpanInfoView');

    var SpanInfoPage = Backbone.View.extend({
        initialize: function () {
            this.$el.html('<div id="listView"></div>');

            this.listView = new SpanInfoView({
                el: '#listView'
            });
        },

        go: function (id) {
            this.listView.request(id);
        },

        remove: function () {
            this.listView.remove();
            Backbone.View.prototype.remove.apply(this);
        }
    });

    module.exports = SpanInfoPage;
})