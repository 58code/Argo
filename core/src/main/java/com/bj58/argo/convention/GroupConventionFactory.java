package com.bj58.argo.convention;

import com.bj58.argo.internal.DefaultGroupConvention;
import com.google.common.base.Throwables;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 获得GroupConvention的工厂
 */
public class GroupConventionFactory {

	
	public static GroupConvention getGroupConvention() {

        String className = GroupConvention.annotatedGroupConventionBinder;

        Class<?> clazz = null;
        GroupConvention groupConvention;
        try {
            clazz = GroupConventionFactory.class.getClassLoader().loadClass(className);
            groupConvention = GroupConvention.class.cast(clazz);
        } catch (Exception e) {
            groupConvention = null;
        }

        if (groupConvention != null)
            return groupConvention;

        if (clazz == null || clazz.getAnnotation(GroupConventionAnnotation.class) == null)
            clazz = DefaultGroupConvention.class;

		GroupConventionAnnotation conventionAnnotation = clazz.getAnnotation(GroupConventionAnnotation.class);


        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        URL url = cl.getResource(".");

        File folder = null;
        try {
            folder = new File(url.toURI());
        } catch (URISyntaxException e) {
            Throwables.propagate(e);
        }

		return new DefaultGroupConvention(conventionAnnotation, folder);

	}
}
