package org.rythmengine.internal.exceptions;

/**
 * Created by igmar on 22/07/16.
 */
public class RythmTemplateException extends RuntimeException {
    public RythmTemplateException() {
        super();
    }

    public RythmTemplateException(String message) {
        super(message);
    }

    public RythmTemplateException(Throwable cause) {
        super(cause);
    }
}
