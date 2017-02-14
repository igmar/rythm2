/*
 * Copyright (c) 2016-2017, Igmar Palsenberg. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rythmengine.internal.generator.java7;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.rythmengine.conf.RythmConfiguration;
import org.rythmengine.internal.ILogger;
import org.rythmengine.internal.exceptions.RythmCompileException;
import org.rythmengine.internal.exceptions.RythmGenerateException;
import org.rythmengine.internal.exceptions.RythmParserException;
import org.rythmengine.internal.fifo.FIFO;
import org.rythmengine.internal.fifo.LinkedFIFO;
import org.rythmengine.internal.generator.GeneratedTemplateSource;
import org.rythmengine.internal.generator.ISourceGenerator;
import org.rythmengine.internal.hash.sha1.SHA1;
import org.rythmengine.internal.logger.Logger;
import org.rythmengine.internal.parser.RythmParser;
import org.rythmengine.internal.parser.RythmParserBaseListener;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Java7SourceGenerator implements ISourceGenerator {
    private static ILogger logger = Logger.get(Java7SourceGenerator.class);

    private final RythmConfiguration configuration;
    private final ClassLoader classLoader;

    public Java7SourceGenerator(final RythmConfiguration configuration, final ClassLoader classLoader) {
        this.configuration = configuration;
        this.classLoader = classLoader;
    }

    @Override
    public GeneratedTemplateSource generateSource(final String source, final String identifier, final ParseTree pt, final
    TokenStream tokenStream) throws RythmGenerateException {
        if (identifier == null || pt == null || tokenStream == null) {
            throw new RythmParserException("Internal error : Bad arguments");
        }

        logger.debug("Generating source for %s", identifier);

        /*
         * We create a new parser / listener on every run : The classes aren't threadsafe
         */
        final ParseTreeWalker walker = new ParseTreeWalker();
        final Java7Listener listener = new Java7Listener(identifier, tokenStream, classLoader);
        walker.walk(listener, pt);

        final String template = loadTemplate().
                replace("@@PACKAGE@@", configuration.getCompiledPackage()).
                replace("@@CLASSNAME@@", listener.getGeneratedClassName()).
                replace("@@IMPORTS@@", listener.getGeneratedImports()).
                replace("@@VARS@@", listener.getGeneratedVars()).
                replace("@@CONSTRUCTOR@@", listener.getGeneratedConstructor()).
                replace("@@FUNCTIONS@@", listener.getGeneratedFunctions()).
                replace("@@FLOW@@", listener.getGeneratedFlow()).
                replace("@@METADATA@@", generateMetaData(source, identifier, listener.getLines(), listener.getMatrix()));

        final String canonicalName = String.format("rythmengine.compiled.%s", listener.getGeneratedClassName());
        return new GeneratedTemplateSource(canonicalName, template);
    }

    private String loadTemplate() throws RythmGenerateException {
        final InputStream tpl = this.getClass().getClassLoader().getResourceAsStream("templates/Template.tpl");
        if (tpl == null) {
            throw new RythmGenerateException("Can't find generator template");
        }

        try {
            return IOUtils.toString(tpl, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RythmGenerateException(e);
        }
    }

    private String generateMetaData(final String source, final String path, Map<String, String> lines, Map<String, String>
            matrix) {
        final String hash = SHA1.sha1Hex(source);

        return String.format("    -- GENERATED --%n" + "    PATH   : %s%n" + "    SHA1   : %s%n" + "    LINES  : %s%n" + "    " +
                "MATRIX : %s%n" + "    ---------------%n", path, hash, flattenMap(lines), flattenMap(matrix));
    }

    private String flattenMap(final Map<String, String> lines) {
        final List<String> t = new ArrayList<>();
        for (Map.Entry<String, String> e : lines.entrySet()) {
            t.add(String.format("%s->%s", e.getKey(), e.getValue()));
        }
        return StringUtils.join(t, "|");
    }

    private static class Java7Listener extends RythmParserBaseListener {
        // Debug
        private final Boolean debug = false;
        // Provided
        private final String identifier;
        private final TokenStream tokenStream;
        private final ClassLoader classLoader;

        // Generated
        private String className;
        private final StringBuffer flow;
        private final StringBuffer imports;
        private final StringBuffer constructor; // Maps to @@CONSTRUCTOR@@
        private final StringBuffer functions;
        private final StringBuffer vars;
        private final FIFO<String> stack;

        public Java7Listener(final String identifier, final TokenStream tokenStream, final ClassLoader classLoader) {

            this.identifier = identifier;
            this.tokenStream = tokenStream;
            this.classLoader = classLoader;

            this.flow = new StringBuffer();
            this.constructor = new StringBuffer();
            this.functions = new StringBuffer();
            this.vars = new StringBuffer();
            this.imports = new StringBuffer();
            this.stack = new LinkedFIFO<>();
        }

        public String getGeneratedClassName() {
            return this.className;
        }

        public String getGeneratedImports() {
            return this.imports.toString();
        }

        public String getGeneratedConstructor() {
            return this.constructor.toString();
        }

        public String getGeneratedFunctions() {
            return this.functions.toString();
        }

        public String getGeneratedVars() {
            return this.vars.toString();
        }

        public String getGeneratedFlow() {
            return this.flow.toString();
        }

        @Override
        public void enterBlock(RythmParser.BlockContext ctx) {
            logger.debug("enterBlock()");
            super.enterBlock(ctx);
            this.flow.append("{\n");
        }

        @Override
        public void exitBlock(RythmParser.BlockContext ctx) {
            logger.debug("exitBlock()");
            super.exitBlock(ctx);
            this.flow.append("\t\t}\n");
        }

        @Override
        public void enterForExpression(RythmParser.ForExpressionContext ctx) {
            logger.debug("enterForExpression()");
            super.enterForExpression(ctx);

            this.flow.append(String.format("for (%s %s; %s; %s)\t", ctx.integralType().getText(),
                    ctx.variableDeclarator().getText(),
                    ctx.expression(0).getText(),
                    ctx.expression(1).getText()));
        }

        @Override
        public void exitForExpression(RythmParser.ForExpressionContext ctx) {
            logger.debug("exitForExpression()");
            super.exitForExpression(ctx);
        }

        @Override
        public void enterEnhancedForExpression(RythmParser.EnhancedForExpressionContext ctx) {
            logger.debug("enterEnhancedForExpression()");
            super.enterEnhancedForExpression(ctx);
        }

        @Override
        public void exitEnhancedForExpression(RythmParser.EnhancedForExpressionContext ctx) {
            logger.debug("exitEnhancedForExpression()");
            super.exitEnhancedForExpression(ctx);
        }
        
        @Override
        public void enterArgs(RythmParser.ArgsContext ctx) {
            logger.debug("enterArgs()");
            super.enterArgs(ctx);
        }

        @Override
        public void exitArgs(RythmParser.ArgsContext ctx) {
            logger.debug("exitArgs()");
            super.exitArgs(ctx);

            /*
             * This handles the @args : We parse those,
             * lookup the class, write  the @@VARS@@ and @@CONSTRUCTOR@@
             */
            for (RythmParser.TemplateArgumentContext arg : ctx.templateArgument()) {
                final Token type = arg.getStart();
                final TerminalNode name = arg.IDENTIFIER();
                final Class<?> typeRef;
                String fqn = getClassName(type.getText());

                try {
                    typeRef = Class.forName(fqn);
                } catch (ClassNotFoundException e) {
                    // FIXME: Make this into a proper error, including the offending line number in the source.
                    throw new RythmParserException(e);
                }

                if (fqn.startsWith("java.lang.")) {
                    fqn = fqn.replace("java.lang.", "");
                } else {
                    this.imports.append(String.format("import %s;%n", fqn));
                }
                this.vars.append(String.format("\tprivate %s %s;%n", fqn, name));
                this.constructor.append(String.format("\t\tthis.%s = (%s) args.get(\"%s\");%n", name, fqn, name));
            }
        }

        @Override
        public void enterTemplateArgument(RythmParser.TemplateArgumentContext ctx) {
            logger.debug("enterTemplateArgument()");
            super.enterTemplateArgument(ctx);
        }

        @Override
        public void exitTemplateArgument(RythmParser.TemplateArgumentContext ctx) {
            logger.debug("exitTemplateArgument()");
            super.exitTemplateArgument(ctx);
        }

        @Override
        public void enterVariableDeclarator(RythmParser.VariableDeclaratorContext ctx) {
            logger.debug("enterVariableDeclarator()");
            super.enterVariableDeclarator(ctx);
        }

        @Override
        public void exitVariableDeclarator(RythmParser.VariableDeclaratorContext ctx) {
            logger.debug("exitVariableDeclarator()");
            super.exitVariableDeclarator(ctx);
        }

        @Override
        public void enterIntegralType(RythmParser.IntegralTypeContext ctx) {
            logger.debug("enterIntegralType()");
            super.enterIntegralType(ctx);
        }

        @Override
        public void exitIntegralType(RythmParser.IntegralTypeContext ctx) {
            logger.debug("exitIntegralType()");
            super.exitIntegralType(ctx);
        }

        @Override
        public void enterBoolExpression(RythmParser.BoolExpressionContext ctx) {
            logger.debug("enterBoolExpression()");
            super.enterBoolExpression(ctx);
            this.flow.append(ctx.getText());
        }

        @Override
        public void exitBoolExpression(RythmParser.BoolExpressionContext ctx) {
            logger.debug("exitBoolExpression()");
            super.exitBoolExpression(ctx);
        }

        @Override
        public void enterExpression(RythmParser.ExpressionContext ctx) {
            logger.debug("enterExpression()");
            super.enterExpression(ctx);
        }

        @Override
        public void exitExpression(RythmParser.ExpressionContext ctx) {
            logger.debug("exitExpression()");
            super.exitExpression(ctx);
        }

        @Override
        public void enterIncDecOperator(RythmParser.IncDecOperatorContext ctx) {
            logger.debug("enterIncDecOperator()");
            super.enterIncDecOperator(ctx);
        }

        @Override
        public void exitIncDecOperator(RythmParser.IncDecOperatorContext ctx) {
            logger.debug("exitIncDecOperator()");
            super.exitIncDecOperator(ctx);
        }

        @Override
        public void enterPrefixOperator(RythmParser.PrefixOperatorContext ctx) {
            logger.debug("enterPrefixOperator()");
            super.enterPrefixOperator(ctx);
        }

        @Override
        public void exitPrefixOperator(RythmParser.PrefixOperatorContext ctx) {
            logger.debug("exitPrefixOperator()");
            super.exitPrefixOperator(ctx);
        }

        @Override
        public void enterPrimary(RythmParser.PrimaryContext ctx) {
            logger.debug("enterPrimary()");
            super.enterPrimary(ctx);
        }

        @Override
        public void exitPrimary(RythmParser.PrimaryContext ctx) {
            logger.debug("exitPrimary()");
            super.exitPrimary(ctx);
        }

        @Override
        public void enterLiteral(RythmParser.LiteralContext ctx) {
            logger.debug("enterLiteral()");
            super.enterLiteral(ctx);
        }

        @Override
        public void exitLiteral(RythmParser.LiteralContext ctx) {
            logger.debug("exitLiteral()");
            super.exitLiteral(ctx);
        }

        @Override
        public void enterIntegerLiteral(RythmParser.IntegerLiteralContext ctx) {
            super.enterIntegerLiteral(ctx);
        }

        @Override
        public void exitIntegerLiteral(RythmParser.IntegerLiteralContext ctx) {
            super.exitIntegerLiteral(ctx);
        }

        @Override
        public void enterBooleanLiteral(RythmParser.BooleanLiteralContext ctx) {
            super.enterBooleanLiteral(ctx);
        }

        @Override
        public void exitBooleanLiteral(RythmParser.BooleanLiteralContext ctx) {
            super.exitBooleanLiteral(ctx);
        }

        @Override
        public void enterDecimalIntegerLiteral(RythmParser.DecimalIntegerLiteralContext ctx) {
            super.enterDecimalIntegerLiteral(ctx);
        }

        @Override
        public void exitDecimalIntegerLiteral(RythmParser.DecimalIntegerLiteralContext ctx) {
            super.exitDecimalIntegerLiteral(ctx);
        }

        @Override
        public void enterDecimalNumeral(RythmParser.DecimalNumeralContext ctx) {
            super.enterDecimalNumeral(ctx);
        }

        @Override
        public void exitDecimalNumeral(RythmParser.DecimalNumeralContext ctx) {
            super.exitDecimalNumeral(ctx);
        }

        @Override
        public void enterDigits(RythmParser.DigitsContext ctx) {
            super.enterDigits(ctx);
        }

        @Override
        public void exitDigits(RythmParser.DigitsContext ctx) {
            super.exitDigits(ctx);
        }

        @Override
        public void enterDigitOrUnderscore(RythmParser.DigitOrUnderscoreContext ctx) {
            super.enterDigitOrUnderscore(ctx);
        }

        @Override
        public void exitDigitOrUnderscore(RythmParser.DigitOrUnderscoreContext ctx) {
            super.exitDigitOrUnderscore(ctx);
        }

        @Override
        public void enterDigit(RythmParser.DigitContext ctx) {
            super.enterDigit(ctx);
        }

        @Override
        public void exitDigit(RythmParser.DigitContext ctx) {
            super.exitDigit(ctx);
        }

        @Override
        public void enterUnderScores(RythmParser.UnderScoresContext ctx) {
            super.enterUnderScores(ctx);
        }

        @Override
        public void exitUnderScores(RythmParser.UnderScoresContext ctx) {
            super.exitUnderScores(ctx);
        }

        @Override
        public void enterHexIntegerLiteral(RythmParser.HexIntegerLiteralContext ctx) {
            super.enterHexIntegerLiteral(ctx);
        }

        @Override
        public void exitHexIntegerLiteral(RythmParser.HexIntegerLiteralContext ctx) {
            super.exitHexIntegerLiteral(ctx);
        }

        @Override
        public void enterHexNumeral(RythmParser.HexNumeralContext ctx) {
            super.enterHexNumeral(ctx);
        }

        @Override
        public void exitHexNumeral(RythmParser.HexNumeralContext ctx) {
            super.exitHexNumeral(ctx);
        }

        @Override
        public void enterHexDigits(RythmParser.HexDigitsContext ctx) {
            super.enterHexDigits(ctx);
        }

        @Override
        public void exitHexDigits(RythmParser.HexDigitsContext ctx) {
            super.exitHexDigits(ctx);
        }

        @Override
        public void enterIntegerTypeSuffix(RythmParser.IntegerTypeSuffixContext ctx) {
            super.enterIntegerTypeSuffix(ctx);
        }

        @Override
        public void exitIntegerTypeSuffix(RythmParser.IntegerTypeSuffixContext ctx) {
            super.exitIntegerTypeSuffix(ctx);
        }

        @Override
        public void enterComment(RythmParser.CommentContext ctx) {
            logger.debug("enterComment()");
            super.enterComment(ctx);
        }

        @Override
        public void exitComment(RythmParser.CommentContext ctx) {
            logger.debug("exitComment()");
            super.exitComment(ctx);
        }

        @Override
        public void enterJavaBlock(RythmParser.JavaBlockContext ctx) {
            logger.debug("enterJavaBlock()");
            super.enterJavaBlock(ctx);
        }

        @Override
        public void exitJavaBlock(RythmParser.JavaBlockContext ctx) {
            logger.debug("exitJavaBlock()");
            super.exitJavaBlock(ctx);
        }

        @Override
        public void enterQualifiedName(RythmParser.QualifiedNameContext ctx) {
            super.enterQualifiedName(ctx);
        }

        @Override
        public void exitQualifiedName(RythmParser.QualifiedNameContext ctx) {
            super.exitQualifiedName(ctx);
        }

        @Override
        public void enterEveryRule(ParserRuleContext ctx) {
            super.enterEveryRule(ctx);
        }

        @Override
        public void exitEveryRule(ParserRuleContext ctx) {
            super.exitEveryRule(ctx);
        }

        @Override
        public void enterTemplate(RythmParser.TemplateContext ctx) {
            logger.debug("enterTemplate()");
            super.enterTemplate(ctx);

            this.className = String.format("ct_%s", this.identifier.replace("/", "_").replace(".", "_"));
        }

        @Override
        public void exitTemplate(RythmParser.TemplateContext ctx) {
            logger.debug("exitTemplate()");
            super.exitTemplate(ctx);

            // TODO : Write post-parse actions
            // We need to check which of the args are referenced, so that we can put in proper check to prevent NPE's
        }

        @Override
        public void enterElements(RythmParser.ElementsContext ctx) {
            logger.debug("enterElements()");
            super.enterElements(ctx);
        }

        @Override
        public void exitElements(RythmParser.ElementsContext ctx) {
            logger.debug("exitElements()");
            super.exitElements(ctx);

            // Flush the fifo.
            for (int i = 0; i < stack.size(); i++) {
                String s = stack.peek(i);
                this.flow.append(String.format("\t\t%s();%n", s));
            }
            stack.clear();
        }

        @Override
        public void enterTemplatedata(RythmParser.TemplatedataContext ctx) {
            logger.debug("enterTemplatedata()");
            super.enterTemplatedata(ctx);
        }

        @Override
        public void exitTemplatedata(RythmParser.TemplatedataContext ctx) {
            logger.debug("exitTemplatedata()");
            super.exitTemplatedata(ctx);

            // Create a proper function name, add to the stack.
            final String functionName = String.format("templatedata_%s", ctx.getStart().getLine());
            stack.push(functionName);
            this.functions.append(String.format("\tprivate void %s() {", functionName));
            this.functions.append(String.format("\t\tthis.sb.append(\"%s\");", StringEscapeUtils.escapeJava(ctx.getText())));
            this.functions.append("\t}\n");
        }

        @Override
        public void enterTemplatedataelement(RythmParser.TemplatedataelementContext ctx) {
            super.enterTemplatedataelement(ctx);
        }

        @Override
        public void exitTemplatedataelement(RythmParser.TemplatedataelementContext ctx) {
            super.exitTemplatedataelement(ctx);
        }

        @Override
        public void enterFlow_if(RythmParser.Flow_ifContext ctx) {
            logger.debug("enterFlow_if()");
            super.enterFlow_if(ctx);

            this.flow.append("\t\tif ");
        }

        @Override
        public void exitFlow_if(RythmParser.Flow_ifContext ctx) {
            logger.debug("exitFlow_if()");
            super.exitFlow_if(ctx);

        }

        @Override
        public void enterFlow_if_else(RythmParser.Flow_if_elseContext ctx) {
            logger.debug("enterFlow_if_else");
            super.enterFlow_if_else(ctx);
            this.flow.append(" else ");
        }

        @Override
        public void exitFlow_if_else(RythmParser.Flow_if_elseContext ctx) {
            logger.debug("exitFlow_if_else");
            super.exitFlow_if_else(ctx);
        }

        @Override
        public void enterFlow_for(RythmParser.Flow_forContext ctx) {
            logger.debug("enterFlow_for()");
            super.enterFlow_for(ctx);
        }

        @Override
        public void exitFlow_for(RythmParser.Flow_forContext ctx) {
            logger.debug("exitFlow_for()");
            super.exitFlow_for(ctx);
        }

        @Override
        public void enterOutputExpression(RythmParser.OutputExpressionContext ctx) {
            super.enterOutputExpression(ctx);
            logger.debug("enterOutputExpression()");
        }

        @Override
        public void exitOutputExpression(RythmParser.OutputExpressionContext ctx) {
            logger.debug("exitOutputExpression()");
            super.exitOutputExpression(ctx);
        }

        @Override
        public void enterCanonicalName(RythmParser.CanonicalNameContext ctx) {
            super.enterCanonicalName(ctx);
        }

        @Override
        public void exitCanonicalName(RythmParser.CanonicalNameContext ctx) {
            super.exitCanonicalName(ctx);
        }

        @Override
        public void enterFlow_return(RythmParser.Flow_returnContext ctx) {
            super.enterFlow_return(ctx);
        }

        @Override
        public void exitFlow_return(RythmParser.Flow_returnContext ctx) {
            super.exitFlow_return(ctx);
        }

        @Override
        public void enterMacro(RythmParser.MacroContext ctx) {
            super.enterMacro(ctx);
        }

        @Override
        public void exitMacro(RythmParser.MacroContext ctx) {
            super.exitMacro(ctx);
        }

        @Override
        public void enterInclude(RythmParser.IncludeContext ctx) {
            super.enterInclude(ctx);
        }

        @Override
        public void exitInclude(RythmParser.IncludeContext ctx) {
            super.exitInclude(ctx);
        }

        @Override
        public void enterMethodArguments(RythmParser.MethodArgumentsContext ctx) {
            super.enterMethodArguments(ctx);
        }

        @Override
        public void exitMethodArguments(RythmParser.MethodArgumentsContext ctx) {
            super.exitMethodArguments(ctx);
        }

        private String getClassName(String className) {
            final String[] packageNames = { "java.lang", "java.util" };
            if (!className.contains(".")) {
                for (String packageName : packageNames) {
                    final String fqqn = String.format("%s.%s", packageName, className);
                    try {
                        Class.forName(fqqn, false, classLoader);
                    } catch (ClassNotFoundException e) {
                        continue;
                    }
                    return fqqn;
                }
                return null;
            } else {
                return className;
            }
        }

        public Map<String, String> getLines() {
            return Collections.emptyMap();
        }

        public Map<String, String> getMatrix() {
            return Collections.emptyMap();
        }
    }
}
