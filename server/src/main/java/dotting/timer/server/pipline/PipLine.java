/**
 * sharemer.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package dotting.timer.server.pipline;

import com.alibaba.druid.pool.DruidDataSource;
import dotting.timer.server.po.CoreSpan;
import dotting.timer.server.serialize.KryoPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author sunqinwen
 * @version \: PipLine.java,v 0.1 2018-12-06 14:26
 */
public class PipLine {

    private Logger logger = LoggerFactory.getLogger(PipLine.class);

    private BlockingQueue<byte[]> queue;

    private DruidDataSource dataSource;

    public PipLine(DruidDataSource dataSource) {
        this.dataSource = dataSource;
        this.queue = new LinkedBlockingQueue<>();
        new Thread(this::pushTask).start();
    }

    public void push(byte[] span) {
        queue.offer(span);
    }

    private void pushTask() {
        if (queue != null) {
            byte[] spanBytes;
            while (true) {
                try {
                    spanBytes = queue.take();
                    CoreSpan span = KryoPool.deserialize(spanBytes);
                    if (span != null) {

                        String sql = String.format("INSERT INTO t_span_node(trace_id, span_id, parent_id, start, end, is_async, is_error, expect, moudle, title, tags, merge, avg_duration, min_duration, max_duration) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, '%s', '%s', '%s', %s, %s, %s, %s);",
                                span.getTraceId(),
                                span.getSpanId(),
                                span.getParentId(),
                                span.getStartTime(),
                                span.getEndTime(),
                                span.getIsAsync(),
                                span.getIsError(),
                                span.getExpect(),
                                span.getMoudle(),
                                span.getTitle(),
                                span.getTags(),
                                span.getCount(),
                                span.getAvg(),
                                span.getMinTime(),
                                span.getMaxTime());

                        Statement statement = null;
                        Connection connection = null;
                        try {
                            connection = dataSource.getConnection();
                            statement = connection.createStatement();
                            statement.executeUpdate(sql);
                        } catch (SQLException e) {
                            logger.error("dotting tracer connection pool write result error! sql={}", sql, e);
                        } finally {
                            if (statement != null) {
                                try {
                                    statement.close();
                                    connection.close();
                                } catch (SQLException e) {
                                    logger.error("dotting tracer connection pool close statement error! sql={}", sql, e);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("dotting tracer push task error!", e);
                }
            }
        }
    }

}
