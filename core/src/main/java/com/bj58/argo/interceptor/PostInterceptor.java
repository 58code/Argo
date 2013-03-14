package com.bj58.argo.interceptor;

import com.bj58.argo.ActionResult;
import com.bj58.argo.BeatContext;

/**
 * 方法执行完成后的拦截处理,可以与
 * @see PreInterceptor
 * 共同实现，也可以单独使用
 *
 * @see PostInterceptorAnnotation
 */
public interface PostInterceptor {

    /**
     * 拦截当前请求
     * @param beat 当前请求的上下文
     * @return
     * null，进入下一个拦截或执行Action
     * <BR/>
     * 非空，直接显示，不进入下一个拦截或执行Action
     */
    public ActionResult postExecute(BeatContext beat, ActionResult executionResult);

}
