package com.bj58.argo.lifecycle;

import com.bj58.argo.BeatContext;
import com.google.common.collect.Queues;

import java.util.Deque;

public class Lifecycle {

    /**
     * 状态
     */
//    private LifeCycleState state = LifeCycleState.START;

    /**
     * 创建时间
     */
    private final long birthTime = System.currentTimeMillis();

    /**
     * 析构函数栈
     */
    private final Deque<Destruct> destructs = Queues.newArrayDeque();

    /**
     * push 一个当前请求的析构处理
     * @param destruct 析构处理
     */
    public void pushDestruct(Destruct destruct) {
        destructs.push(destruct);
    }

    /**
     * pop 出一个当前析构处理
     * @return 栈上第一个析构处理 <tt>null</tt> 栈上没数据。
     */
    public Destruct pollDestruct() {
        return destructs.poll();
    }

    public long getBirthTime() {
        return birthTime;
    }

    /**
     * 当前调用
     */
    public interface Destruct {
        void clean(BeatContext beatContext);
    }


}
