package org.rythmengine.internal.exceptions;


public final class RythmTemplateException extends RuntimeException {
    public RythmTemplateException() {
        super();
    }

    public RythmTemplateException(final String message) {
        super(message);
    }

    public RythmTemplateException(final Throwable cause) {
        super(cause);
    }
}
