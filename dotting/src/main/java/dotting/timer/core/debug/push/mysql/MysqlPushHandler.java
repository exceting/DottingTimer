/**
 * Bilibili.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package dotting.timer.core.debug.push.mysql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dotting.timer.core.push.PushHandler;
import dotting.timer.core.span.TreeSpan;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author sunqinwen
 * @version \: MysqlPushHandler.java,v 0.1 2018-11-16 15:55
 */
public class MysqlPushHandler implements PushHandler {

    private static Logger logger = LoggerFactory.getLogger(dotting.timer.core.debug.push.mysql.MysqlPushHandler.class);

    private BlockingQueue<TreeSpan> queue;

    private static final PushHandler handler = new dotting.timer.core.debug.push.mysql.MysqlPushHandler();

    private MysqlPushHandler() {
        this.queue = new LinkedBlockingQueue<>();
        new Thread(this::pushTask).start();
    }

    public static PushHandler getHandler() {
        return handler;
    }

    @Override
    public void pushSpan(TreeSpan span) {
        queue.offer(span);
    }

    private void pushTask() {
        if (queue != null) {
            TreeSpan span;
            while (true) {
                try {
                    span = queue.take();
                    if (dotting.timer.core.debug.db.mysql.ConnectionPool.connectionPool != null) {
                        String sql = String.format("INSERT INTO t_span_node(trace_id, span_id, parent_id, start, end, is_async, is_error, expect, moudle, title, tags) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, '%s', '%s', '%s');",
                                span.context().getTraceId(),
                                span.context().getSpanId(),
                                span.getParentId(),
                                span.getStartTime(),
                                span.getEndTime(),
                                span.isAsync() ? 1 : 0,
                                span.isError() ? 1 : 0,
                                span.getExpect(),
                                span.getMoudle(),
                                span.getTitle(),
                                span.getTags() != null ? span.getTags().toString() : "");
                        dotting.timer.core.debug.db.mysql.ConnectionPool.connectionPool.writeResult(sql);
                    }
                } catch (Exception e) {
                    logger.error("tree tracer push task error!", e);
                }
            }
        }
    }
}
