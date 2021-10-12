package com.inspur.cloud.common.response.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author LisonCheung
 * @version 1.0
 * @date 2020/5/20
 * @since 2020/5/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface ResponseResult {

}
