package com.bj58.argo.utils.converter;

/**
 * Convert String to Character.
 * 
 * @author Michael Liao (askxuefeng@gmail.com)
 */
public class CharacterConverter implements Converter<Character> {

    public Character convert(String s) {
        if (s.length()==0)
            throw new IllegalArgumentException("Cannot convert empty string to char.");
        return s.charAt(0);
    }

}
