package com.bj58.argo.internal.actionresult;

import com.bj58.argo.ActionResult;
import com.bj58.argo.BeatContext;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 */
@Singleton
public class StatusCodeActionResult {

    public final static ActionResult defaultSc404 = new ActionResult() {
        @Override
        public void render(BeatContext beatContext) {

            HttpServletResponse response = beatContext.getResponse();
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } catch (IOException e) {
                //TODO:log
                e.printStackTrace();
            }

        }
    };

    public final static ActionResult defaultSc405 = new ActionResult() {
        @Override
        public void render(BeatContext beatContext) {

            HttpServletResponse response = beatContext.getResponse();
            try {
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            } catch (IOException e) {
                //TODO:log
                e.printStackTrace();
            }
        }
    };

    @Inject
    @Named("HTTP_STATUS=404")
    ActionResult sc404;

    @Inject
    @Named("HTTP_STATUS=405")
    ActionResult sc405;

    public void render404(BeatContext beat) {
        sc404.render(beat);
    }

    public void render405(BeatContext beat) {
        sc405.render(beat);
    }

    public ActionResult getSc404() {
        return sc404;
    }

    public ActionResult getSc405() {
        return sc405;
    }
}
