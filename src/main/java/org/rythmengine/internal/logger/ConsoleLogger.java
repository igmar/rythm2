package org.rythmengine.internal.logger;

import org.rythmengine.ILogger;
import org.rythmengine.LoggerLevel;

public class ConsoleLogger implements ILogger {
    private final String className;
    private LoggerLevel loggerLevel;

    public ConsoleLogger(Class<?> c, LoggerLevel logLevel) {
        className = c.getName();
        this.loggerLevel = logLevel;
    }
    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public void trace(final String format, final Object... args) {
        System.out.println(String.format(format, args));
    }

    @Override
    public void trace(final Throwable t, final String format, final Object... args) {
        System.out.println(String.format(format, args));
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public void debug(final String format, final Object... args) {
        System.out.println(String.format(format, args));
    }

    @Override
    public void debug(final Throwable t, final String format, final Object... args) {
        System.out.println(String.format(format, args));
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public void info(final String format, final Object... args) {
        System.out.println(String.format(format, args));
    }

    @Override
    public void info(final Throwable t, final String format, final Object... args) {
        System.out.println(String.format(format, args));
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public void warn(final String format, final Object... args) {
        System.out.println(String.format(format, args));
    }

    @Override
    public void warn(final Throwable t, final String format, final Object... args) {
        System.out.println(String.format(format, args));
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public void error(final String format, final Object... args) {
        System.out.println(String.format(format, args));
    }

    @Override
    public void error(final Throwable t, final String format, final Object... args) {
        System.out.println(String.format(format, args));
    }
}
