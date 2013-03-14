package com.bj58.argo.lifecycle;


public interface LifecycleEvent {
    void before(LifeCycleState state);

    void after(LifeCycleState state);
}