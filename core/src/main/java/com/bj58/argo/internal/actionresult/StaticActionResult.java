package com.bj58.argo.internal.actionresult;

import com.bj58.argo.ActionResult;
import com.bj58.argo.Argo;
import com.bj58.argo.ArgoException;
import com.bj58.argo.BeatContext;
import com.bj58.argo.utils.PathUtils;
import com.google.inject.ImplementedBy;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 *
 * 处理静态文件
 *
 *
 */
public class StaticActionResult {

    @ImplementedBy(DefaultFactory.class)
    public static interface Factory {
        ActionResult create(String simplyPath);
    }

    private static class DefaultFactory implements Factory {


        @Inject
        public DefaultFactory() {
        }

        @Override
        public ActionResult create(String simplyPath) {

            return new DefaultStaticResult(simplyPath);
        }
    }

    private static class DefaultStaticResult implements ActionResult {

        private final String simplyPath;

        public DefaultStaticResult(String simplyPath) {
            this.simplyPath = simplyPath;

        }

        @Override
        public void render(BeatContext beatContext) {

            HttpServletRequest request = beatContext.getRequest();
            HttpServletResponse response = beatContext.getResponse();

            try {
                // 交给web容器处理
                request.getRequestDispatcher(simplyPath).forward(request, response);
            } catch (Throwable e) {
                throw ArgoException
                        .newBuilder(e)
                        .addContextVariable("File", simplyPath)
                        .build();
            }

        }
    }



}
