package com.bj58.argo;

import com.google.common.collect.Maps;

import java.util.Map;

public class ArgoException extends RuntimeException {

    public static ArgoExceptionBuilder newBuilder(String message) {
        return newBuilder(message, null);
    }

    public static ArgoExceptionBuilder newBuilder(Throwable cause) {
        return newBuilder("", cause);
    }

    public static ArgoExceptionBuilder newBuilder() {
        return newBuilder("", null);
    }

    public static ArgoExceptionBuilder newBuilder(String message, Throwable cause) {
        return new ArgoExceptionBuilder(message, cause);
    }

    private static final long serialVersionUID = 5099827279044223975L;


    ArgoException() {
        super();
    }

    ArgoException(String message) {
        super(message);
    }

    ArgoException(Throwable cause) {
        super(cause);
    }

    ArgoException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ArgoException raise(String message) {
        return new ArgoException(message);
    }

    public static ArgoException raise(Throwable cause) {
        return new ArgoException(cause);
    }

    public static ArgoException raise(String message, Throwable cause) {
        return new ArgoException(message, cause);
    }

    public static class ArgoExceptionBuilder {
        private final Map<String, Object> contextInfos = Maps.newLinkedHashMap();

        private final Throwable cause;

        private final String currentMessage;

        ArgoExceptionBuilder(String message, Throwable cause) {
            this.currentMessage = message;
            this.cause = cause;
        }

        ArgoExceptionBuilder(Throwable cause) {
            this("", cause);
        }

        ArgoExceptionBuilder(String message) {
            this(message, null);
        }

        /**
         * 给异常增加上下文变量信息。
         * @param name 变量名
         * @param value 变量值
         * @return 自身
         */
        public ArgoExceptionBuilder addContextVariable(String name, Object value) {
            contextInfos.put(name, value);
            return this;
        }

        public ArgoExceptionBuilder addContextVariables(Map<?, ?> variables) {
            for (Map.Entry entry : variables.entrySet())
                addContextVariable(entry.toString(), entry.getValue());

            return this;
        }

        /**
         * 创建一个ArgoException
         */
        public ArgoException build() {
            return new ArgoException(getContextInfo(), cause);
        }

        /**
         * throw
         * @param clazz
         * @param <T>
         * @return
         */
        public <T> T raise(Class<T> clazz) {
            throw build();
        }

        private String getContextInfo() {
            return this.currentMessage +
                    (contextInfos.size() > 0  ?  "\ncontext: "  + contextInfos.toString()
                            : "");
        }
    }
}
