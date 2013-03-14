package com.bj58.argo;

/**
 * 所有的Controller必须实现的接口
 *
 */
public interface ArgoController {

    /**
     * Controller被injector实例化后，将立即调用本方法进行初始化，代替构造函数
     */
    void init();

}
