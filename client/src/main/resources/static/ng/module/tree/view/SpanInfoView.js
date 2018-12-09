define(function (require, exports, module) {

    seajs.use('./css/module/orgchart/tree/font-awesome.min.css');
    seajs.use('./css/module/orgchart/tree/jquery.orgchart.css');
    seajs.use('./css/module/orgchart/tree/style.css');
    seajs.use('./css/module/orgchart/tree/tree_style.css');
    seajs.use('./css/module/span_info.css');

    var mainTemp = require('module/tree/tpl/SpanInfoView.tpl');
    var modalTemp = require('module/tree/tpl/NodeModal.tpl');

    var Span = require('module/tree/model/Span');
    var SpanInfoView = Backbone.View.extend({
        mainTpl: mainTemp,
        modalTpl: modalTemp,

        initialize: function () {
            this.model = new Span();
            this.$el.append(this.mainTpl);
            this.$el.append(this.modalTpl);
        },

        events: {
            'click .node': '_nodeInfo'
        },

        _nodeInfo: function (event) {
            var $target = $(event.target);
            var className = $target.prop("className");
            if (!className.startsWith("edge")) {
                var node = JSON.parse($target.parent().find(".node_info").val());
                this.$el.find('#node_info').modal('show');
                this.$el.find('.modal-title').text(node.title);
                // 计算耗时比例
                var total = node.avg_duration + node.expect;
                var max_rate = 0;
                var min_rate = 0;
                if (node.merge > 1) {
                    total = total + node.max_duration + node.min_duration;
                    max_rate = (node.max_duration / total) * 100;
                    min_rate = (node.min_duration / total) * 100;
                }
                var avg_rate = (node.avg_duration / total) * 100;
                var expect_rate = (node.expect / total) * 100;

                var htm = "<table class=\"table table-striped\"><thead><tr><th style='width: 15rem'>属性</th><th style='width: 85rem'>值</th></tr></thead><tbody>";
                htm += "<tr><td>链路ID</td><td>TRACE_ID = " + node.trace_id + "; SPAN_ID = " + node.span_id + "</td></tr>";
                htm += "<tr><td>所属项目</td><td>" + node.moudle + "</td></tr>";
                htm += "<tr><td>方法全名</td><td>" + node.title + "</td></tr>";
                htm += "<tr><td>实际耗时</td><td>";
                htm += this.rateTimeHtm(avg_rate, node.avg_duration);
                htm += "</td></tr>";
                htm += "<tr><td>期望耗时</td><td>";
                htm += this.rateTimeHtm(expect_rate, node.expect);
                htm += "<tr><td>节点状态</td><td>";
                if (node.is_error) {
                    htm += "<span class=\"label label-danger\">异常</span>";
                } else {
                    if (node.avg_duration > node.expect) {
                        htm += "<span class=\"label label-warning\">超出预期</span>"
                    } else {
                        htm += "<span class=\"label label-success\">正常</span>"
                    }
                }
                htm += "</td></tr>";
                if (node.merge > 1) {
                    htm += "<tr><td>重复执行</td><td>" + node.merge + "次</td></tr>";
                    htm += "<tr><td>平均耗时</td><td>";
                    htm += this.rateTimeHtm(avg_rate, node.avg_duration);
                    htm += "</td></tr>";
                    htm += "<tr><td>最大耗时</td><td>";
                    htm += this.rateTimeHtm(max_rate, node.max_duration);
                    htm += "</td></tr>";
                    htm += "<tr><td>最小耗时</td><td>";
                    htm += this.rateTimeHtm(min_rate, node.min_duration);
                    htm += "</td></tr>";
                }
                htm += "<tr><td>其他信息</td><td style='word-wrap:break-word'>" + node.tags + "</td></tr>"
                htm += "</tbody></table>";
                this.$el.find('.modal-body').html(htm);
            }
        },

        rateTimeHtm: function (rate, time) {
            if (rate < 8) {
                return "<div class=\"progress\" style='font-size: 11px; color: #000; font-weight: bolder'>" + time + " ms<div class=\"progress-bar progress-bar-info\" role=\"progressbar\" style=\"width: " + rate + "%;\"></div></div>";
            } else {
                return "<div class=\"progress\"><div class=\"progress-bar progress-bar-info\" role=\"progressbar\" style=\"width: " + rate + "%; padding-left: 10px; text-align: left;font-weight: bolder\">" + time + "ms</div></div>";
            }
        },

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
                        if (slaveNum == null || slaveNum == 0) {
                            view.$el.find('.async_num').html("该主线程并没有产生任何子线程链路~");
                        } else {
                            view.$el.find('.async_num').html("该主线程共产生<span style='font-weight: bolder; color: #ff869a'>"+slaveNum+"</span>个子线程");
                        }

                        view.model.tree_nodes = {};//清理
                        for (var i = 0; i < slaveNum; i++) {
                            view.makeTree(view.model.tree_nodes, resp.data.slaveThread[i]);
                            view.$el.find('.slave').append("<div class='chart-container' id='slave_tree_" + i + "'><div class='slave-chart-badge'><span class='glyphicon glyphicon-leaf'></span>&nbsp;&nbsp;链路" + (i + 1) + "号子线程</div></div>");
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
                    "<input class='node_info' type='hidden' value='" + JSON.stringify(treeNode.node) + "'/>实际耗时：<span style='color: #e72f6a; font-weight: bold'>" + treeNode.node.avg_duration + "ms</span><br/>"
                    + "期望耗时：<span style='color: #00a5ff; font-weight: bold'>" + treeNode.node.expect + "ms</span><br/>";
                if (treeNode.node.merge > 1) {
                    tree_child.title += "重复执行：<span class=\"badge\" style=\"background-color:#ff68b5\">" + treeNode.node.merge + "次</span><br/>"
                        + "平均耗时：<span style='color: #b85c0d; font-weight: bold'>" + treeNode.node.avg_duration + "ms</span><br/>"
                        + "最大耗时：<span style='color: #bf0018; font-weight: bold'>" + treeNode.node.max_duration + "ms</span><br/>"
                        + "最小耗时：<span style='color: #5e9003; font-weight: bold'>" + treeNode.node.min_duration + "ms</span><br/>";
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