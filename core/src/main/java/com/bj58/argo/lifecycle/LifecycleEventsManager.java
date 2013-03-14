package com.bj58.argo.lifecycle;

import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * TODO:这个现在不实现，留给下一版本完成。
 * 可以监控系统的性能，甚至可以实现节流器功能
 * <p/>
 * 用于管理生命周期中的事件
 * 每个事件以类的形式注册，在每个BeatContext的生命周期，会为每个事件类构造一个对象。
 *
 */
public interface LifecycleEventsManager {

    /**
     * 注册事件，绑定特定的BeatContext生命周期状态
     *
     * @param eventClass 事件类
     * @param states     绑定的生命周期状态
     */
    void registerLifecycleEvent(Class<? extends LifecycleEvent> eventClass, LifeCycleState... states);

    /**
     * 移除绑定的特定注册事件
     *
     * @param eventClass 需要移除的事件类
     * @param states     需要移除的绑定的生命周期状态
     */
    void removeLifecycleEvent(Class<? extends LifecycleEvent> eventClass, LifeCycleState... states);

    /**
     * 获得某个生命周期状态的所有事件类
     *
     * @param state 特定的生命周期状态
     * @return 需要通知的事件集合
     */
    Set<Class<? extends LifecycleEvent>> raiseEvents(LifeCycleState state);

    public static class Default implements LifecycleEventsManager {

        private Map<LifeCycleState, ImmutableLinkedHashSet<Class<? extends LifecycleEvent>>> eventsMap = Maps.newHashMap();

        public Default() {
            for (LifeCycleState state : LifeCycleState.values())
                eventsMap.put(state, new ImmutableLinkedHashSet<Class<? extends LifecycleEvent>>());

        }

        private ImmutableLinkedHashSet<Class<? extends LifecycleEvent>> get(LifeCycleState state) {
            return eventsMap.get(state);

        }

        private Set<Class<? extends LifecycleEvent>> delegate(LifeCycleState state) {
            return get(state).delegate();

        }

        @Override
        public synchronized void registerLifecycleEvent(Class<? extends LifecycleEvent> eventClass, LifeCycleState... states) {

            for (LifeCycleState state : states)
                delegate(state).add(eventClass);
        }

        @Override
        public synchronized void removeLifecycleEvent(Class<? extends LifecycleEvent> eventClass, LifeCycleState... states) {
            for (LifeCycleState state : states)
                delegate(state).remove(eventClass);

        }

        @Override
        public Set<Class<? extends LifecycleEvent>> raiseEvents(LifeCycleState state) {
            return get(state);
        }
    }

    class ImmutableLinkedHashSet<E> extends ForwardingSet<E> {

        private Set<E> delegate;

        public ImmutableLinkedHashSet() {
            this.delegate = new LinkedHashSet<E>();
        }

        @Override
        protected Set<E> delegate() {
            return delegate;
        }


        /**
         * Guaranteed to throw an exception and leave the collection unmodified.
         *
         * @throws UnsupportedOperationException always
         */
        @Override
        public final boolean add(E e) {
            throw new UnsupportedOperationException();
        }

        /**
         * Guaranteed to throw an exception and leave the collection unmodified.
         *
         * @throws UnsupportedOperationException always
         */
        @Override
        public final boolean remove(Object object) {
            throw new UnsupportedOperationException();
        }

        /**
         * Guaranteed to throw an exception and leave the collection unmodified.
         *
         * @throws UnsupportedOperationException always
         */
        @Override
        public final boolean addAll(Collection<? extends E> newElements) {
            throw new UnsupportedOperationException();
        }

        /**
         * Guaranteed to throw an exception and leave the collection unmodified.
         *
         * @throws UnsupportedOperationException always
         */
        @Override
        public final boolean removeAll(Collection<?> oldElements) {
            throw new UnsupportedOperationException();
        }

        /**
         * Guaranteed to throw an exception and leave the collection unmodified.
         *
         * @throws UnsupportedOperationException always
         */
        @Override
        public final boolean retainAll(Collection<?> elementsToKeep) {
            throw new UnsupportedOperationException();
        }

        /**
         * Guaranteed to throw an exception and leave the collection unmodified.
         *
         * @throws UnsupportedOperationException always
         */
        @Override
        public final void clear() {
            throw new UnsupportedOperationException();
        }
    }

}
