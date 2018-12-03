/**
 * sharemer.com Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package dotting.timer.core.debug.push.mysql;

import dotting.timer.core.debug.db.mysql.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dotting.timer.core.push.PushHandler;
import dotting.timer.core.span.DottingSpan;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author sunqinwen
 * @version \: MysqlPushHandler.java,v 0.1 2018-11-16 15:55
 */
public class MysqlPushHandler implements PushHandler {

    private static Logger logger = LoggerFactory.getLogger(MysqlPushHandler.class);

    private BlockingQueue<DottingSpan> queue;

    private static final PushHandler handler = new MysqlPushHandler();

    private MysqlPushHandler() {
        this.queue = new LinkedBlockingQueue<>();
        new Thread(this::pushTask).start();
    }

    public static PushHandler getHandler() {
        return handler;
    }

    @Override
    public void pushSpan(DottingSpan span) {
        queue.offer(span);
    }

    private void pushTask() {
        if (queue != null) {
            DottingSpan span;
            while (true) {
                try {
                    span = queue.take();
                    if (ConnectionPool.connectionPool != null) {
                        String sql = String.format("INSERT INTO t_span_node(trace_id, span_id, parent_id, start, end, is_async, is_error, expect, moudle, title, tags, merge, avg_duration, min_duration, max_duration) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, '%s', '%s', '%s', %s, %s, %s, %s);",
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
                                span.getTags() != null ? span.getTags().toString() : "",
                                span.count(),
                                span.getAvg(),
                                span.getMinTime(),
                                span.getMaxTime());
                        ConnectionPool.connectionPool.writeResult(sql);
                    }
                } catch (Exception e) {
                    logger.error("dotting tracer push task error!", e);
                }
            }
        }
    }
}
