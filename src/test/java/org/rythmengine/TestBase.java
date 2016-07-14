package org.rythmengine;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.io.IOUtils;
import org.rythmengine.internal.parser.RythmLexer;
import org.rythmengine.internal.parser.RythmParser;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by igmar on 14/07/16.
 */
public class TestBase {
    public InputStream loadTemplate(String template) {
        InputStream tpl = this.getClass().getClassLoader().getResourceAsStream(template);
        if (tpl == null) {
            return null;
        }

        return tpl;
    }

    public RythmParser createParser(InputStream is) {
        try {
            CharStream input = new ANTLRInputStream(is);
            RythmLexer lexer = new RythmLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            RythmParser parser = new RythmParser(tokens);

            return parser;
        } catch (IOException e) {
            return null;
        }
    }
}
