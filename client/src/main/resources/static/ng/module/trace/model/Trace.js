define(function (require, exports, module) {
    var HttpUtil = require('util/HttpUtil');
    var Tracer = function () {
        this.params = {
            pageNo: 1,
            pageSize: 15,
            trace_id: null
        };

        this.lastPage = null;
    };

    Tracer.prototype = {
        constructor: Tracer,

        fetch: function () {
            var model = this;
            return HttpUtil.request({
                url: STATEMENT.root + 'trace/page',
                method: 'GET',
                data: this.params
            }).done(function (resp) {
                if (resp.code == 0) {
                    model.lastPage = resp.data;
                }
            });
        }

    };

    module.exports = Tracer;

});