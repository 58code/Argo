package com.bj58.argo.utils;

/**
 * @author renjun
 */
public class Pair<K, V> {

    public static <K, V> Pair<K, V> build(K k, V v) {
        return new Pair<K, V>(k, v);
    }

    private final K key;
    private final V value;

    public Pair(K k, V v) {
        this.key = k;
        this.value = v;
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
     return this.value;
    }

    public java.lang.String toString() {
        return "Pair " + key + ": " + value;

    }
}
