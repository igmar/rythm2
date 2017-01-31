package org.rythmengine.internal.exceptions;

public final class RythmParserException extends RuntimeException {
    public RythmParserException() {
        super();
    }

    public RythmParserException(final String message) {
        super(message);
    }

    public RythmParserException(final Throwable cause) {
        super(cause);
    }
}
