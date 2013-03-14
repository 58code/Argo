package com.bj58.argo.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bj58.argo.*;
import com.bj58.argo.internal.actionresult.statuscode.ActionResults;
import com.bj58.argo.logs.Logger;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 提供一个基础的抽象的Controller实现，并提供一些Controller实现所常用的方法
 *
 * @author renjun
 */
public abstract class AbstractController implements ArgoController {

    @Inject
    private Argo argo;

    @Inject
    private ViewFactory viewFactory;

    private Logger logger;

    @Override
    public void init() {
        this.logger = argo.getLogger(this.getClass());

        logger().info("initialize");
    }

     /**
      * 返回一个view的ActionResult
      * 系统默认采用velocity实现。<br/>
      * viewName + .html存放的目录在 maven项目的resources/views下
      * 编译后存放在classes/views下
      *
      * @param viewName view的名字
      * @return 合适ActionResult
      */
    protected ActionResult view(String viewName) {
		return viewFactory.create(viewName);
	}

     /**
      * 跳转到一个新页面
      *
      * @param redirectUrl 调整页面的url
      * @return Http 302 跳转
      */
    protected ActionResult redirect(String redirectUrl) {
        return ActionResults.redirect(redirectUrl);
    }

    /**
     * 301永久跳转到一个新页面
     *
     * @param redirectUrl 调整页面的url
     * @return Http 1 跳转
     */
    protected ActionResult redirect301(String redirectUrl) {
        return ActionResults.redirect301(redirectUrl);
    }

    /**
     * 得到model
     *
     * @return 当前请求的model
     */
    protected Model model() {
		
		return  beat().getModel();
	}

    /**
     * 获得当前的上下文信息
     *
     * @return 当前beat
     */
    protected BeatContext beat() {
		
		return argo.beatContext();
	}

    /**
     * 获得当前的request
     *
     * @return 获得当前的request
     */
    protected HttpServletRequest request() {
		
		return  beat().getRequest();
	}

    /**
     * 获得当前的response
     *
     * @return 获得当前的response
     */
    protected HttpServletResponse response() {
		
		return  beat().getResponse();
	}

    /**
     * 获得当前类的logger
     * @return 当前类的logger
     */
    protected Logger logger() {
        return logger;
    }

    /**
     * 根据名字获得Logger
     * @param name logger的名字
     * @return 对应的logger
     */
    protected Logger logger(String name) {
    
        return argo.getLogger(name);
    }

    /**
     * 根据类名获得Logger
     * @param clazz 类名
     * @return 对应的logger
     */
    protected Logger logger(Class clazz) {
    
        return argo.getLogger(clazz);
    }

    /**
     * 获得上帝 Argo
     * @return argo
     */
    protected Argo argo() {
        return this.argo;
    }

    /**
     * 以原始的方式提供写response的方法
     * @return 写Response
     */
    protected InnerPrintWriter writer() {
        return new InnerPrintWriter(beat().getResponse());
    }

    protected static class InnerPrintWriter implements ActionResult {

        private final HttpServletResponse response;
        private PrintWriter writer = null;

        public InnerPrintWriter(HttpServletResponse response) {
            this.response = response;
        }

        private PrintWriter getPrintWriter() {
            if (writer != null)
                return writer;
            try {
                writer = response.getWriter();
                return writer;
            } catch (IOException e) {
                throw ArgoException.raise(e);
            }
        }

        public InnerPrintWriter setStatus(int sc) {
            response.setStatus(sc);
            return this;
        }

        public InnerPrintWriter setContentType(String type) {
            response.setContentType(type);
            return this;
        }

        public InnerPrintWriter setDateHeader(String name, long date) {
            response.setDateHeader(name, date);
            return this;
        }

        public InnerPrintWriter addDateHeader(String name, long date) {
            response.addDateHeader(name, date);
            return this;
        }

        public InnerPrintWriter setHeader(String name, String value) {
            response.setHeader(name, value);
            return this;
        }

        public InnerPrintWriter addHeader(String name, String value) {
            response.addHeader(name, value);
            return this;
        }

        public InnerPrintWriter setIntHeader(String name, int value) {
            response.setIntHeader(name, value);
            return this;
        }

        public InnerPrintWriter addIntHeader(String name, int value) {
            response.addIntHeader(name, value);
            return this;
        }

        public InnerPrintWriter write(int c) {
            getPrintWriter().write(c);
            return this;
        }

        public InnerPrintWriter write(String s) {
            getPrintWriter().write(s);
            return this;
        }

        public InnerPrintWriter write(String format, Object ... args) {
            getPrintWriter().format(format, args);
            return this;
        }


        @Override
        public void render(BeatContext beatContext) {

            if (writer != null) {
                writer.flush();
                writer.close();
            }

        }
    }
}	
