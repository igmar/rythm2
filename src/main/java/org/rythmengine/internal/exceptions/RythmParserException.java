package org.rythmengine.internal.exceptions;

/**
 * Created by igmar on 22/07/16.
 */
public class RythmParserException extends RuntimeException {
    public RythmParserException() {
        super();
    }

    public RythmParserException(String message) {
        super(message);
    }

    public RythmParserException(Throwable cause) {
        super(cause);
    }
}
