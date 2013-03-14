package com.bj58.argo.convention;


import com.bj58.argo.ArgoController;
import com.google.inject.Module;

import java.io.File;
import java.util.Set;

/**
 * 这是一个组织级的约定规则
 * <p/>
 * convention over configuration 约定优于配置
 * http://zh.wikipedia.org/zh-cn/%E7%BA%A6%E5%AE%9A%E4%BC%98%E4%BA%8E%E9%85%8D%E7%BD%AE
 *
 * @author renjun
 */
public interface GroupConvention {

    /**
     * 用于自定义的组织的策略的类
     * 如果需要自己实现组织级的策略，需要实现这个各类
     * 可以在该类上配置注解
     * @see GroupConventionAnnotation
     * 也可以实现接口
     * @see GroupConvention
     *
     * <br/>
     *
     * 接口的优先级比annotation高
     *
     * <br/>
     *
     * 一个组织应该只提供一个实现类的jar包给其他项目约束。
     *
     */
    public final static String annotatedGroupConventionBinder = "com.bj58.argo.GroupConventionBinder";

    /**
     * 组织级配置
     * @return 组织级配置
     */
    GroupConfig group();

    /**
     * 项目级配置
     * @return 项目级配置
     */
    ProjectConfig currentProject();



    /**
     * 组织级配置文件，隶属于
     * @see com.bj58.argo.convention.GroupConvention#group()
     *
     * 为便于运维管理与维护及安全，需要将项目的配置文件日志单独区分开来，并统一管理
     */
    public interface GroupConfig {

        /**
         * 公司级的配置文件路径，项目根据各自id对应相应文件夹放置配置文件
         * @return 公司级的配置文件路径
         */
        File configFolder();

        /**
         * 公司级的日志文件路径，项目根据各自id对应相应文件夹写日志文件
         * @return 公司级的日志文件路径
         */
        File logFolder();

        /**
         * 组织级的Guice注入module配置，
         * 在项目级的module后面实现，保证组织级的策略实现
         *
         * @return 组织级的Guice注入module配置
         */
        Module module();

    }

    /**
     * 项目级配置
     * @see com.bj58.argo.convention.GroupConvention#currentProject()
     */
    public interface ProjectConfig {

        /**
         * 项目的Id，用于组织和运维进行统一管理
         */
        String id();

        /**
         * 项目所有的Controller集合
         *
         */
        Set<Class<? extends ArgoController>> controllerClasses();

        /**
         * 项目级的注入配置
         *
         */
        Module module();
    }


}
