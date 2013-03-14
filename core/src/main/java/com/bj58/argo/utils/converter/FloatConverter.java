package com.bj58.argo.utils.converter;

/**
 * Convert String to Float.
 * 
 * @author Michael Liao (askxuefeng@gmail.com)
 */
public class FloatConverter implements Converter<Float> {

    public Float convert(String s) {
        return Float.parseFloat(s);
    }

}
