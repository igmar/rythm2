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

import org.rythmengine.ILogger;
import org.rythmengine.LoggerLevel;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class JDKLogger implements ILogger {
    private final Logger logger;
    private final String className;
    private LoggerLevel loggerLevel;

    public JDKLogger(Class<?> c, LoggerLevel logLevel) {
        className = c.getName();
        logger = Logger.getLogger(className);
        logger.setLevel(LoggerLevelConverter(logLevel));
        this.loggerLevel = logLevel;
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isLoggable(Level.FINEST);
    }

    @Override
    public void trace(String format, Object... args) {
        log(Level.FINEST, format, args);
    }

    @Override
    public void trace(Throwable t, String format, Object... args) {
        log(Level.FINEST, t, format, args);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isLoggable(Level.FINE);
    }

    @Override
    public void debug(String format, Object... args) {
        log(Level.FINE, format, args);
    }

    @Override
    public void debug(Throwable t, String format, Object... args) {
        log(Level.FINE, t, format, args);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isLoggable(Level.INFO);
    }

    @Override
    public void info(String format, Object... args) {
        log(Level.INFO, format, args);
    }

    @Override
    public void info(Throwable t, String format, Object... args) {
        log(Level.INFO, t, format, args);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isLoggable(Level.WARNING);
    }

    @Override
    public void warn(String format, Object... args) {
        log(Level.WARNING, format, args);
    }

    @Override
    public void warn(Throwable t, String format, Object... args) {
        log(Level.WARNING, t, format, args);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isLoggable(Level.SEVERE);
    }

    @Override
    public void error(String format, Object... args) {
        log(Level.SEVERE, format, args);
    }

    @Override
    public void error(Throwable t, String format, Object... args) {
        log(Level.SEVERE, t, format, args);
    }

    private void log(Level l, Throwable t, String m, Object... args) {
        if (logger.isLoggable(l)) {
            try {
                m = String.format(m, args);
            } catch (Exception e) {
                // ignore
            }
            logger.logp(l, className, null, m, t);
        }
    }

    private void log(Level l, String m, Object... args) {
        if (logger.isLoggable(l)) {
            try {
                m = String.format(m, args);
            } catch (Exception e) {
                // ignore
            }
            logger.logp(l, className, null, m);
        }
    }

    private Level LoggerLevelConverter(LoggerLevel loggerLevel) {
        switch (loggerLevel) {
            case OFF:
                return Level.OFF;
            case SEVERE:
                return Level.SEVERE;
            case WARNING:
                return Level.WARNING;
            case INFO:
                return Level.INFO;
            case DEBUG:
                return Level.FINE;
            case TRACE:
                return Level.FINEST;
            default:
                return Level.INFO;
        }
    }
}
