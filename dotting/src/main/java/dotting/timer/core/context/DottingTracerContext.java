package dotting.timer.core.context;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dotting.timer.core.span.DottingSpan;
import dotting.timer.core.tracer.DottingTracer;

import java.util.LinkedList;
import java.util.Map;

/**
 * @author sunqinwen
 * @version \: DottingTracerContext.java,v 0.1 2018-10-30 10:21
 */
public class DottingTracerContext {

    private DottingTracer tracer;

    private Thread mainThread;

    private String family;

    private Map<Thread, LinkedList<DottingSpan>> spans = Maps.newConcurrentMap();

    private Map<String, DottingSpan> merge = Maps.newHashMap();

    private DottingTracerContext(DottingTracer dottingTracer, DottingSpan currentSpan, String family, Thread mainThread) {
        this.mainThread = mainThread;
        this.tracer = dottingTracer;
        LinkedList<DottingSpan> dottingSpans = new LinkedList<>();
        dottingSpans.add(currentSpan);
        spans.put(Thread.currentThread(), dottingSpans);
        this.family = family;
    }

    public static DottingTracerContext create(DottingTracer dottingTracer, DottingSpan currentSpan, String family, Thread mainThread) {
        return new DottingTracerContext(dottingTracer, currentSpan, family, mainThread);
    }

    public boolean isMainThread() {
        return Thread.currentThread().equals(mainThread);
    }

    public DottingTracer getTracer() {
        return tracer;
    }

    public void setTracer(DottingTracer tracer) {
        this.tracer = tracer;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public Thread getMainThread() {
        return mainThread;
    }

    public void setMainThread(Thread mainThread) {
        this.mainThread = mainThread;
    }

    public boolean isAllSpansFinished() {
        return spans != null && spans.get(Thread.currentThread()).size() == 0;
    }

    public boolean isAllAsyncFinished() {
        return spans != null && spans.get(Thread.currentThread()).size() == 1;//无中生妈
    }

    public synchronized DottingSpan getCurrentSpan() {
        if (spans != null && spans.size() > 0) {
            LinkedList<DottingSpan> nowSpans = spans.get(Thread.currentThread());
            if (nowSpans != null && nowSpans.size() > 0) {
                return nowSpans.getLast();
            }
        }
        return null;
    }

    public synchronized DottingSpan getAndRemoveCurrentSpan() {
        if (spans != null && spans.size() > 0) {
            LinkedList<DottingSpan> nowSpans = spans.get(Thread.currentThread());
            if (nowSpans != null && nowSpans.size() > 0) {
                return nowSpans.removeLast();
            }
        }
        return null;
    }

    public synchronized DottingSpan getParentSpan() {
        if (spans != null && spans.size() > 0) {
            LinkedList<DottingSpan> nowSpans = spans.get(Thread.currentThread());
            if (nowSpans != null && nowSpans.size() > 0) {
                return nowSpans.getLast();
            }

        }
        return null;
    }

    public synchronized void addCurrentSpan(DottingSpan currentSpan) {
        LinkedList<DottingSpan> nowSpans = spans.get(Thread.currentThread());
        if (nowSpans == null) {
            nowSpans = Lists.newLinkedList();
        }
        nowSpans.add(currentSpan);
        spans.put(Thread.currentThread(), nowSpans);
    }

    public synchronized boolean canMerge(DottingSpan currentSpan) {
        String key = String.format("%s-%s-%s", Thread.currentThread().getName(), currentSpan.getParentId(), currentSpan.getTitle());
        DottingSpan mergeSpan = merge.get(key);
        long duration = currentSpan.getEndTime() - currentSpan.getStartTime();
        if (mergeSpan != null) {
            mergeSpan.setMaxTime(duration).setMinTime(duration).incre(duration);
            return true;
        }
        merge.put(key, currentSpan.initMerge(duration));
        return false;
    }

    public synchronized void clearMerge(DottingSpan currentSpan) {
        String key = String.format("%s-%s-%s", Thread.currentThread().getName(), currentSpan.getParentId(), currentSpan.getTitle());
        merge.remove(key);
    }
}
