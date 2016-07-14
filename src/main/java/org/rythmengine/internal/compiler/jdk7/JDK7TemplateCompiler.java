package org.rythmengine.internal.compiler.jdk7;

import org.rythmengine.internal.compiler.CompiledTemplate;
import org.rythmengine.internal.compiler.TemplateCompiler;
import org.rythmengine.internal.exceptions.RythmCompileException;
import org.rythmengine.internal.parser.ParsedTemplate;

public class JDK7TemplateCompiler extends TemplateCompiler {
    public JDK7TemplateCompiler(ParsedTemplate parsed) throws RythmCompileException {
        super(parsed);
    }

    @Override
    public CompiledTemplate call() throws Exception {
        // TODO : Compile it
        return null;
    }
}
