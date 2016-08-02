package org.rythmengine.internal.exceptions;

/**
 * Created by igmar on 12/07/16.
 */
public class RythmCompileException extends RuntimeException {
    public RythmCompileException() {
        super();
    }

    public RythmCompileException(String message) {
        super(message);
    }

    public RythmCompileException(Throwable cause) {
        super(cause);
    }

    public RythmCompileException(String message, Throwable cause) {
        super(message, cause);
    }
}
