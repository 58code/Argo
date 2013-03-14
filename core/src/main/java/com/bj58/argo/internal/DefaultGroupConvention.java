package com.bj58.argo.internal;

import com.bj58.argo.ArgoController;
import com.bj58.argo.ArgoException;
import com.bj58.argo.convention.*;
import com.bj58.argo.utils.ClassUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.inject.Binder;
import com.google.inject.Module;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@GroupConventionAnnotation
public class DefaultGroupConvention implements GroupConvention {

    private static final String GROUP_CONFIG_FOLDER = "groupConfigFolder";
    public static final String GROUP_LOG_FOLDER = "groupLogFolder";

    private static final String PROJECT_ID = "projectId";

    public static final String PACKAGES_PREFIX = "groupPackagesPrefix";
    
    private final File folder;
    private final GroupConfig groupConfig;
    private final ProjectConfig projectConfig;
    
    public DefaultGroupConvention(GroupConventionAnnotation groupConventionAnnotation, File folder) {

        this.folder = folder;

        ProjectConvention projectConvention = parseProjectConvention(groupConventionAnnotation);
        Map<String, String> configInfoMap = parseGroupConventionPath(groupConventionAnnotation, projectConvention);

        groupConfig = parseGroupConfig(groupConventionAnnotation, configInfoMap);

        projectConfig = parseProjectConfig(projectConvention, groupConventionAnnotation);

    }


    @Override
    public GroupConfig group() {
        return groupConfig;
    }

    @Override
    public ProjectConfig currentProject() {
        return projectConfig;
    }

    /**
     * 解析Group规范
     * @param groupConventionAnnotation 组织级注解
     * @return Group规范
     */
    private GroupConfig parseGroupConfig(GroupConventionAnnotation groupConventionAnnotation
            , Map<String, String> configInfoMap) {

        String configPath = configInfoMap.get(GROUP_CONFIG_FOLDER);
        String logPath = configInfoMap.get(GROUP_LOG_FOLDER);

        Module module =  EmptyModule.class.isAssignableFrom(groupConventionAnnotation.groupModule())
                ? EmptyModule.instance
                : newInstanceByClass(groupConventionAnnotation.groupModule(), "");

        return new DefaultGroupConfig(getDir(configPath), getDir(logPath), module);

    }

    private ProjectConfig parseProjectConfig(ProjectConvention projectConvention,
                                             GroupConventionAnnotation groupConventionAnnotation) {

        Set<Class<? extends ArgoController>> controllersClasses = parseControllers(groupConventionAnnotation);

        return new DefaultProjectConfig(projectConvention.id(), controllersClasses, projectConvention);

    }


    private <T> T newInstanceByClass(Class<T> clazz, String message) {
        try {
            return clazz.newInstance();
        }  catch (Exception e) {
            throw ArgoException.raise(message, e);
        }
    }

    /**
     * 解析项目规范
     * @param groupConventionAnnotation 组织级注解
     * @return 项目规范信息
     */
    ProjectConvention parseProjectConvention(GroupConventionAnnotation groupConventionAnnotation) {

        String className = groupConventionAnnotation.projectConventionClass();

        Class<?> clazz;
        try {
            clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            return new ProjectConvention() {
                @Override
                public String id() {
                    return "";
                }

                @Override
                public void configure(Binder binder) {
                }
            };
        }

        ProjectConvention projectConvention = null;
        if(ProjectConvention.class.isAssignableFrom(clazz)){
            //TODO:需要描述，为何抛错，这个类必须继承ProjectConvention并实现,并且构造函数必须无参。
            projectConvention = ProjectConvention.class.cast(newInstanceByClass(clazz, ""));
        }

        if (projectConvention != null)
            return projectConvention;

        throw ArgoException
                .raise(String.format("Class %s not implement ProjectConvention!", className));

    }

    private Map<String, String> parseGroupConventionPath(GroupConventionAnnotation groupConventionAnnotation
            , ProjectConvention projectConvention) {

        Map<String, String> paths = ImmutableMap.<String, String>builder()
                .put(PACKAGES_PREFIX, groupConventionAnnotation.groupPackagesPrefix())
                .put(PROJECT_ID, projectConvention.id())
                .put(GROUP_CONFIG_FOLDER, groupConventionAnnotation.groupConfigFolder())
                .put(GROUP_LOG_FOLDER, groupConventionAnnotation.groupLogFolder())
                .build();

        return matchPath(paths);

    }

