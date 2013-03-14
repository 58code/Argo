package com.bj58.argo.utils.converter;

/**
 * Convert String to any given type.
 * 
 * @author Michael Liao (askxuefeng@gmail.com)
 * 
 * @param <T> Generic type of converted result.
 */
public interface Converter<T> {

    /**
     * Convert a not-null String to specified object.
     */
    T convert(String s);

}
