package com.bj58.argo.logs;

import com.bj58.argo.internal.DefaultLoggerFactory;
import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultLoggerFactory.class)
public interface LoggerFactory {

    /**
     *
     * @param name the name of the Logger to return
     * @return a Logger instance
     */
    Logger getLogger(String name);

    Logger getLogger(Class<?> clazz);
}
