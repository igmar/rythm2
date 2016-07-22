package org.rythmengine.internal.parser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.io.IOUtils;
import org.rythmengine.internal.IResourceLoader;
import org.rythmengine.internal.exceptions.RythmParserException;

import javax.inject.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

public final class TemplateParser implements Callable<ParsedTemplate> {
    private org.rythmengine.internal.parser.RythmParser parser;
    private ParseTree pt;
    private String source;
    private IResourceLoader resourceLoader;
    private InputStream is;

    public TemplateParser(final Provider<IResourceLoader> resourceLoaderProvider, final InputStream template) {
        this.resourceLoader = resourceLoaderProvider.get();
        this.is = template;
    }

    private ParsedTemplate parseTemplate() throws IOException {
        this.parser = createParser(this.is);
        if (this.parser.getNumberOfSyntaxErrors() > 0) {
            throw new RythmParserException();
        }
        this.pt = parser.template();
        this.source = inputStreamToString(this.is);
        // FIXME : We need to get the list of parsed templates from the parser
        return new ParsedTemplate(null, pt, this.source);
    }

    private org.rythmengine.internal.parser.RythmParser createParser(InputStream is) throws IOException {
        CharStream input = new ANTLRInputStream(is);
        org.rythmengine.internal.parser.RythmLexer lexer = new org.rythmengine.internal.parser.RythmLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        org.rythmengine.internal.parser.RythmParser parser = new org.rythmengine.internal.parser.RythmParser(tokens);

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
