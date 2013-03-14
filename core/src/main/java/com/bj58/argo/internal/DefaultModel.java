package com.bj58.argo.internal;

import com.bj58.argo.Model;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * MVC 中的Model, 以key,value形式存放，可以由Controller传个View
 * @author renjun
 *
 */
public class DefaultModel implements Model {

    /** Model Map */
    private Map<String, Object> data = Maps.newConcurrentMap();

    /**
     * 增加一个属性
     * @param attributeName 属性名称
     * @param attributeValue 属性值
     */
    @Override
    public Model add(String attributeName, Object attributeValue) {
        data.put(attributeName, attributeValue);
        return this;
    }



    /**
     * 根据属性名得到属性值
     * @param attributeName 属性名称
     * @return 对应的属性值
     */
    @Override
    public Object get(String attributeName) {
        return data.get(attributeName);
    }




    /**
     * Return the model map. Never returns <code>null</code>.
     * To be called by application code for modifying the model.
     */
    @Override
    public Map<String, Object> getModel() {
        return data;
    }

    /**
     * 批量增加属性
     * @param attributes
     */
    @Override
    public Model addAll(Map<String, ?> attributes) {
        data.putAll(attributes);
        return this;
    }

    /**
     * 判断是否包含属性名
     * @param attributeName 需要查找的属性
     * @return
     */
    @Override
    public boolean contains(String attributeName) {
        return data.containsKey(attributeName);
    }

}
