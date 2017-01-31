package org.rythmengine.internal.exceptions;

/*
 * Thrown when we encounter an error in the generation phase
 */
public final class RythmGenerateException extends Exception {
    public RythmGenerateException() {
        super();
    }

    public RythmGenerateException(final String message) {
        super(message);
    }

    public RythmGenerateException(final Throwable cause) {
        super(cause);
    }
}
