package org.rythmengine.internal.compiler;

import org.rythmengine.internal.exceptions.RythmCompileException;
import org.rythmengine.internal.parser.ParsedTemplate;

import java.util.concurrent.Callable;

public abstract class TemplateCompiler implements Callable<CompiledTemplate> {
    protected ParsedTemplate parsedTemplate;
    protected CompiledTemplate compiledTemplate;

    public TemplateCompiler(ParsedTemplate parsed) throws RythmCompileException {
        this.parsedTemplate = parsed;
    }
}
