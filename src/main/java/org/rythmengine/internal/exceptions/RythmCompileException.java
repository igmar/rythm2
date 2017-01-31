package org.rythmengine.internal.exceptions;

/*
 * Thrown when a template compile error occurs
 */
public final class RythmCompileException extends RuntimeException {
    public RythmCompileException() {
        super();
    }

    public RythmCompileException(final String message) {
        super(message);
    }

    public RythmCompileException(final Throwable cause) {
        super(cause);
    }

    public RythmCompileException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
