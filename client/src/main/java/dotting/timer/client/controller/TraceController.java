/**
 * sharemer.com Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package dotting.timer.client.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import dotting.timer.client.db.ConnectionPool;
import dotting.timer.client.po.Span;
import dotting.timer.client.resp.DataResult;
import dotting.timer.client.utils.Page;

import java.sql.SQLException;

/**
 * @author sunqinwen
 * @version \: TraceController.java,v 0.1 2018-11-19 13:51
 */
@RestController
@RequestMapping("/trace")
public class TraceController {

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public DataResult userWatchReport(@RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                      @RequestParam(value = "traceId", required = false, defaultValue = "-1") long traceId) throws SQLException {
        Page<Span> result = new Page<>(pageNo, pageSize);
        String sql = String.format("SELECT * FROM t_span_node WHERE parent_id = 0 AND is_async != 1 %s ORDER BY ctime DESC LIMIT %s, %s", traceId == -1 ? "" :
                String.format("AND trace_id = %s", traceId), (pageNo - 1) * pageSize, pageSize);
        String sqlCount = String.format("SELECT COUNT(*) AS count FROM t_span_node WHERE parent_id = 0 AND is_async != 1 %s", traceId == -1 ? "" :
                String.format("AND trace_id = %s", traceId));
        ConnectionPool.connectionPool.getPageResults(result, sql, sqlCount);
        return DataResult.success(result);
    }

}
