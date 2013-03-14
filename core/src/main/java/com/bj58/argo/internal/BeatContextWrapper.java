package com.bj58.argo.internal;

import com.bj58.argo.BeatContext;
import com.bj58.argo.Model;
import com.bj58.argo.client.ClientContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BeatContextWrapper implements BeatContext {

    private BeatContext beatContext;

    public BeatContextWrapper(BeatContext beatContext) {
        this.beatContext = beatContext;
    }

    @Override
    public Model getModel() {
        return beatContext.getModel();
    }

    @Override
    public HttpServletRequest getRequest() {
        return beatContext.getRequest();
    }

    @Override
    public HttpServletResponse getResponse() {
        return beatContext.getResponse();
    }

    @Override
    public ServletContext getServletContext() {
        return beatContext.getServletContext();
    }

    @Override
    public ClientContext getClient() {
        return beatContext.getClient();
    }

//    @Override
//    public Lifecycle getLifecycle() {
//        return beatContext.getLifecycle();
//    }
}
