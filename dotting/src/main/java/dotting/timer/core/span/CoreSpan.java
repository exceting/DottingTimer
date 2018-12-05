package dotting.timer.core.span;

import java.io.Serializable;

/**
 * Create by 18073 on 2018/12/5.
 */
public class CoreSpan implements Serializable {

    private static final long serialVersionUID = 1L;

    private long traceId;
    private long spanId;
    private long parentId;
    private long startTime;
    private long endTime;
    private int isAsync;
    private int isError;
    private long expect;
    private String moudle;
    private String title;
    private String tags;
    private long count;
    private long avg;
    private long minTime;
    private long maxTime;

    public CoreSpan() {
    }

    public CoreSpan(DottingSpan span) {
        traceId = span.context().getTraceId();
        spanId = span.context().getSpanId();
        parentId = span.getParentId();
        startTime = span.getStartTime();
        endTime = span.getEndTime();
        isAsync = span.isAsync() ? 1 : 0;
        isError = span.isError() ? 1 : 0;
        expect = span.getExpect();
        moudle = span.getMoudle();
        title = span.getTitle();
        tags = span.getTags() != null ? span.getTags().toString() : "";
        count = span.count();
        avg = span.getAvg();
        minTime = span.getMinTime();
        maxTime = span.getMaxTime();
    }

    public long getTraceId() {
        return traceId;
    }

    public void setTraceId(long traceId) {
        this.traceId = traceId;
    }

    public long getSpanId() {
        return spanId;
    }

    public void setSpanId(long spanId) {
        this.spanId = spanId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getIsAsync() {
        return isAsync;
    }

    public void setIsAsync(int isAsync) {
        this.isAsync = isAsync;
    }

    public int getIsError() {
        return isError;
    }

    public void setIsError(int isError) {
        this.isError = isError;
    }

    public long getExpect() {
        return expect;
    }

    public void setExpect(long expect) {
        this.expect = expect;
    }

    public String getMoudle() {
        return moudle;
    }

    public void setMoudle(String moudle) {
        this.moudle = moudle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getAvg() {
        return avg;
    }

    public void setAvg(long avg) {
        this.avg = avg;
    }

    public long getMinTime() {
        return minTime;
    }

    public void setMinTime(long minTime) {
        this.minTime = minTime;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
