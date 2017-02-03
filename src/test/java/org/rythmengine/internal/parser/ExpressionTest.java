package org.rythmengine.internal.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import org.rythmengine.TestBase;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by igmar on 20/07/16.
 */
public class ExpressionTest extends TestBase {
    @Test
    public void testInstanceField() {
        InputStream is = loadTemplate("expression/expression1.html");
        String expected = loadFile("expression/expression1.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(0, parser.getNumberOfSyntaxErrors());
        assertEquals(expected, actual);
    }

    @Test
    public void testInstanceMethod() {
        InputStream is = loadTemplate("expression/expression2.html");
        String expected = loadFile("expression/expression2.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(0, parser.getNumberOfSyntaxErrors());
        assertEquals(expected, actual);
    }

    @Test
    public void testCInstanceMethod() {
        InputStream is = loadTemplate("expression/expression3.html");
        String expected = loadFile("expression/expression3.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(0, parser.getNumberOfSyntaxErrors());
        assertEquals(expected, actual);
    }

    @Test
    public void testCInstanceMethodWithArgs() {
        InputStream is = loadTemplate("expression/expression4.html");
        String expected = loadFile("expression/expression4.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(0, parser.getNumberOfSyntaxErrors());
        assertEquals(expected, actual);
    }

    @Test
    public void testInstanceMethodWithArgs() {
        InputStream is = loadTemplate("expression/expression5.html");
        String expected = loadFile("expression/expression5.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(0, parser.getNumberOfSyntaxErrors());
        assertEquals(expected, actual);
    }

    @Test
    public void testInstanceFieldMethodWithArgs() {
        InputStream is = loadTemplate("expression/expression6.html");
        String expected = loadFile("expression/expression6.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(0, parser.getNumberOfSyntaxErrors());
        assertEquals(expected, actual);
    }
}
