package com.bj58.argo;

import com.bj58.argo.convention.GroupConvention;
import com.bj58.argo.inject.ArgoModule;
import com.bj58.argo.logs.Logger;
import com.bj58.argo.logs.LoggerFactory;
import com.bj58.argo.servlet.ArgoDispatcher;
import com.bj58.argo.utils.OnlyOnceCondition;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * Argo是整个项目的上帝，是整个项目的根对象(root)
 *
 * 负责环境的管理和各个对象的创建，从Argo可以抵达项目的每个对象
 *
 * @author renjun
 */
public class Argo {

    public static final Argo instance = new Argo();

    private Injector injector;
    private ServletContext servletContext;
    private GroupConvention groupConvention;

    private Set<Class<? extends ArgoController>> controllerClasses;

    private File currentFolder;
    private LoggerFactory loggerFactory;
    private Logger logger;

    private final OnlyOnceCondition onlyOnce = OnlyOnceCondition
            .create("Argo has been initialized.");

    private Argo() {
    }


    public ArgoDispatcher init(ServletContext servletContext, GroupConvention groupConvention) {
        onlyOnce.check();

        servletContext.log("initializing Argo...");

        this.servletContext = servletContext;
        this.groupConvention = groupConvention;
        this.controllerClasses = groupConvention.currentProject().controllerClasses();
        this.currentFolder = innerCurrentFolder();

        List<Module> modules = Lists.newArrayList();
        modules.add(new ArgoModule(this));


        Module groupModule = groupConvention.group().module();
        if (null != groupModule)
            modules.add(groupModule);

        Module projectModule = groupConvention.currentProject().module();
        if (null != projectModule)
            modules.add(projectModule);

        servletContext.log("preparing an injector");
        this.injector = buildInjector(modules);
        servletContext.log("injector completed");

        this.loggerFactory = getInstance(LoggerFactory.class);

        this.logger = getLogger("ARGO");

        logger.info("preparing an argo dispatcher");
        this.argoDispatcher = getInstance(ArgoDispatcher.class);
        logger.info("the argo dispatcher completed");

        logger.info("argo initialized");

        return argoDispatcher;
    }


    private Injector buildInjector(List<Module> modules) {
        return Guice.createInjector(modules);
    }

    /**
     * Returns the appropriate instance for the given injection type.
     *
     * @param type the given injection type
     * @return the appropriate instance
     */
    public <T> T getInstance(Class<T> type) {
        return injector().getInstance(type);
    }

    /**
     * Injects dependencies into the fields and methods of {@code instance}.
     * Ignores the presence of absence of an injectable constructor.
     *
     * @param instance to inject members on
     */
    public <T> T injectMembers(T instance) {
        injector().injectMembers(instance);
        return instance;
    }

    /**
     * an injector singleton instance.
     *
     * @return 注入器
     */
    public Injector injector() {
        return injector;
    }

    /**
     * 公用线程池
     *
     * @return 公用线程池
     */
    public Executor commonExecutor() {
        return getInstance(Executor.class);
    }

    /**
     * 项目的ServletContext
     *
     * @return 项目的ServletContext
     */
    public ServletContext servletContext() {
        return this.servletContext;
    }

    private volatile ArgoDispatcher argoDispatcher;

    /**
     * Servlet的适配器，负责url转发
     *
     * @return ArgoDispatcher
     */
    public ArgoDispatcher argoDispatcher() {
        return argoDispatcher;
    }

    /**
     * 当前的Request
     *
     * @return 当前的Request
     */
    public HttpServletRequest currentRequest() {
        return argoDispatcher().currentRequest();
    }

    public HttpServletResponse currentResponse() {
        return argoDispatcher().currentResponse();
    }

    /**
     * 当前请求的上下文
     *
     * @return 当前请求的上下文
     */
    public BeatContext beatContext() {
        return argoDispatcher().currentBeatContext();
    }

    /**
     * 组织级策略
     *
     * @return 组织级策略
     */
    public GroupConvention groupConvention() {
        return this.groupConvention;
    }

    /**
     * 项目所管理的所有Controller类集合
     *
     * @return 所有Controller类集合
     */
    public Set<Class<? extends ArgoController>> getControllerClasses() {
        return controllerClasses;
    }

    /**
     * 根据名字获得logger
     *
     * @param name 需要获得的logger名字
     * @return 对应名字的logger
     */
    public Logger getLogger(String name) {
        return loggerFactory.getLogger(name);
    }

    /**
     * 根据类获得logger
     * @param clazz 类
     * @return logger
     */
    public Logger getLogger(Class<?> clazz) {
        return loggerFactory.getLogger(clazz);
    }

    public Logger getLogger() {
        return this.logger;
    }

    /**
     * Classloader所在文件夹
     * @return 启动文件夹 classloader
     */
    public File currentFolder() {
        return currentFolder;
    }

    private File innerCurrentFolder() {

        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            URL url = cl.getResource(".");
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            throw ArgoException.raise(e);
        }
    }

}
