package org.rythmengine.internal.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Ignore;
import org.rythmengine.internal.parser.RythmParser;
import org.junit.Test;
import org.rythmengine.TestBase;
import java.io.InputStream;
import static org.junit.Assert.assertEquals;

public class ParserCommentTest extends TestBase {
    @Test
    public void testComment1() {
        InputStream is = loadTemplate("comment/comment1.html");
        RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        assertEquals("(template (elements (comment @ // \\n)))", pt.toStringTree(parser));
    }

    @Test
    public void testComment2() {
        InputStream is = loadTemplate("comment/comment2.html");
        RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        assertEquals("(template (elements (comment @ * *@)))", pt.toStringTree(parser));
    }

    @Test
    public void testComment3() {
        InputStream is = loadTemplate("comment/comment3.html");
        RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        assertEquals("(template (elements (comment @ // \\n)) (elements (comment @ // \\n)))", pt.toStringTree(parser));
    }

    @Test
    public void testComment4() {
        InputStream is = loadTemplate("comment/comment4.html");
        RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        assertEquals("(template (elements (comment @ * *@)) (elements (comment @ * *@)))", pt.toStringTree(parser));
    }
}
