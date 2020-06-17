define(function (require, exports, module) {

    seajs.use('./css/module/trace.css');

    var Template = require('module/trace/tpl/TraceView.tpl');
    var Pagination = require('component/Pagination');
    var Trace = require('module/trace/model/Trace');

    var TraceView = Backbone.View.extend({
        template: Template,

        $trTemplate: $($(Template).filter('#trTemplate').html().trim()),

        initialize: function () {
            this.model = new Trace();

            this.$el.append($(this.template).filter(':not(script)'));

            var view = this;

            this.pagination = new Pagination({
                el: '#pageNav',
                changed: function (newPageNo) {
                    view.request(newPageNo);
                }
            });

        },

        events: {
            'click .j_search': '_search'
        },

        _search: function () {
            this.request(1);
        },

        request: function (pageNo) {
            if (pageNo == undefined || pageNo == null) {
                pageNo = 1;
            }
            this.model.params.pageNo = pageNo;
            this.model.params.traceId = this.$el.find('.j_s_trace_id').val();
            this.$el.find('tbody').empty();
            var view = this;
            this.model.fetch().done(function (resp) {
                if (resp.code == 0) {
                    view.render(resp);
                }
            });
        },


        render: function () {
            this.$el.find('tbody').empty();
            var page = this.model.lastPage,
                traces = page.records;

            var trs = [];
            for (var i = 0; i < traces.length; i++) {
                trs.push(this._renderOne(traces[i]));
            }

            this.$el.find('tbody').append(trs);

            this.pagination.render(page.pageNo, page.pageCount, page.totalCount);
        },

        _renderOne: function (trace) {
            var $tr = this.$trTemplate.clone();
            $tr.find('.j_trace_id').text(trace.trace_id);
            $tr.find('.j_span_id').text(trace.span_id);
            $tr.find('.j_time').text(trace.duration);
            $tr.find('.j_album_title').text(trace.album_title);
            $tr.find('.j_songer').text(trace.songer);
            $tr.find('.j_is_ex').html(trace.is_error == 1 ? "<span class=\"label label-danger\">异常</span>" : "<span class=\"label label-success\">正常</span>");
            $tr.find('.j_is_async').text(trace.is_async == 1 ? "是" : "否");
            $tr.find('.j_expect').html(trace.expect < trace.duration ? "<span class=\"label label-warning\">超出预期的" + trace.expect + "ms</span>" : "<span class=\"label label-info\">符合预期的" + trace.expect + "ms</span>");
            $tr.find('.j_moudle').text(trace.moudle);
            $tr.find('.j_title').text(trace.title);
            $tr.find('.j_ctime').text(trace.ctime);
            $tr.find('.j_button').html("<a href='/#span/info/" + trace.trace_id + "' target='_blank'>详情</a>");
            return $tr;
        }
    });

    module.exports = TraceView;

});