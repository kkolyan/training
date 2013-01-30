package net.kkolyan.web.weedyweb.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author nplekhanov
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Action {
    String path();
    String[] method() default {"GET","POST"};
    String[] params() default {};
    String view() default "";
}
