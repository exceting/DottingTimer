/**
 * sharemer.com Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package dotting.timer.client.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dotting.timer.client.db.ConnectionPool;
import dotting.timer.client.po.Span;
import dotting.timer.client.po.SpanTree;
import dotting.timer.client.resp.DataResult;
import dotting.timer.client.vo.SpanTrees;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author sunqinwen
 * @version \: SpanController.java,v 0.1 2018-11-16 19:04
 */
@RestController
@RequestMapping("/span")
public class SpanController {

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public DataResult userWatchReport() {
        return DataResult.success("rua");
    }

    @RequestMapping(value = "/spans", method = RequestMethod.GET)
    public DataResult spans(@RequestParam(value = "traceId") long traceId) {

        String sql = String.format("SELECT * FROM t_span_node WHERE trace_id = %s ORDER BY start ASC", traceId);
        List<Span> result = ConnectionPool.connectionPool.getResults(sql);
        Map<String, List<Span>> treeMap = Maps.newTreeMap();
        SpanTree masterThread = new SpanTree();
        List<SpanTree> slaveThread = Lists.newArrayList();
        List<Span> slaveSpan = Lists.newArrayList();
        if (result != null) {
            result.forEach(r -> {
                if ("0".equals(r.getParent_id())) {
                    if (r.getIs_async() == 0) {
                        masterThread.setNode(r);
                    } else {
                        slaveSpan.add(r);
                    }
                }
                treeMap.computeIfAbsent(r.getParent_id(), k -> Lists.newArrayList());
                treeMap.get(r.getParent_id()).add(r);
            });
            // 主线程链路
            makeTree(masterThread, treeMap);
            // 子线程链路
            slaveSpan.forEach(s -> {
                SpanTree slave = new SpanTree();
                slave.setNode(s);
                makeTree(slave, treeMap);
                slaveThread.add(slave);
            });
        }
        return DataResult.success(SpanTrees.build(masterThread, slaveThread));
    }

    private void makeTree(SpanTree currentSpan, Map<String, List<Span>> treeMap) {
        List<Span> nowChild = treeMap.get(currentSpan.getNode().getSpan_id());
        if (nowChild != null && nowChild.size() > 0) {
            nowChild.forEach(nc -> makeTree(currentSpan.setChild(nc), treeMap));
        }
    }

}
