define(function (require, exports, module) {

    var BackboneUtil = require('util/BackboneUtil');

    var ContractRouter = Backbone.Router.extend({
        routes: {
            "trace": "trace",
            "span/info/(:id)": "spanInfo",
            "quill/test": "quillTest",
            "*path": "home"
        },

        setApp: function (app) {
            this.app = app;
        },

        home: function (queryString) {
            if (queryString) {
                var options = BackboneUtil.queryStringToObject(queryString);
            }
            this.app.renderHome(options);
        },

        trace: function () {
            this.app.renderTrace();
        },

        spanInfo: function (id) {
            this.app.renderSpanInfo(id);
        },

        quillTest: function () {
            this.app.renderQuillTest();
        },

        defaultRoute: function (args) {
            alert('路由路径不存在！');
        }
    });

    module.exports = ContractRouter;
})
