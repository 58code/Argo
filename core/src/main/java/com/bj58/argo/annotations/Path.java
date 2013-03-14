package com.bj58.argo.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 标注资源类或方法的相对路径
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Path {

    /**
     * 对应的url相对路径
     * 可以使用正则表达式
     */
    String value();

    /**
     * 确定匹配的优先级
     *
     * @return 模式匹配的优先级
     */
    int order() default 10000;

}