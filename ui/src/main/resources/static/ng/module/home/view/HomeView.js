define(function(require, exports, module) {

    //seajs.use('./css/module/homelist.css');

    var Template = require('module/home/tpl/HomeView.tpl');

    var Home = require('module/home/model/Home');

    var HomeView = Backbone.View.extend({
        template: Template,

        initialize: function() {
            this.model = new Home();
            this.$el.append(this.template);
        },

        events: {

        },

        request: function() {
        }
    });

    module.exports = HomeView;

});