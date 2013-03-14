package com.bj58.argo.logs;

import com.bj58.argo.internal.DefaultLoggerFactory;
import com.google.inject.ImplementedBy;

/**
 * 对日志文件进行设置
 * 默认采用log4j
 *
 */
@ImplementedBy(DefaultLoggerFactory.DefaultLog4jConfigure.class)
public interface LogConfigure {

    void configure();


}
