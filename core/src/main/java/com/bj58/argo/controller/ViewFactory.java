package com.bj58.argo.controller;

import com.bj58.argo.ActionResult;
import com.bj58.argo.internal.VelocityViewFactory;
import com.google.inject.ImplementedBy;

/**
 * 提供View工厂，默认采用velocity模板
 */
@ImplementedBy(VelocityViewFactory.class)
public interface ViewFactory {
    ActionResult create(String viewName);
}
