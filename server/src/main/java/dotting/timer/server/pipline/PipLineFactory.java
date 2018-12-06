/**
 * Bilibili.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package dotting.timer.server.pipline;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Map;

/**
 * @author sunqinwen
 * @version \: PipLineFactory.java,v 0.1 2018-12-06 16:05
 */
public class PipLineFactory {

    private Map<Integer, PipLine> pipLines = Maps.newHashMap();

    private int size;

    public PipLineFactory(int pipLineSize, DruidDataSource dataSource) {
        size = pipLineSize;
        for (int i = 0; i < size; i++) {
            pipLines.put(i, new PipLine(dataSource));
        }
    }

    public PipLine getPipLine(byte[] data) {
        return pipLines.get(Math.abs(Arrays.hashCode(data)) % size);
    }
}
