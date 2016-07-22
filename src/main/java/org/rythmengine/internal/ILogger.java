package org.rythmengine.internal;

/**
 * Created by igmar on 12/07/16.
 */
public interface ILogger {
    boolean isTraceEnabled();

    void trace(String format, Object... args);

    void trace(Throwable t, String format, Object... args);

    boolean isDebugEnabled();

    void debug(String format, Object... args);

    void debug(Throwable t, String format, Object... args);

    boolean isInfoEnabled();

    void info(String format, Object... args);

    void info(Throwable t, String format, Object... args);

    boolean isWarnEnabled();

    void warn(String format, Object... args);

    void warn(Throwable t, String format, Object... args);

    boolean isErrorEnabled();

    void error(String format, Object... args);

    void error(Throwable t, String format, Object... args);
}
