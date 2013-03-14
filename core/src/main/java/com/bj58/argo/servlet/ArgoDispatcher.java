package com.bj58.argo.servlet;

import com.bj58.argo.BeatContext;
import com.bj58.argo.internal.DefaultArgoDispatcher;
import com.google.inject.ImplementedBy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * 用于处理request请求调度的核心类
 *
 */
@ImplementedBy(DefaultArgoDispatcher.class)
public interface ArgoDispatcher {

    void init();

    void service(HttpServletRequest request, HttpServletResponse response);

    void destroy();

    public HttpServletRequest currentRequest();

    public HttpServletResponse currentResponse();

    BeatContext currentBeatContext();

}
