package com.bj58.argo.convention;

import com.google.inject.Module;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 这是一个组织级的约定规则
 * <p/>
 * convention over configuration 约定优于配置
 * http://zh.wikipedia.org/zh-cn/%E7%BA%A6%E5%AE%9A%E4%BC%98%E4%BA%8E%E9%85%8D%E7%BD%AE
 *
 * @author renjun
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GroupConventionAnnotation {


    /**
     * 约定组织中所有项目必须实现的类,
     * @see com.bj58.argo.convention.GroupConvention.ProjectConfig
     *
     * 该类实现项目的Id用于项目的唯一编号，并便于管理
     * @see com.bj58.argo.convention.GroupConvention.ProjectConfig#id()
     *
     * 同时实现module，
     * @see com.bj58.argo.convention.GroupConvention.ProjectConfig#module()
     * 用于项目的Guice注入配置,优先级低于组织的module并可能会被组织级的module覆盖
     *
     */
    String projectConventionClass() default "com.bj58.argo.ProjectConfigBinder";

    /**
     * group级的注入Module
     * 可以覆盖项目级的module，保证组织的策略实施
     *
     */
    Class<? extends Module> groupModule() default EmptyModule.class;

    /**
     * 配置文件夹位置
     *
     * @return 配置文件夹的根目录
     */
    String groupConfigFolder() default "/opt/argo";

    String groupLogFolder() default "{groupConfigFolder}/log";


    /**
     * group包的前缀，Argo将只扫描该前缀下的类
     * @return group包的前缀
     */
    String groupPackagesPrefix() default "com.bj58.argo";

    /**
     * controller的类名强制检查约定
     * 只有符合匹配条件的controller才能被Argo管理，保证组织级代码风格一致
     *
     */
    String controllerPattern() default ".*\\.controllers\\..*Controller";
    
}
