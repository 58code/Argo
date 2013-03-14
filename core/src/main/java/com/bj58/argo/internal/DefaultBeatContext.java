package com.bj58.argo.internal;

import com.bj58.argo.BeatContext;
import com.bj58.argo.client.ClientContext;
import com.bj58.argo.inject.ArgoSystem;
import com.bj58.argo.Model;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ArgoSystem
public class DefaultBeatContext implements BeatContext {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final Model model;
    private final ClientContext clientContext;
    private final ServletContext servletContext;

    @Inject
    public DefaultBeatContext(HttpServletRequest request, HttpServletResponse response, Model model, ClientContext clientContext, ServletContext servletContext) {
        this.request = request;
        this.response = response;
        this.model = model;
        this.clientContext = clientContext;
        this.servletContext = servletContext;
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @Override
    public HttpServletResponse getResponse() {
        return response;
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public ClientContext getClient() {
        return clientContext;
    }


}
