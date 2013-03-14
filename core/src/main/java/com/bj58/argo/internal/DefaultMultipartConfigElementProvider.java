package com.bj58.argo.internal;

import com.bj58.argo.ArgoException;
import com.google.inject.Provider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;

@Singleton
public class DefaultMultipartConfigElementProvider implements Provider<MultipartConfigElement> {

    private final MultipartConfigElement config;
    @Inject
    public DefaultMultipartConfigElementProvider(ServletContext servletContext) {
        try {
            File tempDir = (File)servletContext.getAttribute("javax.servlet.context.tempdir");
            config = new MultipartConfigElement(tempDir.getCanonicalPath());
        } catch (IOException e) {
            throw ArgoException.raise(e);
        }

    }

    @Override
    public MultipartConfigElement get() {
        return config;
    }
}
