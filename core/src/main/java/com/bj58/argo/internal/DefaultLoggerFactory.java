package com.bj58.argo.internal;

import com.bj58.argo.Argo;
import com.bj58.argo.ArgoException;
import com.bj58.argo.convention.GroupConventionUtils;
import com.bj58.argo.logs.LogConfigure;
import com.bj58.argo.logs.Logger;
import com.bj58.argo.logs.LoggerFactory;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.io.Closeables;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Map;
import java.util.Properties;

@Singleton
public class DefaultLoggerFactory implements LoggerFactory {

    private static final String configFileName = "log4j.properties";

    Map<String, Logger> loggerMap = Maps.newHashMap();

    @Inject
    public DefaultLoggerFactory(LogConfigure logConfigure) {
        logConfigure.configure();
    }

    @Override
    public Logger getLogger(String name) {

        Logger logger = null;

        synchronized (this) {
            logger = loggerMap.get(name);
            if (logger != null)
                return logger;

            org.slf4j.Logger slf4jLogger = org.slf4j.LoggerFactory.getLogger(name);

            logger = new DefaultLogger(slf4jLogger);

            loggerMap.put(name, logger);
        }

        return logger;
    }

    @Override
    public Logger getLogger(Class<?> clazz) {

        return getLogger(clazz.getName());

    }

    @Singleton
    public static class DefaultLog4jConfigure implements LogConfigure {

        private Argo argo;

        @Inject
        public DefaultLog4jConfigure(Argo argo) {
            this.argo = argo;
        }

        @Override
        public void configure() {

            Properties properties = configureFile();

            if (properties == null)
                properties = defaultProperties();

            configure(properties);

        }

        protected Properties configureFile() {
            File configFolder = GroupConventionUtils.configFolder(argo.groupConvention());

            File configFile =  new File(configFolder, configFileName);

            if (!configFile.exists())
                return null;

            Properties properties = new Properties();
            Reader reader = null;
            try {
                reader = new FileReader(configFile);
                properties.load(reader);
            } catch (Exception e) {
                throw ArgoException.raise("fail to init log config file.", e);
            } finally{
                Closeables.closeQuietly(reader);
            }

            if (!properties.containsKey("log4j.appender.file.File"))
                properties.put("log4j.appender.file.File", defaultLogFile());

            return properties;

        }

        protected Properties defaultProperties() {

            Properties properties = new Properties();

            properties.put("log4j.rootLogger", "INFO, file");
            properties.put("log4j.appender.file.File", defaultLogFile());

            properties.put("log4j.appender.file.DatePattern","'.'yyyy-MM-dd");
            properties.put("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
            properties.put("log4j.appender.stdout.Target", "System.out");
            properties.put("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
            properties.put("log4j.appender.stdout.layout.ConversionPattern", "%m%n");
            properties.put("log4j.appender.file", "org.apache.log4j.DailyRollingFileAppender");
            properties.put("log4j.appender.file.Append", "true");
            properties.put("log4j.appender.file.Threshold", "INFO");
            properties.put("log4j.appender.file.layout", "org.apache.log4j.PatternLayout");
            properties.put("log4j.appender.file.layout.ConversionPattern", "%d{ABSOLUTE} %5p %c{1}:%L - %m%n");

            return properties;
        }

        private String defaultLogFile() {

            File logFolder = GroupConventionUtils.logFolder(argo.groupConvention());

            String projectId = argo.groupConvention().currentProject().id();

            if (Strings.isNullOrEmpty(projectId))
                projectId = "argo";

            File logFile = new File(logFolder
                    , projectId + ".log");

            return logFile.getAbsolutePath();
        }

        private void configure(Properties properties) {
            PropertyConfigurator.configure(properties);
        }


    }
}
