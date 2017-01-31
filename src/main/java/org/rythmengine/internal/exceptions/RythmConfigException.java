package org.rythmengine.internal.exceptions;

/*
 * Thrown when a RythmEngine can't be create due to an error in the supplied config
 */
public final class RythmConfigException extends RuntimeException {
    public RythmConfigException() {
        super();
    }

    public RythmConfigException(final String message) {
        super(message);
    }

    public RythmConfigException(final Throwable cause) {
        super(cause);
    }
}
