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
package org.rythmengine.internal.debug;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.StringEscapeUtils;
import org.rythmengine.internal.parser.RythmLexer;

import java.util.List;

public final class AntlrDebug {
    private AntlrDebug() {}

    public  static String dumpTerminalNode(TerminalNode tn) {
        final StringBuilder sb = new StringBuilder();
        sb.append("--> TERMNALNODE : ").append(tn.hashCode()).append("\n");
        sb.append("class           : ").append(tn.getClass().getSimpleName()).append("\n");
        sb.append("source interval : ").append(tn.getSourceInterval()).append("\n");
        sb.append("text            : '").append(tn.getText()).append("'\n");
        sb.append("child count     : ").append(tn.getChildCount()).append("\n");
        sb.append("symbol          : ").append(tn.getSymbol()).append("\n");
        sb.append("<-- TERMNALNODE : ").append(tn.hashCode()).append("\n");
        return sb.toString();
    }

    public static String dumpRuleContext(RuleContext rc) {
        final StringBuilder sb = new StringBuilder();

        sb.append("--> RULECONTEXT : ").append(rc.hashCode()).append("\n");
        sb.append("class           : ").append(rc.getClass().getSimpleName()).append("\n");
        sb.append("depth           : ").append(rc.depth()).append("\n");
        sb.append("alt number      : ").append(rc.getAltNumber()).append("\n");
        sb.append("child count     : ").append(rc.getChildCount()).append("\n");
        sb.append("rule index      : ").append(rc.getRuleIndex()).append("\n");
        sb.append("text            : '").append(rc.getText()).append("'\n");
        sb.append("source interval : ").append(rc.getSourceInterval()).append("\n");
        sb.append("<-- RULECONTEXT : ").append(rc.hashCode()).append("\n");
        return sb.toString();
    }

    public static String dumpToken(Token t) {
        final StringBuilder sb = new StringBuilder();
        sb.append("--> TOKEN      : ").append(t.hashCode()).append("\n");
        sb.append("class          : ").append(t.getClass().getSimpleName()).append("\n");
        sb.append("channel        : ").append(t.getChannel()).append("\n");
        sb.append("line           : ").append(t.getLine()).append("\n");
        sb.append("linepos        : ").append(t.getCharPositionInLine()).append("\n");
        sb.append("start idx      : ").append(t.getStartIndex()).append("\n");
        sb.append("stop  idx      : ").append(t.getStopIndex()).append("\n");
        sb.append("token idx      : ").append(t.getTokenIndex()).append("\n");
        sb.append("type           : ").append(RythmLexer.VOCABULARY.getSymbolicName(t.getType())).append("\n");
        sb.append("text           : '").append(t.getText()).append("'\n");
        sb.append("<-- TOKEN      : ").append(t.hashCode()).append("\n");

        return sb.toString();
    }

    public static String dumpTokens(List<Token> tokens) {
        final StringBuilder sb = new StringBuilder();

        for (Token t : tokens) {
            if (t.getType() == -1)
                break;
            sb.append(String.format("<%s(%s) %s '%s'> ", t.getType(), RythmLexer.ruleNames[t.getType() - 1], t.getChannel() == RythmLexer.HIDDEN ? "H" : "N", StringEscapeUtils.escapeJava(t.getText())));
        }

        return sb.toString();
    }
}
