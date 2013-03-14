 package com.bj58.argo.annotations;

 import java.lang.annotation.*;

 /**
  * 标识Action所处理HTTP POST请求类型
  *
  */
 @Target({ElementType.METHOD, ElementType.TYPE})
 @Retention(RetentionPolicy.RUNTIME)
 @Documented
 public @interface POST {

 }
