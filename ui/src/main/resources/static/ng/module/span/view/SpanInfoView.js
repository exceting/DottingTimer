define(function (require, exports, module) {

    seajs.use('./css/module/span_info.css');

    var Template = require('module/span/tpl/SpanInfoView.tpl');

    var Span = require('module/span/model/Span');
    var SpanInfoView = Backbone.View.extend({
        template: Template,

        initialize: function () {
            this.model = new Span();
            this.$el.append(this.template);
        },

        events: {},

        request: function (id) {
            if (id == null || id == undefined || id == "") {
                window.location.href = "/#";
                return;
            }
            this.model.id = id;
            var view = this;
            this.model.getSpanInfo().done(function (resp) {
                if (resp.code == 0) {
                    if (resp.data != null && resp.data != undefined) {
                        view.makeTree(resp.data);
                        view.$el.find('.tree_tracer').html(view.model.htm);
                    } else {
                        view.$el.find('.v_tag_name_text').text("未找到");
                    }
                } else {
                    view.$el.find('.v_tag_name_text').text("未知");
                }
            });
        },

        makeTree: function (treeNode) {
            var view = this;
            if(treeNode != null && treeNode.node != null){
                if(treeNode.node.parent_id === 0){
                    view.model.htm+="<ul>";
                }
                if(treeNode.node.is_error){
                    view.model.htm+="<li><a href='#' style='border: 1px #ff605a solid; background-color: #ffc7bb'>";
                }else{
                    if(treeNode.node.duration <= treeNode.node.expect){
                        view.model.htm+="<li><a href='#' style='border: 1px #00c100 solid; background-color: #bcffbc'>";
                    }else if(treeNode.node.duration > treeNode.node.expect){
                        view.model.htm+="<li><a href='#' style='border: 1px #ff9601 solid; background-color: #ffe76e'>";
                    }
                }
                view.model.htm+=treeNode.node.short_title+"(期望:<span style='color:#12c58c'>"+treeNode.node.expect+"ms</span>,耗时:<span style='color:#ff6fb7'>"+treeNode.node.duration+"ms</span>)</a>";
                if(treeNode.child != null && treeNode.child.length > 0){
                    for(var i = 0; i < treeNode.child.length; i++){
                        if(i === 0){
                            view.model.htm+="<ul>";
                        }
                        view.makeTree(treeNode.child[i]);
                        if((treeNode.child.length - 1) === i){
                            view.model.htm+="</ul>";
                        }
                    }
                }
                view.model.htm+="</li>";
                if(treeNode.node.parent_id === 0){
                    view.model.htm+="</ul>";
                }
            }
        }
    });

    module.exports = SpanInfoView;

});