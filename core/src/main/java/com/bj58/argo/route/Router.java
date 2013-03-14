package com.bj58.argo.route;

import com.bj58.argo.BeatContext;
import com.bj58.argo.ActionResult;
import com.bj58.argo.internal.DefaultRouter;
import com.google.inject.ImplementedBy;

/**
 * 路由器，根据每个请求的url进行匹配找到合适的
 * @see Action
 * 来执行
 */
@ImplementedBy(DefaultRouter.class)
//@Singleton
public interface Router {

    public ActionResult route(BeatContext beat);

}
