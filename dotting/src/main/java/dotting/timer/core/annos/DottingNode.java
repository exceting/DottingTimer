package dotting.timer.core.annos;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sunqinwen
 * @version \: DottingNode.java,v 0.1 2018-10-30 11:16
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DottingNode {

    String moudle() default "";

    String method() default "";

    boolean root() default false;

    long expect() default 0L;

    boolean debug() default false;

}