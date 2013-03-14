package com.bj58.argo.interceptor;

import java.lang.annotation.*;

/**
 * 设置拦截器
 * @author renjun
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreInterceptorAnnotation {
    Class<? extends PreInterceptor> value();
}