package dotting.timer.core.tracer;

import dotting.timer.core.builder.DottingSpanContext;
import dotting.timer.core.push.PushHandler;
import dotting.timer.core.push.PushHandlerManager;
import dotting.timer.core.utils.PushUtils;
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

    private final List<DottingSpan> finishedSpans = new ArrayList<>();
    private String moudle;
    private boolean isDebug;
    private boolean sampled;
    private boolean includeAsync;
    private Long traceId;


    public DottingTracer(boolean sampled, String moudle, boolean isDebug) {
        this.moudle = moudle;
        this.sampled = isDebug || sampled;
        this.isDebug = isDebug;
    }

    public void setTraceId(Long traceId){
        this.traceId = traceId;
    }

    public Long getTraceId() {
        return traceId;
    }

    public boolean getSampled() {
        return sampled;
    }

    public synchronized void reset() {
        this.finishedSpans.clear();
    }

    public synchronized List<DottingSpan> finishedSpans() {
        return new ArrayList<>(this.finishedSpans);
    }

    public void onSpanFinished(DottingSpan biliSpan) {
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

    public synchronized void appendFinishedSpan(DottingSpan biliSpan) {
        this.finishedSpans.add(biliSpan);
        this.onSpanFinished(biliSpan);
    }

    public synchronized void pushSpans() {
        if (sampled) {
            List<DottingSpan> finished = this.finishedSpans;
            if (finished.size() > 0) {
                finished.stream().filter(DottingSpan::getSampled).forEach(f -> {
                    PushHandler pushHandler = PushHandlerManager.getHandler(isDebug, PushUtils.DBTYPE_MYSQL);
                    if (pushHandler != null) {
                        pushHandler.pushSpan(f);
                    }
                });
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
