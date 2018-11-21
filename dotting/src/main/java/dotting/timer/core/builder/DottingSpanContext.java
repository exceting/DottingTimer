package dotting.timer.core.builder;

import io.opentracing.SpanContext;

import java.util.Map;

/**
 * Create by 18073 on 2018/10/29.
 */
public class DottingSpanContext implements SpanContext {

    private final long traceId;
    private final long spanId;

    public DottingSpanContext(long traceId, long spanId) {
        this.traceId = traceId;
        this.spanId = spanId;
    }

    public long getTraceId() {
        return traceId;
    }

    public long getSpanId() {
        return spanId;
    }

    @Override
    public Iterable<Map.Entry<String, String>> baggageItems() {
        return null;
    }
}
