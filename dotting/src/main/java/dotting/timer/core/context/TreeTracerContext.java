package dotting.timer.core.context;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dotting.timer.core.span.TreeSpan;
import dotting.timer.core.tracer.TreeTracer;

import java.util.LinkedList;
import java.util.Map;

/**
 * @author sunqinwen
 * @version \: TreeTracerContext.java,v 0.1 2018-10-30 10:21
 */
public class TreeTracerContext {

    private TreeTracer tracer;

    private Thread mainThread;

    private Map<Thread, LinkedList<TreeSpan>> spans = Maps.newConcurrentMap();

    private String family;

    private TreeTracerContext(TreeTracer treeTracer, TreeSpan currentSpan, String family, Thread mainThread) {
        this.mainThread = mainThread;
        this.tracer = treeTracer;
        LinkedList<TreeSpan> treeSpans = new LinkedList<>();
        treeSpans.add(currentSpan);
        spans.put(Thread.currentThread(), treeSpans);
        this.family = family;
    }

    public static dotting.timer.core.context.TreeTracerContext create(TreeTracer treeTracer, TreeSpan currentSpan, String family, Thread mainThread) {
        return new dotting.timer.core.context.TreeTracerContext(treeTracer, currentSpan, family, mainThread);
    }

    public boolean isMainThread() {
        return Thread.currentThread().equals(mainThread);
    }

    public TreeTracer getTracer() {
        return tracer;
    }

    public void setTracer(TreeTracer tracer) {
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

    public boolean isAllSpansFinished(){
        return spans != null && spans.get(Thread.currentThread()).size() == 0;
    }

    public synchronized TreeSpan getCurrentSpan() {
        if (spans != null && spans.size() > 0) {
            LinkedList<TreeSpan> nowSpans = spans.get(Thread.currentThread());
            if (nowSpans != null && nowSpans.size() > 0) {
                return nowSpans.getLast();
            }
        }
        return null;
    }

    public synchronized TreeSpan getAndRemoveCurrentSpan() {
        if (spans != null && spans.size() > 0) {
            LinkedList<TreeSpan> nowSpans = spans.get(Thread.currentThread());
            if (nowSpans != null && nowSpans.size() > 0) {
                return nowSpans.removeLast();
            }
        }
        return null;
    }

    public synchronized TreeSpan getParentSpan() {
        if (spans != null && spans.size() > 0) {
            LinkedList<TreeSpan> nowSpans = spans.get(Thread.currentThread());
            if (nowSpans != null && nowSpans.size() > 0) {
                return nowSpans.getLast();
            }

        }
        return null;
    }

    public synchronized void addCurrentSpan(TreeSpan currentSpan) {
        LinkedList<TreeSpan> nowSpans = spans.get(Thread.currentThread());
        if (nowSpans == null) {
            nowSpans = Lists.newLinkedList();
        }
        nowSpans.add(currentSpan);
        spans.put(Thread.currentThread(), nowSpans);
    }
}
