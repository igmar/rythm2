package org.rythmengine.internal.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Ignore;
import org.junit.Test;
import org.rythmengine.TestBase;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by igmar on 19/07/16.
 */
public class FlowForTest extends TestBase {
    @Test
    public void testSimpleFor() {
        InputStream is = loadTemplate("flow_for/flow_for1.html");
        String expected = loadFile("flow_for/flow_for1.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        assertEquals(expected, pt.toStringTree(parser));
    }

    @Test
    public void testEnhancedFor() {
        InputStream is = loadTemplate("flow_for/flow_for2.html");
        String expected = loadFile("flow_for/flow_for2.html.exp");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        assertEquals(expected, pt.toStringTree(parser));
    }
}
