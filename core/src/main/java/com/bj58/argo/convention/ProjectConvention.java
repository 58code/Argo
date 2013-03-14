package com.bj58.argo.convention;

import com.google.inject.Module;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 每个项目需要实现的项目级约定
 * @see com.bj58.argo.convention.GroupConventionAnnotation#projectConventionClass()
 */
public interface ProjectConvention extends Module{

    /**
     * 提供项目的编号
     */
    String id();

}
