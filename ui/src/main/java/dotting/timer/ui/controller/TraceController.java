/**
 * Bilibili.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package dotting.timer.ui.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import dotting.timer.ui.db.ConnectionPool;
import dotting.timer.ui.po.Span;
import dotting.timer.ui.resp.DataResult;
import dotting.timer.ui.utils.Page;

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
        String sql = String.format("SELECT * FROM t_span_node WHERE parent_id = 0 %s ORDER BY ctime DESC LIMIT %s, %s", traceId == -1 ? "" :
                String.format("AND trace_id = %s", traceId), (pageNo - 1) * pageSize, pageSize);
        String sqlCount = String.format("SELECT COUNT(*) AS count FROM t_span_node WHERE parent_id = 0 %s", traceId == -1 ? "" :
                String.format("AND trace_id = %s", traceId));
        ConnectionPool.connectionPool.getPageResults(result, sql, sqlCount);
        return DataResult.success(result);
    }

}
