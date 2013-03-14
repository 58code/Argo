package com.bj58.argo.utils;

import com.bj58.argo.ArgoException;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 保证只执行一次
 * @author renjun
 */
public class OnlyOnceCondition {

    public static OnlyOnceCondition create(String message) {
        return new OnlyOnceCondition(message);
    }

    private final String message;
    private OnlyOnceCondition(String message) {
        this.message = message;
    }

    private final AtomicBoolean hasChecked = new AtomicBoolean(false);
    public void check() {
        if (!hasChecked.compareAndSet(false, true))
            throw ArgoException
                    .newBuilder(message)
                    .build();
    }

}
