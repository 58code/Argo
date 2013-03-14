package com.bj58.argo.servlet;

import com.bj58.argo.Argo;
import com.bj58.argo.convention.GroupConvention;
import com.bj58.argo.convention.GroupConventionFactory;

import javax.servlet.ServletContext;


public class ArgoDispatcherFactory {

    public static ArgoDispatcher create(ServletContext servletContext){

        // xxx:这是一处硬编码
        GroupConvention groupConvention = GroupConventionFactory.getGroupConvention();

        return Argo.instance.init(servletContext, groupConvention);

    }
}
