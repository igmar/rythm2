package org.rythmengine.internal.logger;

import org.rythmengine.internal.ILogger;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class JDKLogger implements ILogger {
    private final Logger logger;
    private final String className;

    public JDKLogger(Class<?> c) {
        className = c.getName();
        logger = Logger.getLogger(className);
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
}
