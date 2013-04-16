package utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.Callable;

import play.mvc.With;

/**
 * Declares a composing action that will check for a condition before deciding on whether to proceed with the request.
 */
@With(PredicatedAction.class)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Predicated {
    /**
     * The condition.
     */
    Class<? extends Callable<Boolean>> predicate();

    /**
     * The http status code to return if the condition fails.
     */
    int failed();
}