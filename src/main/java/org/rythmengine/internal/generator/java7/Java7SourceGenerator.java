package org.rythmengine.internal.generator.java7;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.rythmengine.internal.ILogger;
import org.rythmengine.internal.exceptions.RythmGenerateException;
import org.rythmengine.internal.exceptions.RythmParserException;
import org.rythmengine.internal.generator.ISourceGenerator;
import org.rythmengine.internal.logger.Logger;
import org.rythmengine.internal.parser.RythmLexer;
import org.rythmengine.internal.parser.RythmParser;
import org.rythmengine.internal.parser.RythmParserBaseListener;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Java7SourceGenerator implements ISourceGenerator {
    private static ILogger logger = Logger.get(Java7SourceGenerator.class);
    private Map<String, String> drainFunctions;
    private List<String> macros;
    private Map<String, Class<?>> args;

    public Java7SourceGenerator() {
        drainFunctions = new HashMap<>();
        macros = new ArrayList<>();
        args = new HashMap<>();

    }

    @Override
    public String generateSource(String identifier, ParseTree pt, TokenStream tokenStream) throws RythmGenerateException {
        assert identifier != null;
        assert pt != null;
        final StringBuffer flowsb = new StringBuffer();
        logger.info("generateSource()");

        ParseTreeWalker walker = new ParseTreeWalker();
        Java7Listener listener = new Java7Listener(flowsb, tokenStream);
        walker.walk(listener, pt);

        final String imports = generateImports();
        final String className = generateClassName(identifier);
        final String drains = generateDrains();

        final String template = loadTemplate()
                .replace("@@IMPORTS@@", imports)
                .replace("@@CLASSNAME@@", className)
                .replace("@@FLOW@@", flowsb.toString())
                .replace("@@DRAINS@@", drains);

        System.out.println(template);

        return null;
    }

    private String generateClassName(String path) {
        return String.format("ct_%s", path.replace("/", "_").replace(".", "_"));
    }

    private String loadTemplate() throws RythmGenerateException {
        InputStream tpl = this.getClass().getClassLoader().getResourceAsStream("templates/Template.java");
        if (tpl == null) {
            throw new RythmGenerateException("Can't find generator template");
        }

        try {
            return IOUtils.toString(tpl, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RythmGenerateException(e);
        }
    }

    private String generateImports() {
        StringBuilder tsb = new StringBuilder();
        for (Map.Entry<String, Class<?>> entry : args.entrySet()) {
            tsb.append("import ").append(entry.getValue().getCanonicalName()).append(";\n");
        }
        return tsb.toString();
    }

    private String generateDrains() {
        StringBuilder tsb = new StringBuilder();

        for (Map.Entry<String, String> entry : drainFunctions.entrySet()) {
            tsb.append("private void ").append(entry.getKey()).append("() {\n");
            tsb.append("\t\tsb.append(\"").append(entry.getValue()).append("\");\n");
            tsb.append("\t}\n");
        }

        return tsb.toString();
    }


    private class Java7Listener extends RythmParserBaseListener {
        private StringBuffer sb;
        private TokenStream tokenStream;
        private int contentStart = 0;

        public Java7Listener(StringBuffer sb, TokenStream tokenStream) {
            this.sb = sb;
            this.tokenStream = tokenStream;
        }

        @Override
        public void enterElements(RythmParser.ElementsContext ctx) {
            drainContentNodes(ctx);
        }

        @Override
        public void enterDoubleat(RythmParser.DoubleatContext ctx) {
            sb.append("emitAt();\n");
        }

        @Override
        public void enterArgs(RythmParser.ArgsContext ctx) {
            /*
             * This handles the @args : We parse those,
             * lookup the class, and put the result in the args map
             */
            for (RythmParser.TemplateArgumentContext arg : ctx.templateArgument()) {
                final Token type = arg.getStart();
                final TerminalNode name = arg.IDENTIFIER();
                final Class<?> typeRef;
                final String fqn = getClassName(type.getText());

                try {
                    typeRef = Class.forName(fqn);
                } catch (ClassNotFoundException e) {
                    throw new RythmParserException(e);
                }
                args.put(name.getText(), typeRef);
            }
        }

        @Override
        public void enterFlow_if(RythmParser.Flow_ifContext ctx) {
            sb.append("\tif ").append(ctx.boolExpression().getText()).append(" {\n");
            sb.append("\t}\n");
            sb.append("CNT : ").append(ctx.block().size());
        }

        /*
         * Helper methods
         */
        private void drainContentNodes(final RythmParser.ElementsContext ctx) {
            final Token start = ctx.getStart();
            final StringBuilder tsb = new StringBuilder();
            int inc = 0;
            int end = start.getTokenIndex();
            int linestart = -1;
            for (int i = contentStart; i <= end; i++, inc++) {
                Token t = tokenStream.get(i);
                if (t.getTokenIndex() == start.getTokenIndex())
                    continue;
                if (t.getChannel() != RythmLexer.TemplateData)
                    continue;
                if (t.getType() != RythmLexer.CONTENT)
                    break;
                if (linestart == -1)
                    linestart = t.getLine();
                tsb.append(t.getText().replace("\r", "\\r").replace("\n", "\\n"));
            }
            if (tsb.length() > 0) {
                final String functionName = String.format("drain_%s", linestart);
                drainFunctions.put(functionName, tsb.toString());
                sb.append(functionName).append("();\n");
            }
        }

        /*
         * FIXME
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
