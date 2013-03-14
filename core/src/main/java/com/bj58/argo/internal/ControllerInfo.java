package com.bj58.argo.internal;

import com.bj58.argo.ActionResult;
import com.bj58.argo.Argo;
import com.bj58.argo.ArgoController;
import com.bj58.argo.annotations.GET;
import com.bj58.argo.annotations.POST;
import com.bj58.argo.annotations.Path;
import com.bj58.argo.thirdparty.AnnotationUtils;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

public class ControllerInfo {


    final ArgoController controller;
    final Class<? extends ArgoController> clazz;
    final Path path;
    final boolean isGet;
    final boolean isPost;
    final String pathUrl;

    final Set<Annotation> annotations;


    public ControllerInfo(ArgoController controller) {
        this.controller = controller;
        clazz = controller.getClass();
        this.path = AnnotationUtils.findAnnotation(clazz, Path.class);

        boolean isGet = AnnotationUtils.findAnnotation(clazz, GET.class) != null;
        boolean isPost = AnnotationUtils.findAnnotation(clazz, POST.class) != null;

        if (!isGet && !isPost) {
            isGet = true;
            isPost = true;
        }

        this.isGet = isGet;
        this.isPost = isPost;

        this.annotations = ImmutableSet.copyOf(clazz.getAnnotations());

        String pathUrl = path == null ? "/" : path.value();

        if (pathUrl.length() == 0 || pathUrl.charAt(0) != '/')
            pathUrl = '/' + pathUrl;

        this.pathUrl = pathUrl;

    }

    public List<ActionInfo> analyze() {
        List<ActionInfo> actions = Lists.newArrayList();


        Set<Method> sets = Sets.filter(
                Sets.newHashSet(clazz.getDeclaredMethods())
                , methodFilter);
//TODO : checkMe
        for(Method method : sets){

            //todo: argo.instance
        	actions.add(new ActionInfo(this, method, Argo.instance));
        }
        	

        return actions;
    }


    public ArgoController getController() {
        return controller;
    }

    public Class<? extends ArgoController> getClazz() {
        return clazz;
    }

    public Path getPath() {
        return path;
    }

    public boolean isGet() {
        return isGet;
    }

    public boolean isPost() {
        return isPost;
    }

    public String getPathUrl() {
        return pathUrl;
    }

    public Set<Annotation> getAnnotations() {
        return annotations;
    }

    public static Predicate<Method> getMethodFilter() {
        return methodFilter;
    }

    private final static Predicate<Method> methodFilter = new Predicate<Method>() {
        @Override
        public boolean apply(Method method) {
//                if (AnnotationUtils.findAnnotation(method, Ignored.class) != null) return false;

            //TODO : 新增类别校验，如果不包含Path则不加载到ActionInfo 中
            if (AnnotationUtils.findAnnotation(method, Path.class) == null) return false;
            Class<?> returnType = method.getReturnType();
            return returnType != null
                    && ActionResult.class.isAssignableFrom(returnType)
                    && (!method.isBridge()  //TODO: 是否需要处理
                    && method.getDeclaringClass() != Object.class
                    && Modifier.isPublic(method.getModifiers()));
        }
    };

}
