/**
 * Bilibili.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package dotting.timer.core.serialize;

import dotting.timer.core.span.CoreSpan;
import dotting.timer.core.span.DottingSpan;

/**
 * @author sunqinwen
 * @version \: ObjTransfer.java,v 0.1 2018-12-06 19:04
 */
public class ObjTransfer {
    public static CoreSpan spanTransfer(DottingSpan dottingSpan) {
        CoreSpan span = new CoreSpan();
        span.setTraceId(dottingSpan.context().getTraceId());
        span.setSpanId(dottingSpan.context().getSpanId());
        span.setParentId(dottingSpan.getParentId());
        span.setStartTime(dottingSpan.getStartTime());
        span.setEndTime(dottingSpan.getEndTime());
        span.setIsAsync(dottingSpan.isAsync() ? 1 : 0);
        span.setIsError(dottingSpan.isError() ? 1 : 0);
        span.setExpect(dottingSpan.getExpect());
        span.setMoudle(dottingSpan.getMoudle());
        span.setTitle(dottingSpan.getTitle());
        span.setTags(dottingSpan.getTags() == null ? "" : dottingSpan.getTags().toString());
        span.setCount(dottingSpan.count());
        span.setAvg(dottingSpan.getAvg());
        span.setMinTime(dottingSpan.getMinTime());
        span.setMaxTime(dottingSpan.getMaxTime());
        return span;
    }
}
