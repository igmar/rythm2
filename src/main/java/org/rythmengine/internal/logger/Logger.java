/*
 * Copyright (c) 2016-2017, Igmar Palsenberg. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rythmengine.internal.logger;

import org.rythmengine.internal.ILogger;
import org.rythmengine.internal.exceptions.RythmConfigException;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Logger {
    private static Class<? extends ILogger> loggerClass;
    private static Map<Class<?>, ILogger> loggers = new ConcurrentHashMap<>();

    private Logger() {
    }

    public static synchronized ILogger get(Class<?> c) {
        ILogger logger = loggers.get(c);
        if (logger == null) {
            try {
                logger = loggerClass.getDeclaredConstructor(Class.class).newInstance(c);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                // Needs a constructor with Class<?> argument
                throw new RythmConfigException(e);
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
