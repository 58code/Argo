package com.bj58.argo.internal;

import java.util.Map;

import com.bj58.argo.ActionResult;
import com.bj58.argo.route.Action;
import com.bj58.argo.interceptor.PostInterceptor;
import com.bj58.argo.interceptor.PreInterceptor;
import com.bj58.argo.route.RouteBag;
import com.bj58.argo.route.RouteResult;
import com.bj58.argo.thirdparty.AntPathMatcher;
import com.bj58.argo.thirdparty.PathMatcher;
import com.google.common.collect.Maps;

public class MethodAction implements Action {

    public static MethodAction create(ActionInfo actionInfo) {
        return new MethodAction(actionInfo);
    }

    private final ActionInfo actionInfo;

    private final double order;

    private PathMatcher pathMatcher = new AntPathMatcher();
    


    private MethodAction(ActionInfo actionInfo) {
        this.actionInfo = actionInfo;

        order = actionInfo.getOrder()
                + (10000.0d - actionInfo.getPathPattern().length())/100000.0d
                + (actionInfo.isPattern() ? 0.5d : 0d);
    }

    @Override
    public double order() {
        return order;
    }
    
    @Override
    public RouteResult matchAndInvoke(RouteBag bag) {

        if (!actionInfo.matchHttpMethod(bag))
            return RouteResult.unMatch();

        Map<String, String> uriTemplateVariables = Maps.newHashMap();

        boolean match = actionInfo.match(bag, uriTemplateVariables);
        if (!match)
            return RouteResult.unMatch();

        // PreIntercept
        for(PreInterceptor preInterceptor : actionInfo.getPreInterceptors()) {
            ActionResult actionResult = preInterceptor.preExecute(bag.getBeat());
            if (ActionResult.NULL != actionResult)
                return RouteResult.invoked(actionResult);
        }

        ActionResult actionResult = actionInfo.invoke(uriTemplateVariables);

        // PostIntercept
        for(PostInterceptor postInterceptor : actionInfo.getPostInterceptors()) {
            actionResult = postInterceptor.postExecute(bag.getBeat(), actionResult);
        }

        return RouteResult.invoked(actionResult);
    }

}
