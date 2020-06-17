define(function (require, exports, module) {
    var PermUtil = {

        hideNoPermissionElements: function () {
            var permissions = [];
            if (STATEMENT.currentUser.groups) {
                _.each(STATEMENT.currentUser.groups, function (g) {
                    permissions = _.compact(_.union(permissions, g.permIds.split('-')));
                })

                if (!permissions.find("1")) {//超级管理员除外
                    $('.main [data-permission]').each(function (i, p) {
                        if (!permissions.find($(p).attr('data-permission'))) {
                            $(p).hide();
                        }
                    })
                }
            } else {
                $('.main [data-permission]').hide();
            }
        },

        /**
         * 返回单个权限判断结果
         * @param perm
         * @return {boolean}
         */
        isHavePerm: function (perm) {
            var permissions = [];
            if (STATEMENT.currentUser.groups) {
                _.each(STATEMENT.currentUser.groups, function (g) {
                    permissions = _.compact(_.union(permissions, g.permIds.split('-')));
                })

                if (!permissions.find("1")) {//超级管理员除外
                    if (permissions.find(perm)) {
                        return true;
                    }
                } else {
                    return true;
                }
                return false;
            } else {
                return false;
            }
        }
    };

    module.exports = PermUtil;
});