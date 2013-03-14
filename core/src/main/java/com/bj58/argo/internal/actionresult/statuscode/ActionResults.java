package com.bj58.argo.internal.actionresult.statuscode;

import com.bj58.argo.ArgoException;
import com.bj58.argo.ActionResult;
import com.bj58.argo.BeatContext;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 *
 */
public class ActionResults {

    private ActionResults() {}

    public static ActionResult redirect(final String url) {
        return new ActionResult() {
            @Override
            public void render(BeatContext beatContext) {
                try {
                    beatContext.getResponse().sendRedirect(url);
                } catch (IOException e) {

                    throw ArgoException.newBuilder(e)
                            .addContextVariable("redirect url:", url)
                            .build();
                }
            }
        };
    }

    public static ActionResult redirect301(final String url) {
        return new ActionResult() {
            @Override
            public void render(BeatContext beatContext) {
                try {
                    //fixMe: 需要判断是否是同一个schema等因素
                    HttpServletResponse response = beatContext.getResponse();
                    response.setStatus(301);
                    response.sendRedirect(url);
                } catch (IOException e) {
                    throw ArgoException.newBuilder(e)
                            .addContextVariable("redirect url:", url)
                            .build();
                }
            }
        };
    }


}
