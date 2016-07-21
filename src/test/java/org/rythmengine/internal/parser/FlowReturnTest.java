package org.rythmengine.internal.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import org.rythmengine.TestBase;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by igmar on 21/07/16.
 */
public class FlowReturnTest extends TestBase {
    @Test
    public void singleReturnTest() {
        InputStream is = loadTemplate("flow_return/flow_return1.html");
        String expected = loadFile("flow_return/flow_return1.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        assertEquals(0, parser.getNumberOfSyntaxErrors());
        assertEquals(expected, pt.toStringTree(parser));
    }

    @Test
    public void insideIfReturnTest() {
        InputStream is = loadTemplate("flow_return/flow_return2.html");
        String expected = loadFile("flow_return/flow_return2.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        assertEquals(0, parser.getNumberOfSyntaxErrors());
        assertEquals(expected, pt.toStringTree(parser));
    }

    @Test
    public void returnIfTest() {
        InputStream is = loadTemplate("flow_return/flow_return3.html");
        String expected = loadFile("flow_return/flow_return3.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        assertEquals(0, parser.getNumberOfSyntaxErrors());
        assertEquals(expected, pt.toStringTree(parser));
    }
}
