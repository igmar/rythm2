package org.rythmengine.internal.compiler.jdt;

import org.rythmengine.internal.compiler.CompiledTemplate;
import org.rythmengine.internal.compiler.TemplateCompiler;
import org.rythmengine.internal.exceptions.RythmCompileException;
import org.rythmengine.internal.parser.ParsedTemplate;

public class JDTTemplateCompiler extends TemplateCompiler {
    public JDTTemplateCompiler(ParsedTemplate parsed) throws RythmCompileException {
        super(parsed);
    }

    @Override
    public CompiledTemplate call() throws Exception {
        return null;
    }
}
