package com.bj58.argo.inject;

import com.bj58.argo.*;
import com.bj58.argo.client.ClientContext;
import com.bj58.argo.client.ClientContext.DefaultClientContext;
import com.bj58.argo.convention.GroupConvention;
import com.bj58.argo.internal.DefaultBeatContext;
import com.bj58.argo.internal.DefaultModel;
import com.bj58.argo.internal.DefaultMultipartConfigElementProvider;
import com.bj58.argo.internal.StaticFilesAction;
import com.bj58.argo.internal.actionresult.StatusCodeActionResult;
import com.bj58.argo.route.Action;
import com.bj58.argo.route.StaticActionAnnotation;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;

import javax.inject.Singleton;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * argo的默认注入，所有注入项都可以被project级及group级注入覆盖
 * 优先级有低到高的顺序是
 * Argo
 * @see com.bj58.argo.convention.ProjectConvention
 * @see com.bj58.argo.convention.GroupConvention.ProjectConfig#module()
 *
 * @author renjun
 */
public class ArgoModule extends AbstractModule {

    private final Argo argo;

    public ArgoModule(Argo argo) {

        this.argo = argo;

    }

    @Override
    protected void configure() {

        bind(ServletRequest.class).to(HttpServletRequest.class);
        bind(ServletResponse.class).to(HttpServletResponse.class);

        bind(BeatContext.class)
                .annotatedWith(ArgoSystem.class)
                .to(DefaultBeatContext.class);

        bind(ActionResult.class)
                .annotatedWith(Names.named("HTTP_STATUS=404"))
                .toInstance(StatusCodeActionResult.defaultSc404);

        bind(ActionResult.class)
                .annotatedWith(Names.named("HTTP_STATUS=405"))
                .toInstance(StatusCodeActionResult.defaultSc405);

        bind(Action.class).annotatedWith(StaticActionAnnotation.class)
                .to(StaticFilesAction.class);

        bind(ClientContext.class).to(DefaultClientContext.class);
        bind(Model.class).to(DefaultModel.class);

        bind(MultipartConfigElement.class)
                .toProvider(DefaultMultipartConfigElementProvider.class)
                .in(Singleton.class);

        // bind all controllers.
        for (Class<? extends ArgoController> clazz : argo.getControllerClasses())
            bind(clazz).in(Singleton.class);
    }


    @Provides
    private HttpServletRequest provideReuqest() {
        return argo.currentRequest();
    }

    @Provides
    private  HttpServletResponse provideResponse() {
        return argo.currentResponse();
    }

    @Provides
    @ArgoSystem
    @Singleton
    private Set<Class<? extends ArgoController>> provideControllerClasses() {
        return argo.getControllerClasses();
    }


    @Provides
    @Singleton
    private GroupConvention provideGroupConvention() {
        return argo.groupConvention();
    }

    @Provides
    @Singleton
    private Argo provideArgo() {
        return argo;
    }

    @Provides
    @Singleton
    private ServletContext provideServletContext() {
        return argo.servletContext();
    }

    @Provides
    @Singleton
    private BeatContext provideBeatContext() {
        return argo.beatContext();
    }

    @Provides
    @ArgoSystem
    @Singleton
    private Executor provideExecutor() {
        return Executors.newCachedThreadPool();
    }


    @Provides
    @Singleton
    private GroupConvention.GroupConfig provideGroupConfig() {
        return argo.groupConvention().group();
    }

    @Provides
    @Singleton
    private GroupConvention.ProjectConfig provideProjectConfig() {
        return argo.groupConvention().currentProject();
    }

}
