define(function(require, exports, module) {

    var Top = require('component/Top');

    var ContractApp = Backbone.View.extend({

        main$el: $('.main'),
        pageEl: '#page',

        initialize: function(options) {
            this.top = new Top();
            this.lastPage = null;
            if(bootbox){
                bootbox.setDefaults({title:'请求结果'});
            }
        },

        renderHome: function (options) {
            var app = this;

            require.async('module/home/HomePage', function (HomePage) {
                if (!(app.lastPage instanceof HomePage)) {
                    app.reset();
                    app.lastPage = new HomePage({
                        el: app.pageEl
                    });
                }
                app.lastPage.go(options);
            });
        },

        renderTrace: function () {
            var app = this;

            require.async('module/trace/TracePage', function (TracePage) {
                if (!(app.lastPage instanceof TracePage)) {
                    app.reset();
                    app.lastPage = new TracePage({
                        el: app.pageEl
                    });
                }
                app.lastPage.go();
            });
        },

        renderSpanInfo: function (id) {
            var app = this;

            require.async('module/tree/SpanInfoPage', function (SpanInfoPage) {
                if (!(app.lastPage instanceof SpanInfoPage)) {
                    app.reset();
                    app.lastPage = new SpanInfoPage({
                        el: app.pageEl
                    });
                }
                app.lastPage.go(id);
            });
        },

        renderQuillTest: function () {
            var app = this;

            require.async('module/quill/QuillPage', function (QuillPage) {
                if (!(app.lastPage instanceof QuillPage)) {
                    app.reset();
                    app.lastPage = new QuillPage({
                        el: app.pageEl
                    });
                }
                app.lastPage.go();
            });
        },

        reset: function() {
            if (this.lastPage) {
                this.lastPage.remove();
                this.main$el.append('<div id="page" style="margin-top: 160px"/>');
            }
        }
    });

    module.exports = ContractApp;
});
