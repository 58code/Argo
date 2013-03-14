package com.bj58.argo.route;

import com.bj58.argo.ActionResult;

/**
 * 路由处理的结果
 */
public class RouteResult {

    public static RouteResult unMatch() {
        return new RouteResult(false, ActionResult.NULL);
    }

    public static RouteResult invoked(ActionResult result) {
        return new RouteResult(ActionResult.NULL != result, result);
    }

    private final boolean success;
    private final ActionResult result;

    private RouteResult(boolean success, ActionResult result) {
        this.success = success;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public ActionResult getResult() {
        return result;
    }
}
