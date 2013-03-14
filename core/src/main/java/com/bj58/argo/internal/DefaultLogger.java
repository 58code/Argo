package com.bj58.argo.internal;


import com.bj58.argo.logs.Logger;

import javax.inject.Inject;

public class DefaultLogger implements Logger {

    private final org.slf4j.Logger logger;

    @Inject
    public DefaultLogger(org.slf4j.Logger logger) {
        this.logger = logger;
    }


    @Override
    public void debug(String msg) {
        logger.debug(msg);
    }

    @Override
    public void debug(String format, Object arg) {
        logger.debug(format, arg);
    }

    @Override
    public void debug(String msg, Throwable t) {
        logger.debug(msg, t);
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
    }

    @Override
    public void info(String format, Object arg) {
        logger.info(format, arg);
    }

    @Override
    public void info(String msg, Throwable t) {
        logger.info(msg, t);
    }

    @Override
    public void error(String msg) {
        logger.error(msg);
    }

    @Override
    public void error(String msg, Throwable t) {
        logger.error(msg, t);
    }

    @Override
    public void error(String format, Object arg) {
        logger.error(format, arg);
    }

    @Override
    public String toString() {
        return logger.toString();
    }
}
