 package com.bj58.argo.annotations;

 import java.lang.annotation.*;


 /**
  * 标识Action所处理HTTP请求类型
  *
  * 处理除POST外所有HTTP method，包括 GET， HEAD等
  */
 @Target({ElementType.METHOD, ElementType.TYPE})
 @Retention(RetentionPolicy.RUNTIME)
 @Documented
 public @interface GET{
 }
