package com.bj58.argo.route;

/**
 * 封装的action方法
 */
public interface Action {
    /**
     * 确定优先级，路由时根据优先级进行匹配
     * @return 优先级
     */
    double order();

    /**
     * 匹配并且执行
     * @param bag 当前路由信息
     * @return 匹配或执行的结果
     */
    RouteResult matchAndInvoke(RouteBag bag);

}
