define(function (require, exports, module) {

    seajs.use('module/quill/core/quill.snow.css');
    seajs.use('module/quill/core/monokai-sublime.min.css');

    var mainTemp = require('module/quill/tpl/QuillView.tpl');

    require('module/quill/core/quill.js');
    require('module/quill/core/katex.min.js');
    require('module/quill/core/highlight.min.js');

    var QuillView = Backbone.View.extend({
        mainTpl: mainTemp,
        initialize: function () {
            this.$el.append(this.mainTpl);
        },

        events: {

        },

        request: function () {
            var quill = new Quill('#editor-container', {
                modules: {
                    formula: true,
                    syntax: true,
                    toolbar: '#toolbar-container'
                },
                placeholder: 'Compose an epic...',
                theme: 'snow'
            });
        }
    });

    module.exports = QuillView;

});