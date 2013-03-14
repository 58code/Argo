package com.bj58.argo.lifecycle;

/**
     * 当前调用的状态枚举
     */
    public enum LifeCycleState {

        /**
         * 开始
         */
        START,

        /**
         * 路由
         */
        ROUTE,

        /**
         * 函数执行
         */
        INVOKE,

        /**
         * 页面呈现
         */
        RENDER,

        /**
         * 析构处理
         */
        DESTRUCT

    }