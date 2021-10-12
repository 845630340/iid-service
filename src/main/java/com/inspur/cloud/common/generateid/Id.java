package com.inspur.cloud.common.generateid;

import java.lang.annotation.*;

/**
 * @author zhangrui
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {
    IdType type() default IdType.NONE;
}
