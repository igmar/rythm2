package org.rythmengine.internal.parser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.util.StringUtils;
import org.rythmengine.internal.IResourceLoader;
import org.rythmengine.internal.debug.AntlrDebug;
import org.rythmengine.internal.exceptions.RythmGenerateException;
import org.rythmengine.internal.exceptions.RythmParserException;
import org.rythmengine.internal.generator.ISourceGenerator;
import org.rythmengine.internal.parser.RythmLexer;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public final class TemplateParser implements Callable<ParsedTemplate> {
    private String identifier;
    private org.rythmengine.internal.parser.RythmParser parser;
    private ParseTree pt;
    private String source;
    private IResourceLoader resourceLoader;
    private ISourceGenerator sourceGenerator;
    private InputStream is;

    public TemplateParser(final String identifier, final ISourceGenerator sourceGenerator, final IResourceLoader resourceLoader, final InputStream template) throws IOException {
        assert identifier != null;
        assert sourceGenerator != null;
        assert resourceLoader != null;
        assert template != null;

        this.identifier = identifier;
        this.sourceGenerator = sourceGenerator;
        this.resourceLoader = resourceLoader;
        this.is = template;
        this.source = inputStreamToString(this.is);
        this.parser = createParser(this.source, this.resourceLoader);

    }

    private ParsedTemplate parseTemplate() throws IOException, RythmGenerateException {
        if (this.parser.getNumberOfSyntaxErrors() > 0) {
            throw new RythmParserException("FIXME : Syntax error");
        }
        this.pt = parser.template();
        final TokenStream tokenstream = parser.getTokenStream();
        // FIXME : We need to get the list of parsed templates from the parser
        return new ParsedTemplate(identifier, sourceGenerator, pt, tokenstream, this.source);
    }

    private org.rythmengine.internal.parser.RythmParser createParser(String input, IResourceLoader resourceLoader) throws IOException {
        CharStream cs = new ANTLRInputStream(input);
        org.rythmengine.internal.parser.RythmLexer lexer = new org.rythmengine.internal.parser.RythmLexer(cs);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        tokenStream.fill();
        List<Token> tokens = tokenStream.getTokens();
        tokens = handleElseWS(tokens);
        tokens = mergeContentTokens(tokens);
        tokens = rewriteElseCondition(tokens);
        try {
            Field field = tokenStream.getClass().getSuperclass().getDeclaredField("tokens");
            field.setAccessible(true);
            field.set(tokenStream, tokens);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println(e);
            throw new RuntimeException("Reflection failed");
        }

        org.rythmengine.internal.parser.RythmParser parser = new org.rythmengine.internal.parser.RythmParser(tokenStream);
        parser.setResourceLoader(resourceLoader);

        return parser;
    }

    private List<Token> handleElseWS(final List<Token> input) {
        /*
         * We consider the EOL behind the { to be part of the condition, not whatever follows it
         * So, if we encounter a CURLY_OPEN, the token behind it is a newline
         */
        boolean curly_open = false;
        final List<Token> tokens = new ArrayList<>(input.size());
        for (int i = 0; i < input.size(); i++) {
            final Token current = input.get(i);
            if (current.getType() == RythmLexer.CURLY_OPEN) {
                tokens.add(current);
                curly_open = true;
                continue;
            }
            if (curly_open) {
                if (current.getType() == RythmLexer.CONTENT) {
                    if (current.getText().equals("\r") ||
                        current.getText().equals("\n") ||
                        current.getText().equals("\r\n")) {
                        CommonToken t = new CommonToken(current);
                        t.setType(RythmLexer.WS);
                        t.setChannel(RythmLexer.HIDDEN);
                        tokens.add(t);
                    } else {
                        tokens.add(current);
                    }
                }
                curly_open = false;
            } else {
                tokens.add(current);
            }
        }
        return tokens;
    }

    private List<Token> mergeContentTokens(final List<Token> input) {
        /*
           This gem is needed because ANTLR doesn't support stop tokens in non-greedy expressions. Which
           is bad.
           So, this gem converts all subsequent CONTENT tokens into 1.
           The TokenRewriteStream stuff doesn't work as I expect. Directly modify the source stream using
           reflection
         */
        final List<Token> tokens = new ArrayList<>(input.size());

        int token_start = -1;
        for (int i = 0; i < input.size(); i++) {
            final Token current = input.get(i);
            if (current.getType() != RythmLexer.CONTENT) {
                if (token_start != -1) {
                    final StringBuilder text = new StringBuilder();
                    Token fct = current;
                    for (int j = token_start; j < current.getTokenIndex(); j++) {
                        final Token t = input.get(j);
                        if (j == token_start) {
                            fct = t;
                        }
                        text.append(t.getText());
                    }
                    CommonToken t = new CommonToken(current);
                    t.setType(RythmLexer.CONTENT);
                    t.setText(text.toString());
                    t.setLine(fct.getLine());
                    tokens.add(t);
                    t = new CommonToken(current);
                    tokens.add(t);
                    token_start = -1;
                } else {
                    CommonToken t = new CommonToken(current);
                    tokens.add(t);
                }
            } else {
                if (token_start == -1) {
                    token_start = i;
                }
            }
        }
        return tokens;
    }

    private List<Token> rewriteElseCondition(final List<Token> input) {
        final List<Token> tokens = new ArrayList<>(input.size());

        for (int i = 0; i < input.size(); i++) {
            final Token current = input.get(i);
            if (current.getType() != RythmLexer.ELSE) {
                tokens.add(current);
            } else {
                if (i - 1 < 0 || i + 1 > input.size()) {
                    tokens.add(current);
                    continue;
                }
                final Token previous = input.get(i - 1);
                final Token next = input.get(i + 1);
                if (previous.getType() == RythmLexer.CONTENT &&
                        StringUtils.isWhitespace(previous.getText())) {
                    if (next.getType() ==RythmLexer.WS &&
                        StringUtils.isWhitespace(next.getText())) {
                        // Matches. Fixup the tokens
                        final CommonToken cp = (CommonToken) previous;
                        cp.setType(RythmLexer.WS);
                        cp.setChannel(RythmLexer.HIDDEN);
                        final CommonToken cn = (CommonToken) next;
                        cn.setChannel(RythmLexer.HIDDEN);
                        tokens.remove(i - 1);
                        tokens.add(cp);
                        tokens.add(current);
                        tokens.add(cn);
                        i++;
                    }
                }
            }
        }

        return tokens;
    }

    private String inputStreamToString(final InputStream is) throws IOException {
        return IOUtils.toString(is, StandardCharsets.UTF_8);
    }

    @Override
    public ParsedTemplate call() throws Exception {
        // Parse it
        return parseTemplate();
    }
}
