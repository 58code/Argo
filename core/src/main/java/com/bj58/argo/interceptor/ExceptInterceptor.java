package com.bj58.argo.interceptor;

import com.bj58.argo.ActionResult;
import com.bj58.argo.BeatContext;

/**
 * 出错拦截器
 * TODO: 还未实现
 */
public interface ExceptInterceptor {

    Throwable[] getExceptionScopes();

    ActionResult catchMe(Throwable e, BeatContext beatContext);
}
