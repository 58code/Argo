package com.bj58.argo;

import com.bj58.argo.internal.DefaultModel;
import com.google.inject.ImplementedBy;

import java.util.Map;

/**
 * Model，MVC中的M，
 * 用于将Controller中的数据传递给View
 *
 */
@ImplementedBy(DefaultModel.class)
public interface Model {

    /**
     * 增加一个属性
     * @param attributeName 属性名称
     * @param attributeValue 属性值
     */
    Model add(String attributeName, Object attributeValue);

    /**
     * 根据属性名得到属性值
     * @param attributeName 属性名称
     * @return 对应的属性值
     */
    Object get(String attributeName);

    /**
     * Return the model map. Never returns <code>null</code>.
     * To be called by application code for modifying the model.
     */
    Map<String, Object> getModel();

    /**
     * 批量增加属性
     * @param attributes 属性map
     */
    Model addAll(Map<String, ?> attributes);

    /**
     * 判断是否包含属性名
     * @param attributeName 需要查找的属性
     * @return 是否包含
     */
    boolean contains(String attributeName);
}
