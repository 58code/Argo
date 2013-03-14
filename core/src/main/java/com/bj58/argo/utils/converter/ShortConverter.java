package com.bj58.argo.utils.converter;

/**
 * Convert String to Short.
 * 
 * @author Michael Liao (askxuefeng@gmail.com)
 */
public class ShortConverter implements Converter<Short> {

    public Short convert(String s) {
        return Short.parseShort(s);
    }

}
