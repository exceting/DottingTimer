package dotting.timer.core.span;

//import com.google.common.base.Charsets;
//import com.google.common.hash.Hashing;

import dotting.timer.core.builder.DottingSpanContext;
import io.opentracing.References;
import io.opentracing.Span;
import dotting.timer.core.builder.DottingReference;
import dotting.timer.core.tracer.DottingTracer;

import java.util.*;

/**
 * Create by 18073 on 2018/10/29.
 */
public class DottingSpan implements Span {

    private final DottingTracer dottingTracer;
    private final long parentId;
    private final long startTime;
    private final Map<String, Object> tags;
    private final List<DottingReference> references;
    private DottingSpanContext context;
    private boolean finished;
    private long endTime;
    private boolean sampled;
    private boolean isError;
    private long expect;
    private boolean isAsync;
    private String moudle;
    private String title;

    public DottingSpan(DottingTracer dottingTracer, String title, long startTime,
                       Map<String, Object> initialTags, List<DottingReference> refs,
                       boolean isAsync, long expect) {
        this.dottingTracer = dottingTracer;
        this.title = title;
        this.startTime = startTime;
        this.moudle = dottingTracer.getMoudle();
        this.sampled = dottingTracer.getSampled();
        this.isAsync = isAsync;
        this.expect = expect;
        if (initialTags == null) {
            this.tags = new HashMap<>();
        } else {
            this.tags = new HashMap<>(initialTags);
        }
        if (refs == null) {
            this.references = Collections.emptyList();
        } else {
            this.references = new ArrayList<>(refs);
        }
        DottingSpanContext parent = findPreferredParentRef(this.references);
        if (parent == null) {
            // 父节点
            Long traceId = dottingTracer.getTraceId();
            if(traceId == null){
                dottingTracer.setTraceId(makeId());
            }
            this.context = new DottingSpanContext(dottingTracer.getTraceId(), makeId());
            this.parentId = 0;
        } else {
            // 子节点
            this.context = new DottingSpanContext(parent.getTraceId(), makeId());
            this.parentId = parent.getSpanId();
        }
    }

    private static DottingSpanContext findPreferredParentRef(List<DottingReference> references) {
        if (references.isEmpty()) {
            return null;
        }
        for (DottingReference reference : references) {
            if (References.CHILD_OF.equals(reference.getDottingReferenceType())) {
                return reference.getContext();
            }
        }
        return references.get(0).getContext();
    }

    public Map<String, Object> getTags() {
        return tags;
    }

    public void setSampled(boolean sampled) {
        this.sampled = sampled;
    }

    public void setAsync(boolean async) {
        isAsync = async;
    }

    public boolean isAsync() {
        return isAsync;
    }

    public DottingSpan setMoudle(String moudle) {
        this.moudle = moudle;
        return this;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public boolean isError() {
        return isError;
    }

    public void setExpect(long expect) {
        this.expect = expect;
    }

    public long getExpect() {
        return expect;
    }

    public String getMoudle() {
        return moudle;
    }

    public long getParentId() {
        return parentId;
    }

    public long getStartTime() {
        return startTime;
    }

    public boolean getSampled() {
        return sampled;
    }

    public String getTitle() {
        return this.title;
    }

    static long makeId() {
        return UUID.randomUUID().hashCode();
       /* Long id = Hashing.farmHashFingerprint64().hashString(UUID.randomUUID().toString(), Charsets.UTF_8).asLong();
        return id < 0 ? id * -1 : id;*/
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis() * 1000000;
    }

    public long getEndTime() {
        if (!finished) {
            return 0L;
        }
        return endTime;
    }

    @Override
    public DottingSpan setOperationName(String title) {
        if (!finished) {
            this.title = title;
        }
        return this;
    }

    @Override
    public synchronized DottingSpanContext context() {
        return this.context;
    }

    @Override
    public void finish() {
        this.finish(getCurrentTime());
    }

    @Override
    public synchronized void finish(long endTime) {
        if (!finished) {
            this.endTime = endTime;
            this.dottingTracer.appendFinishedSpan(this);
            this.finished = true;
        }
    }

    @Override
    public DottingSpan setTag(String key, String value) {
        return addTag(key, value);
    }

    @Override
    public DottingSpan setTag(String key, boolean value) {
        return addTag(key, value);
    }

    @Override
    public DottingSpan setTag(String key, Number value) {
        return addTag(key, value);
    }

    private synchronized DottingSpan addTag(String key, Object value) {
        if (!finished) {
            tags.put(key, value);
        }
        return this;
    }

    @Override
    public final Span log(Map<String, ?> fields) {
        return log(getCurrentTime(), fields);
    }

    @Override
    public final synchronized DottingSpan log(long ts, Map<String, ?> fields) {
        return this;
    }

    @Override
    public DottingSpan log(String event) {
        return this.log(getCurrentTime(), event);
    }

    @Override
    public DottingSpan log(long timestampMicroseconds, String event) {
        return this.log(timestampMicroseconds, Collections.singletonMap("event", event));
    }

    @Override
    public synchronized Span setBaggageItem(String key, String value) {
        return this;
    }

    @Override
    public synchronized String getBaggageItem(String key) {
        return null;
    }

    @Override
    public String toString() {
        return "{" +
                "traceId:" + context.getTraceId() +
                ", spanId:" + context.getSpanId() +
                ", parentId:" + parentId +
                ", title:\"" + title + "\"}";
    }
}
