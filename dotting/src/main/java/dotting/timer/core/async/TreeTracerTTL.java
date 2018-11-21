/**
 * Bilibili.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package dotting.timer.core.async;

import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.ExecutorService;

/**
 * @author sunqinwen
 * @version \: TTLProxy.java,v 0.1 2018-10-31 18:38
 */
public class TreeTracerTTL {

    public static ExecutorService transToTTL(ExecutorService executorService) {
        return TtlExecutors.getTtlExecutorService(executorService);
    }

}
