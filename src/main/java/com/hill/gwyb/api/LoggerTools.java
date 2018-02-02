package com.hill.gwyb.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 */
public final class LoggerTools {
    private static Logger logger;

    private LoggerTools() {
    }

    public static Logger getLogger() {
        synchronized (LoggerTools.class) {
            if (logger == null) {
                logger = LoggerFactory.getLogger(LoggerTools.class);
            }
        }
        return logger;
    }
}