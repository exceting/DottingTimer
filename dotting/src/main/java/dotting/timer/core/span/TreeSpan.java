package dotting.timer.core.span;

//import com.google.common.base.Charsets;
//import com.google.common.hash.Hashing;

import io.opentracing.References;
import io.opentracing.Span;
import dotting.timer.core.builder.TreeReference;
import dotting.timer.core.tracer.TreeTracer;

import java.util.*;

/**
 * Create by 18073 on 2018/10/29.
 */
public class TreeSpan implements Span {

    private final TreeTracer treeTracer;
    private final long parentId;
    private final long startTime;
    private final Map<String, Object> tags;
    private final List<TreeReference> references;
    private dotting.timer.core.builder.TreeSpanContext context;
    private boolean finished;
    private long endTime;
    private boolean sampled;
    private boolean isError;
    private long expect;
    private boolean isAsync;
    private String moudle;
    private String title;

    public TreeSpan(TreeTracer treeTracer, String title, long startTime,
                    Map<String, Object> initialTags, List<TreeReference> refs,
                    boolean isAsync, long expect) {
        this.treeTracer = treeTracer;
        this.title = title;
        this.startTime = startTime;
        this.moudle = treeTracer.getMoudle();
        this.sampled = treeTracer.getSampled();
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
        dotting.timer.core.builder.TreeSpanContext parent = findPreferredParentRef(this.references);
        if (parent == null) {
            // 父节点
            this.context = new dotting.timer.core.builder.TreeSpanContext(makeId(), makeId());
            this.parentId = 0;
        } else {
            // 子节点
            this.context = new dotting.timer.core.builder.TreeSpanContext(parent.getTraceId(), makeId());
            this.parentId = parent.getSpanId();
        }
    }

    private static dotting.timer.core.builder.TreeSpanContext findPreferredParentRef(List<TreeReference> references) {
        if (references.isEmpty()) {
            return null;
        }
        for (dotting.timer.core.builder.TreeReference reference : references) {
            if (References.CHILD_OF.equals(reference.getTreeReferenceType())) {
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

    public dotting.timer.core.span.TreeSpan setMoudle(String moudle) {
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
    public dotting.timer.core.span.TreeSpan setOperationName(String title) {
        if (!finished) {
            this.title = title;
        }
        return this;
    }

    @Override
    public synchronized dotting.timer.core.builder.TreeSpanContext context() {
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
            this.treeTracer.appendFinishedSpan(this);
            this.finished = true;
        }
    }

    @Override
    public dotting.timer.core.span.TreeSpan setTag(String key, String value) {
        return addTag(key, value);
    }

    @Override
    public dotting.timer.core.span.TreeSpan setTag(String key, boolean value) {
        return addTag(key, value);
    }

    @Override
    public dotting.timer.core.span.TreeSpan setTag(String key, Number value) {
        return addTag(key, value);
    }

    private synchronized dotting.timer.core.span.TreeSpan addTag(String key, Object value) {
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
    public final synchronized dotting.timer.core.span.TreeSpan log(long ts, Map<String, ?> fields) {
        return this;
    }

    @Override
    public dotting.timer.core.span.TreeSpan log(String event) {
        return this.log(getCurrentTime(), event);
    }

    @Override
    public dotting.timer.core.span.TreeSpan log(long timestampMicroseconds, String event) {
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
