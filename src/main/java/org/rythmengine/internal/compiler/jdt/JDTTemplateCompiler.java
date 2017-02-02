package org.rythmengine.internal.compiler.jdt;

import org.rythmengine.conf.RythmConfiguration;
import org.rythmengine.internal.compiler.CompiledTemplate;
import org.rythmengine.internal.compiler.TemplateCompiler;
import org.rythmengine.internal.exceptions.RythmCompileException;
import org.rythmengine.internal.parser.ParsedTemplate;

import java.util.List;
import java.util.Map;

public class JDTTemplateCompiler extends TemplateCompiler {
    public JDTTemplateCompiler(final RythmConfiguration configuration, final List<ParsedTemplate> parsedTemplates) throws RythmCompileException {
        super(configuration, parsedTemplates);
    }

    @Override
    public Map<ParsedTemplate, CompiledTemplate> call() throws Exception {
        return null;
    }
}
