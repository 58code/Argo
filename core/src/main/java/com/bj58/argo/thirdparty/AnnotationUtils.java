/*
*  Copyright Beijing 58 Information Technology Co.,Ltd.
*
*  Licensed to the Apache Software Foundation (ASF) under one
*  or more contributor license agreements.  See the NOTICE file
*  distributed with this work for additional information
*  regarding copyright ownership.  The ASF licenses this file
*  to you under the Apache License, Version 2.0 (the
*  "License"); you may not use this file except in compliance
*  with the License.  You may obtain a copy of the License at
*
*        http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/
package com.bj58.argo.thirdparty;

import com.google.common.base.Preconditions;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class AnnotationUtils {

    /** The attribute name for annotations with a single element */
    static final String VALUE = "value";

    /**annotation 的缓存  **/
    private static final Map<Class, Boolean> annotatedInterfaceCache = new WeakHashMap<Class, Boolean>();


    /**
     * 获取 给定{@link Class}的单个{@link Annotation Annotations}。
     * 当根据类型从方法中未找到符合条件的Annotation时，遍历该类的父类或者接口进行查找。
     * @param clazz 被查询的class
     * @param annotationType 查找的annotation类型
     * @return 查找到的annotation，或者<code>null</code>
     */
    public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType) {
        Preconditions.checkNotNull(clazz, "Class must not be null");
        A annotation = clazz.getAnnotation(annotationType);
        if (annotation != null) {
            return annotation;
        }
        for (Class<?> ifc : clazz.getInterfaces()) {
            annotation = findAnnotation(ifc, annotationType);
            if (annotation != null) {
                return annotation;
            }
        }
        if (!Annotation.class.isAssignableFrom(clazz)) {
            for (Annotation ann : clazz.getAnnotations()) {
                annotation = findAnnotation(ann.annotationType(), annotationType);
                if (annotation != null) {
                    return annotation;
                }
            }
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass == null || superClass == Object.class) {
            return null;
        }
        return findAnnotation(superClass, annotationType);
    }

    /**
     * 获取 给定{@link java.lang.reflect.Method}的单个{@link java.lang.annotation.Annotation Annotations}。
     * 当根据类型从方法中未找到符合条件的Annotation时，查找该方法所在类所实现的接口，看接口是否存在注解。
     * @param method 被查询的method
     * @param annotationType 查找的annotation类型
     * @return 查找到的annotation
     */
    public static <A extends Annotation> A findAnnotation(Method method, Class<A> annotationType) {
        A annotation = getAnnotation(method, annotationType);
        Class<?> cl = method.getDeclaringClass();
        if (annotation == null) {
            annotation = searchOnInterfaces(method, annotationType, cl.getInterfaces());
        }
        while (annotation == null) {
            cl = cl.getSuperclass();
            if (cl == null || cl == Object.class) {
                break;
            }
            try {
                Method equivalentMethod = cl.getDeclaredMethod(method.getName(), method.getParameterTypes());
                annotation = getAnnotation(equivalentMethod, annotationType);
                if (annotation == null) {
                    annotation = searchOnInterfaces(method, annotationType, cl.getInterfaces());
                }
            }
            catch (NoSuchMethodException ex) {
                // We're done...
            }
        }
        return annotation;
    }

    /**
     * 获取 给定{@link Method}的单个{@link Annotation Annotations}。
     * 当根据类型从方法中未找到符合条件的Annotation时，可以通过查找其全部声明并进行类型比较处理。
     * @param method 被查询的method
     * @param annotationType 查找的annotation类型
     * @return 查找到的annotation
     */
    public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {

        Method resolvedMethod = BridgeMethodResolver.findBridgedMethod(method);
        A ann = resolvedMethod.getAnnotation(annotationType);
        if (ann == null) {
            for (Annotation metaAnn : resolvedMethod.getAnnotations()) {
                ann = metaAnn.annotationType().getAnnotation(annotationType);
                if (ann != null) {
                    break;
                }
            }
        }
        return ann;
    }


    private static <A extends Annotation> A searchOnInterfaces(Method method, Class<A> annotationType, Class[] ifcs) {
        A annotation = null;
        for (Class<?> iface : ifcs) {
            if (isInterfaceWithAnnotatedMethods(iface)) {
                try {
                    Method equivalentMethod = iface.getMethod(method.getName(), method.getParameterTypes());
                    annotation = getAnnotation(equivalentMethod, annotationType);
                }
                catch (NoSuchMethodException ex) {
                    // Skip this interface - it doesn't have the method...
                }
                if (annotation != null) {
                    break;
                }
            }
        }
        return annotation;
    }


    private static boolean isInterfaceWithAnnotatedMethods(Class<?> iface) {
        synchronized (annotatedInterfaceCache) {
            Boolean flag = annotatedInterfaceCache.get(iface);
            if (flag != null) {
                return flag;
            }
            boolean found = false;
            for (Method ifcMethod : iface.getMethods()) {
                if (ifcMethod.getAnnotations().length > 0) {
                    found = true;
                    break;
                }
            }
            annotatedInterfaceCache.put(iface, found);
            return found;
        }
    }

    /**
     * Retrieve the <em>value</em> of a named Annotation attribute, given an annotation instance.
     * @param annotation the annotation instance from which to retrieve the value
     * @param attributeName the name of the attribute value to retrieve
     * @return the attribute value, or <code>null</code> if not found
     * @see #getValue(Annotation)
     */
    public static Object getValue(Annotation annotation, String attributeName) {
        try {
            Method method = annotation.annotationType().getDeclaredMethod(attributeName, new Class[0]);
            return method.invoke(annotation);
        }
        catch (Exception ex) {
            return null;
        }
    }

    /**
     * Retrieve the <em>value</em> of the <code>&quot;value&quot;</code> attribute of a
     * single-element Annotation, given an annotation instance.
     * @param annotation the annotation instance from which to retrieve the value
     * @return the attribute value, or <code>null</code> if not found
     * @see #getValue(Annotation, String)
     */
    public static Object getValue(Annotation annotation) {
        return getValue(annotation, VALUE);
    }
}
