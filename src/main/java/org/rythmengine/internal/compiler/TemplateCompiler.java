package org.rythmengine.internal.compiler;

import org.rythmengine.conf.RythmConfiguration;
import org.rythmengine.internal.exceptions.RythmCompileException;
import org.rythmengine.internal.parser.ParsedTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class TemplateCompiler implements Callable<Map<ParsedTemplate, CompiledTemplate>> {
    protected List<ParsedTemplate> sources;
    protected RythmConfiguration configuration;

    public TemplateCompiler(final RythmConfiguration configuration, final List<ParsedTemplate> parsedTemplates) throws RythmCompileException {
        this.configuration = configuration;
        this.sources = parsedTemplates;
    }
}
