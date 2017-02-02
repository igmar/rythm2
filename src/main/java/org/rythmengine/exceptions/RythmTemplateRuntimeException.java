package org.rythmengine.exceptions;

/**
 * This is thrown in the template itself when something goes wrong
 */
public class RythmTemplateRuntimeException extends RuntimeException {
    public RythmTemplateRuntimeException() { super(); }

    public RythmTemplateRuntimeException(final String message) { super(message); }
}
