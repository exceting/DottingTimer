define(function (require, exports, module) {
    var HttpUtil = require('util/HttpUtil');
    var Span = function () {
        this.id = null;
        this.tree_nodes = {};
    };

    Span.prototype = {
        constructor: Span,
        getSpanInfo: function () {
            var param = {traceId:this.id};
            return HttpUtil.request({
                url: STATEMENT.root + 'span/spans',
                method: 'GET',
                data: param
            });
        }
    };

    module.exports = Span;

})