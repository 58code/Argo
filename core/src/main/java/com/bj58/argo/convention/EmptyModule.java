package com.bj58.argo.convention;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * 提供一个空的Module使用
 *
 */
public class EmptyModule implements Module {

    public static final EmptyModule instance = new EmptyModule();

    @Override
    public void configure(Binder binder) {
    }
}
