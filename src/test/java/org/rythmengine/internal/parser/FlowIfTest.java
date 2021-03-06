package org.rythmengine.internal.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import org.rythmengine.TestBase;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by igmar on 18/07/16.
 */
public class FlowIfTest extends TestBase {
    @Test
    public void testEmptyBlock() {
        InputStream is = loadTemplate("flow_if/flow_if1.html");
        String expected = loadFile("flow_if/flow_if1.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(0, parser.getNumberOfSyntaxErrors());
        assertEquals(expected, actual);
    }

    @Test
    public void testSimpleIf() {
        InputStream is = loadTemplate("flow_if/flow_if2.html");
        String expected = loadFile("flow_if/flow_if2.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(0, parser.getNumberOfSyntaxErrors());
        assertEquals(expected, actual);
    }

    @Test
    public void testNestedIf() {
        InputStream is = loadTemplate("flow_if/flow_if3.html");
        String expected = loadFile("flow_if/flow_if3.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(0, parser.getNumberOfSyntaxErrors());
        assertEquals(expected, actual);
    }

    @Test
    public void testElse() {
        InputStream is = loadTemplate("flow_if/flow_if4.html");
        String expected = loadFile("flow_if/flow_if4.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        String actual = pt.toStringTree(parser);
        assertEquals(0, parser.getNumberOfSyntaxErrors());
        assertEquals(expected, actual);
    }

    @Test(expected=org.rythmengine.internal.exceptions.RythmParserException.class)
    public void testBadElse() {
        InputStream is = loadTemplate("flow_if/flow_if5.html");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        assertEquals(1, parser.getNumberOfSyntaxErrors());
    }
}
