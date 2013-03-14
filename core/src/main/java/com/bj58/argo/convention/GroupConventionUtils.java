package com.bj58.argo.convention;

import com.bj58.argo.ArgoException;
import com.google.common.base.Strings;

import java.io.File;

/**
 * GroupConvetion工具
 */
public class GroupConventionUtils {

    private GroupConventionUtils() {}

    public static File configFolder(GroupConvention groupConvention) {
        File file = groupConvention.group().configFolder();
        String projectId = groupConvention.currentProject().id();

        file = Strings.isNullOrEmpty(projectId)
                ? file
                : new File(file, projectId);

        return buildDirectory(file);
    }

    public static File logFolder(GroupConvention groupConvention) {
        File file = groupConvention.group().logFolder();
        String projectId = groupConvention.currentProject().id();

        file = Strings.isNullOrEmpty(projectId)
                ? file
                : new File(file, projectId);

        return buildDirectory(file);
    }

    private static File buildDirectory(File file) {
        if (file.isDirectory())
            return file;

        if (file.exists())
            throw ArgoException.raise(String.format("File %s has exist, but not directory.", file));

        if (!file.mkdirs())
            throw ArgoException.raise(String.format("Failed to getLogger directory %s.", file));

        return file;
    }
}
