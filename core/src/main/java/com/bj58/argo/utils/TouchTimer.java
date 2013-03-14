package com.bj58.argo.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * 一个不需要定时线程的定时器，减少线程量
 *
 * @author renjun
 */
public class TouchTimer {

    private final long interval;

    private final Runnable run;

    private final Executor executor;

    private volatile long lastTime = 0;
    private AtomicBoolean isRun = new AtomicBoolean(false);

    public static TouchTimer build(long interval, Runnable run, Executor executor) {
        return new TouchTimer(interval, run, executor);
    }

    public TouchTimer(long interval, Runnable run, Executor executor) {
        this.interval = interval;
        this.run = run;
        this.executor = executor;
    }

    public void touch() {

        long time = System.currentTimeMillis();
        if (isRun.get())
            return;

        if (time - lastTime < interval)
            return;

        execute();

        lastTime = time;

    }

    public void execute() {

        if(!isRun.compareAndSet(false, true))
            return;

        executor.execute(new Runnable() {
            @Override
            public void run() {
                immediateRun();
            }
        });

    }

    public void immediateRun() {
        try {
            if (isRun.get())
                return;

            executor.execute(run);
        } finally {
            lastTime = System.currentTimeMillis();
            isRun.set(false);
        }
    }
}
