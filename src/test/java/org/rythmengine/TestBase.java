package org.rythmengine;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.io.IOUtils;
import org.rythmengine.conf.RythmConfiguration;
import org.rythmengine.conf.RythmEngineMode;
import org.rythmengine.internal.parser.RythmLexer;
import org.rythmengine.internal.parser.RythmParser;

import java.io.IOException;
import java.io.InputStream;

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

    public String loadFile(String file) {
        InputStream tpl = this.getClass().getClassLoader().getResourceAsStream(file);
        if (tpl == null) {
            return "";
        }

        try {
            String content = IOUtils.toString(tpl, "UTF-8");
            return content;
        } catch (IOException e) {
            return "";
        }
    }

    public RythmConfiguration createRythmConfiguration() {
        RythmConfiguration configuration = new RythmConfiguration.Builder()
                .engineMode(RythmEngineMode.DEV)
                .homeDir("/tmp")
                .tempDir("/tmp")
                .writeEnabled(true)
                .build();
        return configuration;
    }
}
