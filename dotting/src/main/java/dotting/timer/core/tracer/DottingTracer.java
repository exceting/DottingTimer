package dotting.timer.core.tracer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dotting.timer.core.builder.DottingSpanContext;
import dotting.timer.core.push.Pusher;
import dotting.timer.core.serialize.ObjTransfer;
import io.opentracing.*;
import io.opentracing.propagation.Format;
import dotting.timer.core.builder.DottingReference;
import dotting.timer.core.span.DottingSpan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by 18073 on 2018/10/29.
 */
public class DottingTracer implements Tracer {

    private final Map<String, List<DottingSpan>> finishedSpans = Maps.newHashMap();
    private String moudle;
    private boolean sampled;
    private boolean includeAsync;
    private Long traceId;


    public DottingTracer(boolean sampled, String moudle, boolean isDebug) {
        this.moudle = moudle;
        this.sampled = isDebug || sampled;
    }

    public void setTraceId(Long traceId) {
        this.traceId = traceId;
    }

    public Long getTraceId() {
        return traceId;
    }

    public boolean getSampled() {
        return sampled;
    }

    public synchronized void reset() {
        this.finishedSpans.put(Thread.currentThread().getName(), null);
    }

    public void setIncludeAsync(boolean includeAsync) {
        this.includeAsync = includeAsync;
    }

    public boolean isIncludeAsync() {
        return includeAsync;
    }

    public String getMoudle() {
        return moudle;
    }

    @Override
    public ScopeManager scopeManager() {
        return null;
    }

    @Override
    public SpanBuilder buildSpan(String operationName) {
        return new SpanBuilder(operationName);
    }

    @Override
    public <C> void inject(SpanContext spanContext, Format<C> format, C carrier) {
    }

    @Override
    public <C> SpanContext extract(Format<C> format, C carrier) {
        return null;
    }

    @Override
    public Span activeSpan() {
        return null;
    }

    public synchronized void appendFinishedSpan(DottingSpan dottingSpan) {
        String key = Thread.currentThread().getName();
        List<DottingSpan> spans = this.finishedSpans.get(key);
        if (spans == null) {
            finishedSpans.put(key, Lists.newArrayList());
        }
        this.finishedSpans.get(key).add(dottingSpan);
    }

    public synchronized void pushSpans() {
        if (sampled) {
            List<DottingSpan> finished = this.finishedSpans.get(Thread.currentThread().getName());
            if (finished != null && finished.size() > 0) {
                finished.stream().filter(DottingSpan::getSampled)
                        .map(ObjTransfer::spanTransfer)
                        .forEach(f -> Pusher.getReceiver().pushSpan(f));
                this.reset();
            }
        }
    }

    public final class SpanBuilder implements Tracer.SpanBuilder {
        private final String title;
        private long startTime;
        private List<DottingReference> references = new ArrayList<>();
        private Map<String, Object> initialTags = new HashMap<>();
        private long expect;
        private boolean isAsync;

        SpanBuilder(String title) {
            this.title = title;
        }

        public SpanBuilder setExpect(long expect) {
            this.expect = expect;
            return this;
        }

        public SpanBuilder setIsAsync(boolean isAsync) {
            this.isAsync = isAsync;
            return this;
        }

        @Override
        public SpanBuilder asChildOf(SpanContext parent) {
            return addReference(References.CHILD_OF, parent);
        }

        @Override
        public SpanBuilder asChildOf(Span parent) {
            if (parent == null) {
                return this;
            }
            return addReference(References.CHILD_OF, parent.context());
        }

        @Override
        public SpanBuilder ignoreActiveSpan() {
            return this;
        }

        @Override
        public SpanBuilder addReference(String referenceType, SpanContext referencedContext) {
            if (referencedContext != null) {
                this.references.add(new DottingReference((DottingSpanContext) referencedContext, referenceType));
            }
            return this;
        }

        @Override
        public SpanBuilder withTag(String key, String value) {
            this.initialTags.put(key, value);
            return this;
        }

        @Override
        public SpanBuilder withTag(String key, boolean value) {
            this.initialTags.put(key, value);
            return this;
        }

        @Override
        public SpanBuilder withTag(String key, Number value) {
            this.initialTags.put(key, value);
            return this;
        }

        @Override
        public SpanBuilder withStartTimestamp(long startTime) {
            this.startTime = startTime;
            return this;
        }

        @Override
        public Scope startActive(boolean finishOnClose) {
            return DottingTracer.this.scopeManager().activate(this.startManual(),
                    finishOnClose);
        }

        @Override
        public DottingSpan start() {
            return startManual();
        }

        @Override
        public DottingSpan startManual() {
            if (this.startTime == 0) {
                this.startTime = DottingSpan.getCurrentTime();
            }
            return new DottingSpan(DottingTracer.this, title, startTime,
                    initialTags, references, isAsync, expect);
        }
    }

}
