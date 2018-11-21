package dotting.timer.core.tracer;

import io.opentracing.*;
import io.opentracing.propagation.Format;
import dotting.timer.core.builder.TreeReference;
import dotting.timer.core.push.PushHandler;
import dotting.timer.core.span.TreeSpan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by 18073 on 2018/10/29.
 */
public class TreeTracer implements Tracer {

    private final List<TreeSpan> finishedSpans = new ArrayList<>();
    private String moudle;
    private boolean isDebug;
    private boolean sampled;
    private boolean includeAsync;


    public TreeTracer(boolean sampled, String moudle, boolean isDebug) {
        this.moudle = moudle;
        this.sampled = isDebug || sampled;
        this.isDebug = isDebug;
    }

    public boolean getSampled() {
        return sampled;
    }

    public synchronized void reset() {
        this.finishedSpans.clear();
    }

    public synchronized List<TreeSpan> finishedSpans() {
        return new ArrayList<>(this.finishedSpans);
    }

    public void onSpanFinished(dotting.timer.core.span.TreeSpan biliSpan) {
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

    public synchronized void appendFinishedSpan(dotting.timer.core.span.TreeSpan biliSpan) {
        this.finishedSpans.add(biliSpan);
        this.onSpanFinished(biliSpan);
    }

    public synchronized void pushSpans() {
        if (sampled) {
            List<TreeSpan> finished = this.finishedSpans;
            if (finished.size() > 0) {
                finished.stream().filter(dotting.timer.core.span.TreeSpan::getSampled).forEach(f -> {
                    System.out.println(f.toString());
                    PushHandler pushHandler = dotting.timer.core.push.PushHandlerManager.getHandler(isDebug, dotting.timer.core.utils.PushUtils.DBTYPE_MYSQL);
                    if(pushHandler != null){
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
        private List<TreeReference> references = new ArrayList<>();
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
                this.references.add(new dotting.timer.core.builder.TreeReference((dotting.timer.core.builder.TreeSpanContext) referencedContext, referenceType));
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
            return dotting.timer.core.tracer.TreeTracer.this.scopeManager().activate(this.startManual(),
                    finishOnClose);
        }

        @Override
        public dotting.timer.core.span.TreeSpan start() {
            return startManual();
        }

        @Override
        public dotting.timer.core.span.TreeSpan startManual() {
            if (this.startTime == 0) {
                this.startTime = dotting.timer.core.span.TreeSpan.getCurrentTime();
            }
            return new dotting.timer.core.span.TreeSpan(dotting.timer.core.tracer.TreeTracer.this, title, startTime,
                    initialTags, references, isAsync, expect);
        }
    }

}
