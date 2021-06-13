package net.x0ti.s0nar.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Decorator to identify event listener methods.
 * @param priority (optional) - Event Priority {default: LOW}
 * @author 0ti
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface S0narEventListener {
	public EventPriority priority() default EventPriority.LOW;
}
