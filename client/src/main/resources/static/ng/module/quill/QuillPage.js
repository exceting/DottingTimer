define(function (require, exports, module) {

    var QuillView = require('module/quill/view/QillView');

    var QuillPage = Backbone.View.extend({
        initialize: function () {
            this.$el.html('<div id="listView"></div>');

            this.listView = new QuillView({
                el: '#listView'
            });
        },

        go: function () {
            this.listView.request();
        },

        remove: function () {
            this.listView.remove();
            Backbone.View.prototype.remove.apply(this);
        }
    });

    module.exports = QuillPage;
})