    static Map<String, String> matchPath(Map<String, String> paths) {
        Map<String, String> values = Maps.newHashMap();
        Map<String, String> templates = Maps.newHashMap();
        Map<String, String> result = Maps.newHashMap();

        classify(paths, values, templates);

        while (values.size() > 0) {
            values = migrate(values, templates, result);
        }

        if (templates.size() > 0)
            throw ArgoException.newBuilder("GroupConventionAnnotation contains nested expression")
                    .addContextVariables(templates)
                    .build();

        return result;

    }

    /**
     * 将定值数据保存到结果数据集合，并将模板数据用定值数据进行替换，并返回替换后的定值
     * @param values 定值数据集合
     * @param templates 模板数据集合
     * @param result 结果数据集合
     * @return 由模板替换后生成的定值数据
     */
    static Map<String, String> migrate(Map<String, String> values, Map<String, String> templates, Map<String, String> result) {
        result.putAll(values);


        for (Map.Entry<String, String> templateItem : templates.entrySet()) {
            String v = templateItem.getValue();
            for (Map.Entry<String, String> valueItem : values.entrySet()) {
                String exp = '{' + valueItem.getKey() + '}';

                if (v.contains(exp))
                    v = v.replace(exp, valueItem.getValue());

            }

            if (!v.equals(templateItem.getValue()))
                templateItem.setValue(v);
        }

        Map<String, String> newValues = Maps.newHashMap();
        Map<String, String> newTemplates = Maps.newHashMap();
        classify(templates, newValues, newTemplates);

        templates.clear();
        templates.putAll(newTemplates);


        return newValues;

    }


    /**
     * 分类数据，若路径中存在"{"，刚归类到模板数据，否则归类到定值数据
     * @param paths 路径数据集合
     * @param values 定值数据集合
     * @param templates 模板数据集合
     */
    static void classify(Map<String, String> paths, Map<String, String> values, Map<String, String> templates) {
        for (Map.Entry<String, String> entry : paths.entrySet()) {
            if (entry.getValue().contains("{"))
                templates.put(entry.getKey(), entry.getValue());
            else
                values.put(entry.getKey(), entry.getValue());

        }
    }

    File getDir(String path) {

        boolean isSub = path.charAt(0) == '.';



        File dir = isSub ? new File(folder, path) : new File(path);

        if (dir.isDirectory())
            return dir;

        if (dir.exists())
            throw ArgoException.raise(String.format("File %s has exist, but not directory.", path));

        if (!dir.mkdirs())
            throw ArgoException.raise(String.format("Failed to getLogger directory %s.", path));

        return dir;
    }

    @SuppressWarnings("unchecked")
    Set<Class<? extends ArgoController>> parseControllers(GroupConventionAnnotation groupConventionAnnotation) {

        Set<Class<?>> classSet = ClassUtils.getClasses(groupConventionAnnotation.groupPackagesPrefix());

        Pattern controllerPattern = Pattern.compile(groupConventionAnnotation.controllerPattern());


        ImmutableSet.Builder<Class<? extends ArgoController>> builder = ImmutableSet.builder();

        for (Class<?> clazz : classSet)
            if (applyArgoController(clazz, controllerPattern))
                builder
                    .add((Class<? extends ArgoController>) clazz)
                    .build();

        return builder.build();
    }

    boolean applyArgoController(Class<?> clazz, Pattern controllerPattern) {

        return ArgoController.class.isAssignableFrom(clazz)
                && controllerPattern.matcher(clazz.getName()).matches()
                && !Modifier.isInterface(clazz.getModifiers())
                && !Modifier.isAbstract(clazz.getModifiers())
                && Modifier.isPublic(clazz.getModifiers());
    }


    private class DefaultGroupConfig implements GroupConfig {

        private final File configFolder;
        private final File logFolder;
        private final Module module;

        private DefaultGroupConfig(File configFolder, File logFolder, Module module) {
            this.configFolder = configFolder;
            this.logFolder = logFolder;
            this.module = module;
        }

        @Override
        public File configFolder() {
            return configFolder;
        }

        @Override
        public File logFolder() {
            return logFolder;
        }

        @Override
        public Module module() {
            return module;
        }
    }

    private class DefaultProjectConfig implements ProjectConfig {

        private final String id;
        private final Set<Class<? extends ArgoController>> controllerClasses;
        private final Module module;
//        private final File configFolder;
//        private final File logFolder;
//        private final File viewFolder;
//        private final File staticResourcesFolder;

        private DefaultProjectConfig(String id, Set<Class<? extends ArgoController>> controllerClasses, Module module) {
            this.id = id;
            this.controllerClasses = controllerClasses;
            this.module = module;
        }

        @Override
        public String id() {
            return id;
        }

        @Override
        public Set<Class<? extends ArgoController>> controllerClasses() {
            return controllerClasses;
        }

        @Override
        public Module module() {
            return module;
        }
    }



}