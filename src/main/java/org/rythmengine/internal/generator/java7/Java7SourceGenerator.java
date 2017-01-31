package org.rythmengine.internal.generator.java7;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.rythmengine.internal.ILogger;
import org.rythmengine.internal.exceptions.RythmGenerateException;
import org.rythmengine.internal.exceptions.RythmParserException;
import org.rythmengine.internal.fifo.FIFO;
import org.rythmengine.internal.fifo.LinkedFIFO;
import org.rythmengine.internal.generator.ISourceGenerator;
import org.rythmengine.internal.logger.Logger;
import org.rythmengine.internal.parser.RythmParser;
import org.rythmengine.internal.parser.RythmParserBaseListener;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Java7SourceGenerator implements ISourceGenerator {
    private static ILogger logger = Logger.get(Java7SourceGenerator.class);

    public Java7SourceGenerator() {
    }

    @Override
    public String generateSource(final String identifier, final ParseTree pt, final TokenStream tokenStream) throws RythmGenerateException {
        if (identifier == null || pt == null || tokenStream == null) {
            throw new RythmParserException("Internal error : Bad arguments");
        }
        logger.info("generateSource()");

        ParseTreeWalker walker = new ParseTreeWalker();
        Java7Listener listener = new Java7Listener(identifier, tokenStream);
        walker.walk(listener, pt);

        final String template = loadTemplate().
                replace("@@CLASSNAME@@", listener.getGeneratedClassName()).
                replace("@@IMPORTS@@", listener.getGeneratedImports()).
                replace("@@VARS@@", listener.getGeneratedVars()).
                replace("@@CONSTRUCTOR@@", listener.getGeneratedConstructor()).
                replace("@@FUNCTIONS@@", listener.getGeneratedFunctions()).
                replace("@@FLOW@@", listener.getGeneratedFlow());

        System.out.println(template);

        return null;
    }

    private String loadTemplate() throws RythmGenerateException {
        InputStream tpl = this.getClass().getClassLoader().getResourceAsStream("templates/Template.tpl");
        if (tpl == null) {
            throw new RythmGenerateException("Can't find generator template");
        }

        try {
            return IOUtils.toString(tpl, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RythmGenerateException(e);
        }
    }

    private class Java7Listener extends RythmParserBaseListener {
        // Provided
        private final String identifier;
        private final TokenStream tokenStream;

        // Generated
        private String className;
        private final StringBuffer flow;
        private final StringBuffer imports;
        private final StringBuffer constructor; // Maps to @@CONSTRUCTOR@@
        private final StringBuffer functions;
        private final StringBuffer vars;
        private final FIFO<String> stack;


        public Java7Listener(final String identifier, final TokenStream tokenStream) {

            this.identifier = identifier;
            this.tokenStream = tokenStream;

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
            System.out.println("enterBlock()");
            super.enterBlock(ctx);
            this.flow.append("{\n");
        }

        @Override
        public void exitBlock(RythmParser.BlockContext ctx) {
            System.out.println("exitBlock()");
            super.exitBlock(ctx);
            this.flow.append("\t\t}");
        }

        @Override
        public void enterForExpression(RythmParser.ForExpressionContext ctx) {
            System.out.println("enterForExpression()");
            super.enterForExpression(ctx);
        }

        @Override
        public void exitForExpression(RythmParser.ForExpressionContext ctx) {
            System.out.println("exitForExpression()");
            super.exitForExpression(ctx);
        }

        @Override
        public void enterArgs(RythmParser.ArgsContext ctx) {
            System.out.println("enterArgs()");
            super.enterArgs(ctx);
        }

        @Override
        public void exitArgs(RythmParser.ArgsContext ctx) {
            System.out.println("exitArgs()");
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
                    this.imports.append(String.format("import %s;\n", fqn));
                }
                this.vars.append(String.format("\tprivate %s %s;\n", fqn, name));
                this.constructor.append(String.format("\t\tthis.%s = args.get(%s);\n", name, name));
            }
        }

        @Override
        public void enterTemplateArgument(RythmParser.TemplateArgumentContext ctx) {
            System.out.println("enterTemplateArgument()");
            super.enterTemplateArgument(ctx);
        }

        @Override
        public void exitTemplateArgument(RythmParser.TemplateArgumentContext ctx) {
            System.out.println("exitTemplateArgument()");
            super.exitTemplateArgument(ctx);
        }

        @Override
        public void enterVariableDeclarator(RythmParser.VariableDeclaratorContext ctx) {
            System.out.println("enterVariableDeclarator()");
            super.enterVariableDeclarator(ctx);
        }

        @Override
        public void exitVariableDeclarator(RythmParser.VariableDeclaratorContext ctx) {
            System.out.println("exitVariableDeclarator()");
            super.exitVariableDeclarator(ctx);
        }

        @Override
        public void enterIntegralType(RythmParser.IntegralTypeContext ctx) {
            System.out.println("enterIntegralType()");
            super.enterIntegralType(ctx);
        }

        @Override
        public void exitIntegralType(RythmParser.IntegralTypeContext ctx) {
            System.out.println("exitIntegralType()");
            super.exitIntegralType(ctx);
        }

        @Override
        public void enterBoolExpression(RythmParser.BoolExpressionContext ctx) {
            System.out.println("enterBoolExpression()");
            super.enterBoolExpression(ctx);
            this.flow.append(ctx.getText());
        }

        @Override
        public void exitBoolExpression(RythmParser.BoolExpressionContext ctx) {
            System.out.println("exitBoolExpression()");
            super.exitBoolExpression(ctx);
        }

        @Override
        public void enterExpression(RythmParser.ExpressionContext ctx) {
            System.out.println("enterExpression()");
            super.enterExpression(ctx);
        }

        @Override
        public void exitExpression(RythmParser.ExpressionContext ctx) {
            System.out.println("exitExpression()");
            super.exitExpression(ctx);
        }

        @Override
        public void enterIncDecOperator(RythmParser.IncDecOperatorContext ctx) {
            System.out.println("enterIncDecOperator()");
            super.enterIncDecOperator(ctx);
        }

        @Override
        public void exitIncDecOperator(RythmParser.IncDecOperatorContext ctx) {
            System.out.println("exitIncDecOperator()");
            super.exitIncDecOperator(ctx);
        }

        @Override
        public void enterPrefixOperator(RythmParser.PrefixOperatorContext ctx) {
            System.out.println("enterPrefixOperator()");
            super.enterPrefixOperator(ctx);
        }

        @Override
        public void exitPrefixOperator(RythmParser.PrefixOperatorContext ctx) {
            System.out.println("exitPrefixOperator()");
            super.exitPrefixOperator(ctx);
        }

        @Override
        public void enterPrimary(RythmParser.PrimaryContext ctx) {
            System.out.println("enterPrimary()");
            super.enterPrimary(ctx);
        }

        @Override
        public void exitPrimary(RythmParser.PrimaryContext ctx) {
            System.out.println("exitPrimary()");
            super.exitPrimary(ctx);
        }

        @Override
        public void enterLiteral(RythmParser.LiteralContext ctx) {
            System.out.println("enterLiteral()");
            super.enterLiteral(ctx);
        }

        @Override
        public void exitLiteral(RythmParser.LiteralContext ctx) {
            System.out.println("exitLiteral()");
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
            System.out.println("enterComment()");
            super.enterComment(ctx);
        }

        @Override
        public void exitComment(RythmParser.CommentContext ctx) {
            System.out.println("exitComment()");
            super.exitComment(ctx);
        }

        @Override
        public void enterJavaBlock(RythmParser.JavaBlockContext ctx) {
            System.out.println("enterJavaBlock()");
            super.enterJavaBlock(ctx);
        }

        @Override
        public void exitJavaBlock(RythmParser.JavaBlockContext ctx) {
            System.out.println("exitJavaBlock()");
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
            System.out.println("enterTemplate()");
            super.enterTemplate(ctx);

            this.className = String.format("ct_%s", this.identifier.replace("/", "_").replace(".", "_"));
        }

        @Override
        public void exitTemplate(RythmParser.TemplateContext ctx) {
            System.out.println("exitTemplate()");
            super.exitTemplate(ctx);

            // TODO : Write post-parse actions
            // We need to check which of the args are referenced, so that we can put in proper check to prevent NPE's
        }

        @Override
        public void enterElements(RythmParser.ElementsContext ctx) {
            System.out.println("enterElements()");
            super.enterElements(ctx);
        }

        @Override
        public void exitElements(RythmParser.ElementsContext ctx) {
            System.out.println("exitElements()");
            super.exitElements(ctx);

            // Flush the fifo.
            for (int i = 0; i < stack.size(); i++) {
                String s = stack.peek(i);
                this.flow.append(String.format("\t\t%s();\n", s));
            }
            stack.clear();
        }

        @Override
        public void enterTemplatedata(RythmParser.TemplatedataContext ctx) {
            System.out.println("enterTemplatedata()");
            super.enterTemplatedata(ctx);
        }

        @Override
        public void exitTemplatedata(RythmParser.TemplatedataContext ctx) {
            System.out.println("exitTemplatedata()");
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
            System.out.println("enterFlow_if()");
            super.enterFlow_if(ctx);

            this.flow.append("\t\tif ");
        }

        @Override
        public void exitFlow_if(RythmParser.Flow_ifContext ctx) {
            System.out.println("exitFlow_if()");
            super.exitFlow_if(ctx);

        }

        @Override
        public void enterFlow_if_else(RythmParser.Flow_if_elseContext ctx) {
            System.out.println("enterFlow_if_else");
            super.enterFlow_if_else(ctx);
            this.flow.append(" else ");
        }

        @Override
        public void exitFlow_if_else(RythmParser.Flow_if_elseContext ctx) {
            System.out.println("exitFlow_if_else");
            super.exitFlow_if_else(ctx);
        }

        @Override
        public void enterFlow_for(RythmParser.Flow_forContext ctx) {
            System.out.println("enterFlow_for()");
            super.enterFlow_for(ctx);
        }

        @Override
        public void exitFlow_for(RythmParser.Flow_forContext ctx) {
            System.out.println("exitFlow_for()");
            super.exitFlow_for(ctx);
        }

        @Override
        public void enterOutputExpression(RythmParser.OutputExpressionContext ctx) {
            super.enterOutputExpression(ctx);
            System.out.println("enterOutputExpression()");
        }

        @Override
        public void exitOutputExpression(RythmParser.OutputExpressionContext ctx) {
            System.out.println("exitOutputExpression()");
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

        /*
         * FIXME. See mail Miel
         */
        private String getClassName(String text) {
            if (!text.contains(".")) {
                return "java.lang." + text;
            } else {
                return text;
            }
        }
    }
}
