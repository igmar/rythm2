package org.rythmengine.internal.logger;

import org.rythmengine.internal.ILogger;
import org.rythmengine.internal.exceptions.RythmConfigException;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by igmar on 22/07/16.
 */
public abstract class Logger {
    private static Class<? extends ILogger> loggerClass;
    private static Map<Class<?>, ILogger> loggers = new ConcurrentHashMap<>();

    public static synchronized ILogger get(Class<?> c) {
        ILogger logger = loggers.get(c);
        if (logger == null) {
            try {
                logger = loggerClass.getDeclaredConstructor(c).newInstance(c);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                // Needs a constructor with Class<?> argument
                throw new RythmConfigException();
            }
            loggers.put(c, logger);
        }
        return logger;
    }

    public static void register(Class<JDKLogger> lc) {
        loggerClass = lc;
    }

    public static void register(ILogger logger) {
        loggerClass = logger.getClass();
    }
}
