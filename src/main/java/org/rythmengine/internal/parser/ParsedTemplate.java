package org.rythmengine.internal.parser;

import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.rythmengine.internal.exceptions.RythmGenerateException;
import org.rythmengine.internal.exceptions.RythmParserException;
import org.rythmengine.internal.exceptions.RythmTemplateException;
import org.rythmengine.internal.generator.ISourceGenerator;

public final class ParsedTemplate {
    private ParseTree pt;
    private TokenStream tokenStream;
    private ISourceGenerator sourceGenerator;
    private String source;
    private String generatedSource;

    public ParsedTemplate(final ISourceGenerator sourceGenerator, final ParseTree pt, TokenStream tokenstream, final String source) throws RythmGenerateException {
        assert sourceGenerator != null;
        assert pt != null;
        assert source != null;
        assert tokenstream != null;

        this.pt = pt;
        this.tokenStream = tokenstream;
        this.sourceGenerator = sourceGenerator;
        this.source = source;
        this.generatedSource = generateTemplateSource();
    }

    public String getSource() {
        return source;
    }

    public String getGeneratedSource() {
        if (generatedSource == null) {
            throw new RythmTemplateException("No sources generated");
        }
        return generatedSource;
    }

    private String generateTemplateSource() throws RythmGenerateException {
        this.generatedSource = sourceGenerator.generateSource(pt, tokenStream);
        if (this.generatedSource == null) {
            throw new RythmParserException("Failed to generate sources");
        }
        return this.generatedSource;
    }
}
