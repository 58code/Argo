package com.bj58.argo.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* 利用Filter来实行调度
*/
@WebFilter(urlPatterns = {"/*"},
        dispatcherTypes = {DispatcherType.REQUEST},
        initParams = {@WebInitParam(name = "encoding", value = "UTF-8")}
)
public class ArgoFilter implements Filter {

    private ArgoDispatcher dispatcher;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {


        ServletContext servletContext = filterConfig.getServletContext();

        try {
            dispatcher = ArgoDispatcherFactory.create(servletContext);
            dispatcher.init();
        } catch (Exception e) {

            servletContext.log("failed to argo initialize, system exit!!!", e);
            System.exit(1);

        }

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        dispatcher.service(httpReq, httpResp);

    }

    @Override
    public void destroy() {
        dispatcher.destroy();
    }
}
