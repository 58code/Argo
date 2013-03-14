package com.bj58.argo.internal;

import com.bj58.argo.ActionResult;
import com.bj58.argo.Argo;
import com.bj58.argo.inject.ArgoSystem;
import com.bj58.argo.route.Action;
import com.bj58.argo.internal.actionresult.StaticActionResult;
import com.bj58.argo.convention.GroupConvention;
import com.bj58.argo.route.RouteBag;
import com.bj58.argo.route.RouteResult;
import com.bj58.argo.utils.Pair;
import com.bj58.argo.utils.PathUtils;
import com.bj58.argo.utils.TouchTimer;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletContext;
import java.io.File;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * 对静态文件处理，把所有静态文件名保存在set中，如何精确匹配，表明当前请求就是静态文件
 *
 */
@Singleton
public class StaticFilesAction implements Action {

    /**
     * 静态文件名set
     */
    private Set<String> staticFiles = Sets.newHashSet();

    /**
     * 不允许访问的文件或文件夹
     */
    private final Set<String> forbitPath = ImmutableSet.of("/WEB-INF");

    /**
     * 定时获取静态文件更新，但不需要另外的定时线程
     */
    private final TouchTimer timer;

    private final StaticActionResult.Factory staticFactory;

    @Inject
    public StaticFilesAction(
            ServletContext servletContext
            , StaticActionResult.Factory staticFactory
            , @ArgoSystem final Executor executor
    ) {

        this.staticFactory = staticFactory;

        final File staticResourcesFolder = new File(servletContext.getRealPath("/"));


        Runnable findFiles = new Runnable() {
            @Override
            public void run() {
                staticFiles = findFiles(staticResourcesFolder, staticFiles.size(), forbitPath);
            }
        };

        timer = TouchTimer
                .build(60 * 1000, findFiles, executor);

        timer.immediateRun();
    }

    @Override
    public double order() {
        return 100d;
    }

    @Override
    public RouteResult matchAndInvoke(RouteBag bag) {
        return RouteResult.invoked(match(bag));
    }

    private ActionResult match(RouteBag bag) {

        String simplyPath = bag.getSimplyPath();

        if (!exist(simplyPath)) return ActionResult.NULL;

        return staticFactory.create(simplyPath);

    }

    private boolean exist(String url) {
        timer.touch();
        return staticFiles.contains(url);
    }

    Set<String> findFiles(File directory, int cap, Set<String> forbitPath) {

        Set<String> staticFiles = new HashSet<String>(cap);

        Deque<Pair<File, String>> dirs = Lists.newLinkedList();

        dirs.add(Pair.build(directory, "/"));

        while (dirs.size() > 0) {
            Pair<File, String> pop = dirs.pop();

            File[] files = pop.getKey().listFiles();

            if (files == null)
                continue;

            for (File file : files) {
                String name = pop.getValue() + file.getName();

                if (forbitPath.contains(name))
                    continue;

                if (file.isDirectory()) {
                    dirs.push(Pair.build(file
                            , name + '/'));
                    continue;
                }

                staticFiles.add(name);
            }
        }

        return staticFiles;
    }

}
