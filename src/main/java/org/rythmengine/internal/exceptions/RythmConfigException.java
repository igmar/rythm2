package org.rythmengine.internal.exceptions;

/**
 * Created by igmar on 22/07/16.
 */
public class RythmConfigException extends RuntimeException {
    public RythmConfigException() {
        super();
    }

    public RythmConfigException(String message) {
        super(message);
    }

    public RythmConfigException(Throwable cause) {
        super(cause);
    }
}
