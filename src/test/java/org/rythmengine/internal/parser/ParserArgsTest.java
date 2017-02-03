package org.rythmengine.internal.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import org.rythmengine.TestBase;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by igmar on 15/07/16.
 */
public class ParserArgsTest extends TestBase {
    @Test
    public void testArgs1() {
        InputStream is = loadTemplate("args/args1.html");
        String expected = loadFile("args/args1.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(0, parser.getNumberOfSyntaxErrors());
        assertEquals(expected, actual);
    }

    @Test
    public void testArgs2() {
        InputStream is = loadTemplate("args/args2.html");
        String expected = loadFile("args/args2.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(0, parser.getNumberOfSyntaxErrors());
        assertEquals(expected, actual);
    }

    @Test
    public void testArgs3() {
        InputStream is = loadTemplate("args/args3.html");
        String expected = loadFile("args/args3.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(0, parser.getNumberOfSyntaxErrors());
        assertEquals(expected, actual);
    }

    @Test
    public void testArgs4() {
        InputStream is = loadTemplate("args/args4.html");
        String expected = loadFile("args/args4.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(0, parser.getNumberOfSyntaxErrors());
        assertEquals(expected, actual);
    }
}
