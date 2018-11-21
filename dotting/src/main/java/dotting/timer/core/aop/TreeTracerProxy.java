/**
 * Bilibili.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package dotting.timer.core.aop;

import com.google.common.base.Strings;
import io.opentracing.References;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dotting.timer.core.annos.TracePoint;
import dotting.timer.core.tracer.TreeTracer;

import java.lang.reflect.Method;

/**
 * @author sunqinwen
 * @version \: TreeTracerProxy.java,v 0.1 2018-10-31 18:50
 */
@Aspect
public class TreeTracerProxy {

    private Logger logger = LoggerFactory.getLogger(dotting.timer.core.aop.TreeTracerProxy.class);

    public TreeTracerProxy() {
    }

    public TreeTracerProxy(String driver, String user, String password, String url) {
        dotting.timer.core.debug.db.mysql.ConnectionPool.initConnectionPool(driver, user, password, url);
    }

    @Pointcut("@annotation(dotting.timer.core.annos.TracePoint)")
    public void tracePoint() {
    }

    @Before("tracePoint()")
    public void before(JoinPoint joinPoint) {
        try {
            MethodSignature methodSign = (MethodSignature) joinPoint.getSignature();
            Object target = joinPoint.getTarget();
            Method method = getMethod(target, methodSign);
            TracePoint tracePoint = method.getAnnotation(TracePoint.class);
            if (tracePoint == null) {
                return;
            }
            dotting.timer.core.context.TreeTracerContext context = dotting.timer.core.context.TreeTracerContextHolder.getContext();
            if (!tracePoint.root() && context == null) {
                return;
            }
            TreeTracer currentTracer = context == null ? null : context.getTracer();
            dotting.timer.core.span.TreeSpan parentSpan = context == null ? null : context.getCurrentSpan();
            if (tracePoint.root() && currentTracer == null) {
                if (Strings.isNullOrEmpty(tracePoint.moudle())) {
                    return;
                }
                String className = joinPoint.getTarget().getClass().getName();
                String finalMethod = tracePoint.method();
                if (Strings.isNullOrEmpty(finalMethod)) {
                    finalMethod = method.getName();
                }
                finalMethod = String.format("%s.%s", className, finalMethod);
                currentTracer = new TreeTracer(dotting.timer.core.utils.PushUtils.sampled(finalMethod),
                        tracePoint.moudle(), tracePoint.debug());
                dotting.timer.core.span.TreeSpan currentSpan = currentTracer.buildSpan(finalMethod)
                        .setExpect(tracePoint.expect())
                        .withTag(dotting.timer.core.utils.SpanTags.PRO_NAME, tracePoint.moudle())
                        .withTag(dotting.timer.core.utils.SpanTags.CLASS_NAME, className)
                        .withTag(dotting.timer.core.utils.SpanTags.MEHODE_NAME, finalMethod)
                        .start();
                dotting.timer.core.context.TreeTracerContext currentContext = dotting.timer.core.context.TreeTracerContext.create(currentTracer,
                        currentSpan, tracePoint.moudle(), Thread.currentThread());
                dotting.timer.core.context.TreeTracerContextHolder.setContext(currentContext);
            } else {
                if (currentTracer == null || parentSpan == null || parentSpan.context() == null) {
                    return;
                }
                String className = joinPoint.getTarget().getClass().getName();
                String finalMethod = tracePoint.method();
                if (Strings.isNullOrEmpty(finalMethod)) {
                    finalMethod = method.getName();
                }
                finalMethod = String.format("%s.%s", className, finalMethod);

                dotting.timer.core.span.TreeSpan currentSpan;

                if (context.isMainThread()) {
                    dotting.timer.core.builder.TreeSpanContext treeSpanContext = new dotting.timer.core.builder.TreeSpanContext(parentSpan.context().getTraceId(),
                            parentSpan.context().getSpanId());
                    currentSpan = currentTracer.buildSpan(finalMethod)
                            .addReference(References.CHILD_OF, treeSpanContext)
                            .setExpect(tracePoint.expect())
                            .withTag(dotting.timer.core.utils.SpanTags.PRO_NAME, Strings.isNullOrEmpty(tracePoint.moudle()) ?
                                    currentTracer.getMoudle() : tracePoint.moudle())
                            .withTag(dotting.timer.core.utils.SpanTags.CLASS_NAME, className)
                            .withTag(dotting.timer.core.utils.SpanTags.MEHODE_NAME, finalMethod)
                            .start();
                } else {
                    currentSpan = currentTracer.buildSpan(finalMethod)
                            .setExpect(tracePoint.expect())
                            .setIsAsync(true)
                            .withTag(dotting.timer.core.utils.SpanTags.PRO_NAME, tracePoint.moudle())
                            .withTag(dotting.timer.core.utils.SpanTags.CLASS_NAME, className)
                            .withTag(dotting.timer.core.utils.SpanTags.MEHODE_NAME, finalMethod)
                            .start();
                }
                context.addCurrentSpan(currentSpan);
            }
        } catch (Exception e) {
            logger.error("tree tracer before error !", e);
        }
    }

    @AfterReturning("tracePoint()")
    public void after(JoinPoint joinPoint) {
        try {
            MethodSignature methodSign = (MethodSignature) joinPoint.getSignature();
            Object target = joinPoint.getTarget();
            Method method = getMethod(target, methodSign);
            TracePoint tracePoint = method.getAnnotation(TracePoint.class);
            if (tracePoint == null) {
                return;
            }
            dotting.timer.core.context.TreeTracerContext context = dotting.timer.core.context.TreeTracerContextHolder.getContext();
            if (context == null) {
                return;
            }
            TreeTracer currentTracer = context.getTracer();
            dotting.timer.core.span.TreeSpan currentSpan = context.getAndRemoveCurrentSpan();
            if (currentTracer == null || currentSpan == null) {
                return;
            }
            currentSpan.finish();
            if (tracePoint.root() && context.isAllSpansFinished()) {
                dotting.timer.core.context.TreeTracerContextHolder.removeContext();
                currentTracer.pushSpans();
            }
        } catch (Exception e) {
            logger.error("tree tracer after error !", e);
        }
    }

    @AfterThrowing(pointcut = "tracePoint()", throwing = "e")
    public void doException(JoinPoint joinPoint, Exception e) {
        try {
            MethodSignature methodSign = (MethodSignature) joinPoint.getSignature();
            Object target = joinPoint.getTarget();
            Method method = getMethod(target, methodSign);
            TracePoint tracePoint = method.getAnnotation(TracePoint.class);
            if (tracePoint == null) {
                return;
            }
            dotting.timer.core.context.TreeTracerContext context = dotting.timer.core.context.TreeTracerContextHolder.getContext();
            if (context == null) {
                return;
            }
            TreeTracer currentTracer = context.getTracer();
            dotting.timer.core.span.TreeSpan currentSpan = context.getAndRemoveCurrentSpan();
            if (currentTracer == null || currentSpan == null) {
                return;
            }
            currentSpan
                    .setTag(dotting.timer.core.utils.SpanTags.ERROR_MSG, e.getMessage())
                    .setError(true);
            currentSpan.finish();
            if (tracePoint.root()) {
                currentTracer.pushSpans();
            }
        } catch (Exception ec) {
            logger.error("tree tracer after throwing error !", ec);
        }
    }

    private Method getMethod(Object target, MethodSignature signature) {
        Method method = null;
        try {
            method = target.getClass()
                    .getMethod(signature.getName(), signature.getParameterTypes());
        } catch (NoSuchMethodException | SecurityException ignored) {
        }
        return method;
    }
}