package org.rythmengine.internal.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import org.rythmengine.TestBase;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class ExpressionTest extends TestBase {
    @Test
    public void testInstanceField() {
        InputStream is = loadTemplate("expression/expression1.html");
        String expected = loadFile("expression/expression1.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(expected, actual);
    }

    @Test
    public void testInstanceMethod() {
        InputStream is = loadTemplate("expression/expression2.html");
        String expected = loadFile("expression/expression2.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(expected, actual);
    }

    @Test
    public void testCInstanceMethod() {
        InputStream is = loadTemplate("expression/expression3.html");
        String expected = loadFile("expression/expression3.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(expected, actual);
    }

    @Test
    public void testCInstanceMethodWithArgs() {
        InputStream is = loadTemplate("expression/expression4.html");
        String expected = loadFile("expression/expression4.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(expected, actual);
    }

    @Test
    public void testInstanceMethodWithArgs() {
        InputStream is = loadTemplate("expression/expression5.html");
        String expected = loadFile("expression/expression5.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(expected, actual);
    }

    @Test
    public void testInstanceFieldMethodWithArgs() {
        InputStream is = loadTemplate("expression/expression6.html");
        String expected = loadFile("expression/expression6.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(expected, actual);
    }

    @Test
    public void testDoubleReferenceOutputExpression() {
        InputStream is = loadTemplate("expression/expression7.html");
        String expected = loadFile("expression/expression7.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(expected, actual);
    }

    @Test
    public void testDoubleMethodReferenceOutputExpression() {
        InputStream is = loadTemplate("expression/expression8.html");
        String expected = loadFile("expression/expression8.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(expected, actual);
    }

    @Test
    public void testDoubleMethodReferenceWithArgsOutputExpression() {
        InputStream is = loadTemplate("expression/expression9.html");
        String expected = loadFile("expression/expression9.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(expected, actual);
    }

    @Test
    public void testCOEDoubleMethodReferenceWithArgsOutputExpression() {
        InputStream is = loadTemplate("expression/expression10.html");
        String expected = loadFile("expression/expression10.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(expected, actual);
    }

    @Test
    public void testMixedDefeferenceOutputExpression1() {
        InputStream is = loadTemplate("expression/expression11.html");
        String expected = loadFile("expression/expression11.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(expected, actual);
    }

    @Test
    public void testMixedDefeferenceOutputExpression2() {
        InputStream is = loadTemplate("expression/expression12.html");
        String expected = loadFile("expression/expression12.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(expected, actual);
    }
}
