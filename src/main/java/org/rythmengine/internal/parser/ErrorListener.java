package org.rythmengine.internal.parser;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.rythmengine.internal.exceptions.RythmParserException;

public class ErrorListener extends BaseErrorListener {
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) throws RythmParserException {
        // FIXME Generate a decent error message
        throw new RythmParserException();
    }
}
