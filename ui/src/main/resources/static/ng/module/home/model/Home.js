define(function (require, exports, module) {
    var HttpUtil = require('util/HttpUtil');
    var Home = function () {

    };

    Home.prototype = {
        constructor: Home,

        fetch: function () {
            var model = this;

            return HttpUtil.request({
                url: STATEMENT.root + '',
                method: 'GET',
                data: this.params
            }).done(function (resp) {
                if (resp.code == 0) {
                    model.lastPage = resp.result;
                }
            });
        }

    };

    module.exports = Home;

});