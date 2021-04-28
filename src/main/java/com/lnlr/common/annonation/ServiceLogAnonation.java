package com.lnlr.common.annonation;

import java.lang.annotation.*;

/**
 * @author:leihfei
 * @description:
 * @date:Create in 19:17 2018/11/29
 * @email:leihfein@gmail.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceLogAnonation {

    String type() default "";

    String value() default "";

    String moduleName() default "";
}
