define(function (require, exports, module) {

    seajs.use('./css/module/orgchart/tree/font-awesome.min.css');
    seajs.use('./css/module/orgchart/tree/jquery.orgchart.css');
    seajs.use('./css/module/orgchart/tree/style.css');
    seajs.use('./css/module/orgchart/tree/tree_style.css');

    var Template = require('module/tree/tpl/SpanInfoView.tpl');

    var Span = require('module/tree/model/Span');
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
                        view.makeTree(view.model.tree_nodes, resp.data.masterThread);
                        view.$el.find(".master").append("<div class='chart-container' id='master_tree'><div class='master-chart-badge'><span class='glyphicon glyphicon-tree-conifer'></span>&nbsp;&nbsp;链路主线程</div></div>")
                        $('#master_tree').orgchart({
                            'data': view.model.tree_nodes,
                            'nodeContent': 'title'
                        });

                        var slaveNum = resp.data.slaveThread.length;
                        view.$el.find('.async_num').text(slaveNum)
                        view.model.tree_nodes = {};//清理
                        for (var i = 0; i < slaveNum; i++) {
                            view.makeTree(view.model.tree_nodes, resp.data.slaveThread[i]);
                            view.$el.find('.slave').append("<div class='chart-container' id='slave_tree_" + i + "'><div class='slave-chart-badge'><span class='glyphicon glyphicon-leaf'></span>&nbsp;&nbsp;链路"+(i+1)+"号子线程</div></div>");
                            $('#slave_tree_' + i).orgchart({
                                'data': view.model.tree_nodes,
                                'nodeContent': 'title'
                            });
                            view.model.tree_nodes = {};//清理
                        }
                    } else {
                        view.$el.find('.v_tag_name_text').text("未找到");
                    }
                } else {
                    view.$el.find('.v_tag_name_text').text("未知");
                }
            });
        },

        makeTree: function (tree_child, treeNode) {
            var view = this;
            if (treeNode != null && treeNode.node != null) {
                tree_child.name = treeNode.node.short_title;
                tree_child.title =
                 "实际耗时：<span style='color: #e72f6a; font-weight: bold'>" + treeNode.node.duration+"ms</span><br/>"
                +"期望耗时：<span style='color: #00a5ff; font-weight: bold'>"+treeNode.node.expect+"ms</span><br/>";
                if(treeNode.node.merge > 1){
                    tree_child.title+="重复执行：<span class=\"badge\" style=\"background-color:#ff68b5\">"+treeNode.node.merge+"次</span><br/>"
                    +"平均耗时：<span style='color: #b85c0d; font-weight: bold'>"+treeNode.node.avg_duration+"ms</span><br/>"
                        +"最大耗时：<span style='color: #bf0018; font-weight: bold'>"+treeNode.node.max_duration+"ms</span><br/>"
                        +"最小耗时：<span style='color: #5e9003; font-weight: bold'>"+treeNode.node.min_duration+"ms</span><br/>";
                }

                if (treeNode.node.is_error) {
                    tree_child.className = treeNode.node.merge > 1 ? "error-for-level" : "error-level";
                } else {
                    if (treeNode.node.avg_duration > treeNode.node.expect) {
                        tree_child.className = treeNode.node.merge > 1 ? "warn-for-level" : "warn-level";
                    } else {
                        tree_child.className = treeNode.node.merge > 1 ? "info-for-level" : "info-level";
                    }
                }
                if (treeNode.child != null && treeNode.child.length > 0) {
                    var children = [];
                    for (var i = 0; i < treeNode.child.length; i++) {
                        var child = {};
                        view.makeTree(child, treeNode.child[i]);
                        children.add(child);
                    }
                    tree_child.children = children;
                }
            }
        }
    });

    module.exports = SpanInfoView;

});