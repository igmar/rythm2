package org.rythmengine.internal.parser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.io.IOUtils;
import org.rythmengine.internal.IResourceLoader;
import org.rythmengine.internal.exceptions.RythmGenerateException;
import org.rythmengine.internal.exceptions.RythmParserException;
import org.rythmengine.internal.generator.ISourceGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

public final class TemplateParser implements Callable<ParsedTemplate> {
    private org.rythmengine.internal.parser.RythmParser parser;
    private ParseTree pt;
    private String source;
    private IResourceLoader resourceLoader;
    private ISourceGenerator sourceGenerator;
    private InputStream is;
    private TokenStream tokenstream;

    public TemplateParser(final ISourceGenerator sourceGenerator, final IResourceLoader resourceLoader, final InputStream template) {
        assert sourceGenerator != null;
        assert resourceLoader != null;
        assert template != null;

        this.sourceGenerator = sourceGenerator;
        this.resourceLoader = resourceLoader;
        this.is = template;
    }

    private ParsedTemplate parseTemplate() throws IOException, RythmGenerateException {
        this.source = inputStreamToString(this.is);
        this.parser = createParser(this.source, this.resourceLoader);
        if (this.parser.getNumberOfSyntaxErrors() > 0) {
            throw new RythmParserException("FIXME : Syntax error");
        }
        this.pt = parser.template();
        this.tokenstream = parser.getTokenStream();

        // FIXME : We need to get the list of parsed templates from the parser

        return new ParsedTemplate(sourceGenerator, pt, tokenstream, this.source);
    }

    private org.rythmengine.internal.parser.RythmParser createParser(String input, IResourceLoader resourceLoader) throws IOException {
        CharStream cs = new ANTLRInputStream(input);
        org.rythmengine.internal.parser.RythmLexer lexer = new org.rythmengine.internal.parser.RythmLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        org.rythmengine.internal.parser.RythmParser parser = new org.rythmengine.internal.parser.RythmParser(tokens);
        parser.setResourceLoader(resourceLoader);

        return parser;
    }

    private String inputStreamToString(InputStream is) throws IOException {
        return IOUtils.toString(is, StandardCharsets.UTF_8);
    }

    @Override
    public ParsedTemplate call() throws Exception {
        // Parse it
        return parseTemplate();
    }
}
