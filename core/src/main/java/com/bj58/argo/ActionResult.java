package com.bj58.argo;

/**
 * 所有Action的返回结果
 *
 */
public interface ActionResult {

    public final static ActionResult NULL = null;

    /**
     * 用于生成显示页面
     *
     * @param beatContext 需要渲染的上下文
     */
    void render(BeatContext beatContext);
}
