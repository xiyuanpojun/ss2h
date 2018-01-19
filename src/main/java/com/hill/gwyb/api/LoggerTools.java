package com.hill.gwyb.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 */
public final class LoggerTools {
    // OFF > FATAL > ERROR > WARN > INFO > DEBUG > TRACE > ALL -->
    public static final int ERROR = 1, WARN = 2, INFO = 3, DEBUG = 4, TRACE = 5;

    private LoggerTools() {
    }

    public static void writerLogToConsole(Class c, String msg, int level) {
        Logger logger;
        if (c != null && null != msg && !"".equals(msg) && level >= ERROR && level <= TRACE) {
            logger = LoggerFactory.getLogger(c);
            switch (level) {
                case ERROR:
                    logger.error(msg);
                    break;
                case WARN:
                    logger.warn(msg);
                    break;
                case INFO:
                    logger.info(msg);
                    break;
                case DEBUG:
                    logger.debug(msg);
                    break;
                case TRACE:
                    logger.trace(msg);
                    break;
            }
        } else {
            logger = LoggerFactory.getLogger(LoggerTools.class);
            logger.warn("输出参数异常");
        }
    }

    public static void writerLogToConsole(Class c, String msg) {
        writerLogToConsole(c, msg, DEBUG);
    }
}