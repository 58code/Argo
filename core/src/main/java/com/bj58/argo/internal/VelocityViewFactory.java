package com.bj58.argo.internal;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletResponse;

import com.bj58.argo.*;
import com.bj58.argo.controller.ViewFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.apache.velocity.io.VelocityWriter;

import org.apache.velocity.runtime.RuntimeInstance;

@Singleton
public class VelocityViewFactory implements ViewFactory {

    private final RuntimeInstance rtInstance;

    private final String suffix = ".html";

    @Inject
	public VelocityViewFactory(Argo argo) {

        String viewFolder = viewFolderPath(argo);

        Properties ps = new Properties();
        ps.setProperty("resource.loader", "file");
        ps.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
        ps.setProperty("file.resource.loader.path", viewFolder);
        ps.setProperty("file.resource.loader.cache", "false");
        ps.setProperty("file.resource.loader.modificationCheckInterval", "2");
        ps.setProperty("input.encoding", "UTF-8");
        ps.setProperty("output.encoding", "UTF-8");
        ps.setProperty("default.contentType", "text/html; charset=UTF-8");
        ps.setProperty("velocimarco.library.autoreload", "true");
        ps.setProperty("runtime.log.error.stacktrace", "false");
        ps.setProperty("runtime.log.warn.stacktrace", "false");
        ps.setProperty("runtime.log.info.stacktrace", "false");
        ps.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
        ps.setProperty("runtime.log.logsystem.log4j.category", "velocity_log");

        rtInstance = new RuntimeInstance();

        try {
            rtInstance.init(ps);
        } catch (Exception e) {
            throw ArgoException.raise(e);
        }
    }

    private String viewFolderPath(Argo argo) {
        File parent = argo.currentFolder();
        return new File(parent, "views").getAbsolutePath();
    }
//
//    private File getViewFolder(GroupConvention groupConvention) {
//        ClassLoader cl = Thread.currentThread().getContextClassLoader();
//
//        URL viewURL = cl.getResource("views/");
//        try {
//            return new File(viewURL.toURI());
//        } catch (URISyntaxException e) {
//            throw ArgoException.raise("folder: " + viewURL + " must exist!!!", e);
//        }
//    }

    @Override
    public ActionResult create(String viewName) {
        return new VelocityViewResult(this, viewName);
    }

    Template getTemplate(String viewName) {
        return rtInstance.getTemplate(viewName + suffix);
    }

    private static class VelocityViewResult implements ActionResult {

        private final VelocityViewFactory factory;

        private final String viewName;

        private VelocityViewResult(VelocityViewFactory factory, String viewName) {
            this.factory = factory;
            this.viewName = viewName;
        }

        @Override
        public void render(BeatContext beatContext) {

            Template template =  factory.getTemplate(viewName);

            HttpServletResponse response = beatContext.getResponse();
            response.setContentType("text/html;charset=\"UTF-8\"");
            response.setCharacterEncoding("UTF-8");
            // init context:
            Context context = new VelocityContext(beatContext.getModel().getModel());
            // render:
            VelocityWriter vw = null;
            try {
                vw = new VelocityWriter(response.getWriter());
                template.merge(context, vw);
                vw.flush();
            } catch (IOException e) {
                throw ArgoException.raise(e);
            }
            finally {
                if (vw != null)
                    vw.recycle(null);
            }
        }
    }
}
