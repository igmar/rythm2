package org.rythmengine.internal.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import org.rythmengine.TestBase;

import java.io.InputStream;

public class ParserCommmentTest extends TestBase {
    @Test
    public void testComment1() {
        InputStream is = loadTemplate("comment/comment1.html");
        ParseTree pt = createParser(is);
    }

    @Test
    public void testComment2() {
        InputStream is = loadTemplate("comment/comment2.html");
        ParseTree pt = createParser(is);
    }
}
