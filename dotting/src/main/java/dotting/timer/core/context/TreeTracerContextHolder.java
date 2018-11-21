package dotting.timer.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @author sunqinwen
 * @version \: TreeTracerContextHolder.java,v 0.1 2018-10-30 10:48
 */
public class TreeTracerContextHolder {

    private static TransmittableThreadLocal<dotting.timer.core.context.TreeTracerContext> contextHolder = new TransmittableThreadLocal<>();

    private TreeTracerContextHolder() {
    }

    public static void setContext(dotting.timer.core.context.TreeTracerContext context) {
        if (context == null) {
            removeContext();
        }
        contextHolder.set(context);
    }

    public static dotting.timer.core.context.TreeTracerContext getContext() {
        return contextHolder.get();
    }


    public static void removeContext() {
        contextHolder.remove();
    }
}
