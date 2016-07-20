package org.rythmengine.internal.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Ignore;
import org.junit.Test;
import org.rythmengine.TestBase;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by igmar on 20/07/16.
 */
public class ExpressionTest extends TestBase {
    @Test
    @Ignore
    public void testInstanceField() {
        InputStream is = loadTemplate("expression/expression1.html");
        String expected = loadFile("expression/expression1.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        assertEquals(expected, pt.toStringTree(parser));
    }

    @Test
    @Ignore
    public void testInstanceMethod() {
        InputStream is = loadTemplate("expression/expression2.html");
        String expected = loadFile("expression/expression2.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        assertEquals(expected, pt.toStringTree(parser));
    }
}
