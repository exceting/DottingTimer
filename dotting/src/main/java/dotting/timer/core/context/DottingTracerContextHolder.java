package dotting.timer.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @author sunqinwen
 * @version \: DottingTracerContextHolder.java,v 0.1 2018-10-30 10:48
 */
public class DottingTracerContextHolder {

    private static TransmittableThreadLocal<DottingTracerContext> contextHolder = new TransmittableThreadLocal<>();

    private DottingTracerContextHolder() {
    }

    public static void setContext(DottingTracerContext context) {
        if (context == null) {
            removeContext();
        }
        contextHolder.set(context);
    }

    public static DottingTracerContext getContext() {
        return contextHolder.get();
    }


    public static void removeContext() {
        contextHolder.remove();
    }
}
