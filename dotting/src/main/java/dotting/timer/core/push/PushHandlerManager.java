/**
 * sharemer.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package dotting.timer.core.push;

import dotting.timer.core.debug.push.mysql.MysqlPushHandler;
import dotting.timer.core.utils.PushUtils;

/**
 * @author sunqinwen
 * @version \: PushHandlerManager.java,v 0.1 2018-11-16 16:05
 */
public class PushHandlerManager {

    public static PushHandler getHandler(boolean isDebug, int dbType) {
        if (isDebug) {
            // 测试
            switch (dbType) {
                case PushUtils.DBTYPE_MYSQL:
                    return MysqlPushHandler.getHandler();
                case PushUtils.DBTYPE_ES:
                    return null;
                case PushUtils.DBTYPE_REDIS:
                    return null;
                case PushUtils.DBTYPE_MONGODB:
                    return null;
                case PushUtils.DBTYPE_HBASE:
                    return null;
            }
        } else {
            //TODO 正式环境
            switch (dbType) {
                case PushUtils.DBTYPE_MYSQL:
                    return null;
                case PushUtils.DBTYPE_ES:
                    return null;
                case PushUtils.DBTYPE_REDIS:
                    return null;
                case PushUtils.DBTYPE_MONGODB:
                    return null;
                case PushUtils.DBTYPE_HBASE:
                    return null;
            }
        }

        return null;
    }

}
