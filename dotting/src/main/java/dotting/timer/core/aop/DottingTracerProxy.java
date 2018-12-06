/**
 * sharemer.com Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package dotting.timer.core.aop;

import com.google.common.base.Strings;
import dotting.timer.core.annos.DottingNode;
import dotting.timer.core.builder.DottingSpanContext;
import dotting.timer.core.context.DottingTracerContext;
import dotting.timer.core.context.DottingTracerContextHolder;
import dotting.timer.core.push.Pusher;
import dotting.timer.core.span.DottingSpan;
import dotting.timer.core.utils.PushUtils;
import dotting.timer.core.utils.SpanTags;
import io.opentracing.References;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dotting.timer.core.tracer.DottingTracer;

import java.lang.reflect.Method;

/**
 * @author sunqinwen
 * @version \: DottingTracerProxy.java,v 0.1 2018-10-31 18:50
 */
@Aspect
public class DottingTracerProxy {

    private Logger logger = LoggerFactory.getLogger(DottingTracerProxy.class);

    public DottingTracerProxy(String hosts) {
        try {
            Pusher.initReceiver(hosts);
        } catch (Exception e) {
            logger.error("dotting tracer proxy init error ! hosts = {}", hosts, e);
        }
    }

    @Pointcut("@annotation(dotting.timer.core.annos.DottingNode)")
    public void dottingNode() {
    }

    @Before("dottingNode()")
    public void before(JoinPoint joinPoint) {
        try {
            MethodSignature methodSign = (MethodSignature) joinPoint.getSignature();
            Object target = joinPoint.getTarget();
            Method method = getMethod(target, methodSign);
            DottingNode dottingNode = method.getAnnotation(DottingNode.class);
            if (dottingNode == null) {
                return;
            }
            DottingTracerContext context = DottingTracerContextHolder.getContext();
            if (!dottingNode.root() && context == null) {
                return;
            }
            DottingTracer currentTracer = context == null ? null : context.getTracer();
            DottingSpan parentSpan = context == null ? null : context.getCurrentSpan();
            if (dottingNode.root() && currentTracer == null) {
                if (Strings.isNullOrEmpty(dottingNode.moudle())) {
                    return;
                }
                String className = joinPoint.getTarget().getClass().getName();
                String finalMethod = dottingNode.method();
                if (Strings.isNullOrEmpty(finalMethod)) {
                    finalMethod = method.getName();
                }
                finalMethod = String.format("%s.%s", className, finalMethod);
                currentTracer = new DottingTracer(PushUtils.sampled(finalMethod),
                        dottingNode.moudle(), dottingNode.debug());
                DottingSpan currentSpan = currentTracer.buildSpan(finalMethod)
                        .setExpect(dottingNode.expect())
                        .withTag(SpanTags.PRO_NAME, dottingNode.moudle())
                        .withTag(SpanTags.CLASS_NAME, className)
                        .withTag(SpanTags.MEHODE_NAME, finalMethod)
                        .start();
                DottingTracerContext currentContext = DottingTracerContext.create(currentTracer,
                        currentSpan, dottingNode.moudle(), Thread.currentThread());
                DottingTracerContextHolder.setContext(currentContext);
            } else {
                if (currentTracer == null) {
                    return;
                }
                String className = joinPoint.getTarget().getClass().getName();
                String finalMethod = dottingNode.method();
                if (Strings.isNullOrEmpty(finalMethod)) {
                    finalMethod = method.getName();
                }
                finalMethod = String.format("%s.%s", className, finalMethod);

                DottingSpan currentSpan = null;

                if (context.isMainThread()) {
                    if (parentSpan == null || parentSpan.context() == null) {
                        return;
                    }
                } else {
                    if (parentSpan == null || parentSpan.context() == null) {
                        currentSpan = currentTracer.buildSpan(finalMethod)
                                .setExpect(dottingNode.expect())
                                .setIsAsync(true)
                                .withTag(SpanTags.PRO_NAME, dottingNode.moudle())
                                .withTag(SpanTags.CLASS_NAME, className)
                                .withTag(SpanTags.MEHODE_NAME, finalMethod)
                                .start();
                    }
                }
                if (currentSpan == null) {
                    DottingSpanContext dottingSpanContext = new DottingSpanContext(parentSpan.context().getTraceId(),
                            parentSpan.context().getSpanId());
                    currentSpan = currentTracer.buildSpan(finalMethod)
                            .addReference(References.CHILD_OF, dottingSpanContext)
                            .setExpect(dottingNode.expect())
                            .withTag(SpanTags.PRO_NAME, Strings.isNullOrEmpty(dottingNode.moudle()) ?
                                    currentTracer.getMoudle() : dottingNode.moudle())
                            .withTag(SpanTags.CLASS_NAME, className)
                            .withTag(SpanTags.MEHODE_NAME, finalMethod)
                            .start();
                }
                context.addCurrentSpan(currentSpan);
            }
        } catch (Exception e) {
            logger.error("dotting tracer before error !", e);
        }
    }

    @AfterReturning("dottingNode()")
    public void after(JoinPoint joinPoint) {
        try {
            MethodSignature methodSign = (MethodSignature) joinPoint.getSignature();
            Object target = joinPoint.getTarget();
            Method method = getMethod(target, methodSign);
            DottingNode dottingNode = method.getAnnotation(DottingNode.class);
            if (dottingNode == null) {
                return;
            }
            DottingTracerContext context = DottingTracerContextHolder.getContext();
            if (context == null) {
                return;
            }
            DottingTracer currentTracer = context.getTracer();
            DottingSpan currentSpan = context.getAndRemoveCurrentSpan();
            if (currentTracer == null || currentSpan == null) {
                return;
            }
            currentSpan.finish();
            if (currentSpan.getParentId() == 0 && context.isAllSpansFinished()) {
                DottingTracerContextHolder.removeContext();
                currentTracer.pushSpans();
            }
        } catch (Exception e) {
            logger.error("dotting tracer after error !", e);
        }
    }

    @AfterThrowing(pointcut = "dottingNode()", throwing = "e")
    public void doException(JoinPoint joinPoint, Exception e) {
        try {
            MethodSignature methodSign = (MethodSignature) joinPoint.getSignature();
            Object target = joinPoint.getTarget();
            Method method = getMethod(target, methodSign);
            DottingNode dottingNode = method.getAnnotation(DottingNode.class);
            if (dottingNode == null) {
                return;
            }
            DottingTracerContext context = DottingTracerContextHolder.getContext();
            if (context == null) {
                return;
            }
            DottingTracer currentTracer = context.getTracer();
            DottingSpan currentSpan = context.getAndRemoveCurrentSpan();
            if (currentTracer == null || currentSpan == null) {
                return;
            }
            currentSpan
                    .setTag(SpanTags.ERROR_MSG, e.getMessage())
                    .setError(true);
            currentSpan.finish();
            if (currentSpan.getParentId() == 0) {
                currentTracer.pushSpans();
            }
        } catch (Exception ec) {
            logger.error("dotting tracer after throwing error !", ec);
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