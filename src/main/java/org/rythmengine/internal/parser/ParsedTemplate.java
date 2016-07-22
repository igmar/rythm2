package org.rythmengine.internal.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.rythmengine.internal.exceptions.RythmTemplateException;

import java.util.Map;

public final class ParsedTemplate {
    private Map<String, String> input;
    private ParseTree pt;
    private String source;
    private String generatedSource;

    public ParsedTemplate(final Map<String, String> input, final ParseTree pt, final String source) {
        this.input = input;
        this.pt = pt;
        this.source = source;
        this.generatedSource = generateTemplateSource();
    }

    public String getSource() {
        return source;
    }

    public String getGeneratedSource() {
        if (generatedSource == null) {
            // FIXME
            throw new RythmTemplateException();
        }
        return generatedSource;
    }

    private String generateTemplateSource() {
        // FIXME
        return null;
    }
}
