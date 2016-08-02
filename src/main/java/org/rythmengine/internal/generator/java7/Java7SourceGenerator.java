package org.rythmengine.internal.generator.java7;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.io.IOUtils;
import org.rythmengine.internal.ILogger;
import org.rythmengine.internal.exceptions.RythmGenerateException;
import org.rythmengine.internal.generator.ISourceGenerator;
import org.rythmengine.internal.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by igmar on 22/07/16.
 */
public class Java7SourceGenerator implements ISourceGenerator {
    private static ILogger logger = Logger.get(Java7SourceGenerator.class);

    @Override
    public String generateSource(ParseTree pt, TokenStream tokenStream) throws RythmGenerateException {
        assert pt != null;
        final StringBuffer sb = new StringBuffer();
        logger.info("generateSource()");
        processNode(sb, pt, tokenStream);

        return null;
    }

    private void processNode(final StringBuffer sb, final Object node, TokenStream tokenStream) throws RythmGenerateException {
        logger.info("Processing type '%s'", node.getClass().getCanonicalName());
        if (node instanceof Token) {
            Token t = (Token) node;
            System.out.println(dumpToken(t));
        } else if (node instanceof RuleContext) {
            RuleContext rc = (RuleContext) node;
            System.out.println(dumpRuleContext(rc));
            for (int i = 0; i < rc.getChildCount(); i++) {
                processNode(sb, rc.getChild(i), tokenStream);
            }
            processRuleContext(sb, rc, tokenStream);
        } else if (node instanceof TerminalNode) {
            TerminalNode tn = (TerminalNode) node;
            System.out.println(dumpTerminalNode(tn));
        }
        else {
            final String msg = String.format("Node is a '%s', we can't process that", node.getClass().getCanonicalName());
            throw new RythmGenerateException(msg);
        }
    }

    private <U extends RuleContext> void processRuleContext(final StringBuffer sb, final U rc, final TokenStream tokenStream) {
        final String className = rc.getClass().getSimpleName();

        switch (className) {
            case "TemplateContext":
                // Start of the template
                break;
            case "ElementsContext":
                break;
            case "Flow_ifContext":
            case "Flow_forContext":
                //dumpRuleContext(rc);
                break;
            default:
                //System.out.println(String.format("Ignoring '%s'", className));
                break;
        }

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

    private String dumpTerminalNode(TerminalNode tn) {
        final StringBuilder sb = new StringBuilder();
        sb.append("--> TERMNALNODE : ").append(tn.hashCode()).append("\n");
        sb.append("class           : ").append(tn.getClass().getSimpleName()).append("\n");
        sb.append("source interval : ").append(tn.getSourceInterval()).append("\n");
        sb.append("text            : ").append(tn.getText()).append("\n");
        sb.append("child count     : ").append(tn.getChildCount()).append("\n");
        sb.append("symbol          : ").append(tn.getSymbol()).append("\n");
        sb.append("<-- TERMNALNODE : ").append(tn.hashCode()).append("\n");
        return sb.toString();
    }

    private String dumpRuleContext(RuleContext rc) {
        final StringBuilder sb = new StringBuilder();

        sb.append("--> RULECONTEXT : ").append(rc.hashCode()).append("\n");
        sb.append("class           : ").append(rc.getClass().getSimpleName()).append("\n");
        sb.append("depth           : ").append(rc.depth()).append("\n");
        sb.append("alt number      : ").append(rc.getAltNumber()).append("\n");
        sb.append("child count     : ").append(rc.getChildCount()).append("\n");
        sb.append("rule index      : ").append(rc.getRuleIndex()).append("\n");
        sb.append("text            : ").append(rc.getText()).append("\n");
        sb.append("source interval : ").append(rc.getSourceInterval()).append("\n");
        sb.append("<-- RULECONTEXT : ").append(rc.hashCode()).append("\n");
        return sb.toString();
    }

    private String dumpToken(Token t) {
        final StringBuilder sb = new StringBuilder();
        sb.append("--> TOKEN      : ").append(t.hashCode()).append("\n");
        sb.append("class          : ").append(t.getClass().getSimpleName()).append("\n");
        sb.append("channel        : ").append(t.getChannel()).append("\n");
        sb.append("line           : ").append(t.getLine()).append("\n");
        sb.append("linepos        : ").append(t.getCharPositionInLine()).append("\n");
        sb.append("start idx      : ").append(t.getStartIndex()).append("\n");
        sb.append("stop  idx      : ").append(t.getStopIndex()).append("\n");
        sb.append("token idx      : ").append(t.getTokenIndex()).append("\n");
        sb.append("type           : ").append(t.getType()).append("\n");
        sb.append("text           : ").append(t.getText()).append("\n");
        sb.append("<-- TOKEN      : ").append(t.hashCode()).append("\n");

        return sb.toString();
    }
}
