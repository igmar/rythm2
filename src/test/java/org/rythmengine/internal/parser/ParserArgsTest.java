package org.rythmengine.internal.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Ignore;
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
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        assertEquals("(template (elements (args @ args (templateArgument (qualifiedName FooBar) myfoobar))))", pt.toStringTree(parser));
    }

    @Test
    public void testArgs2() {
        InputStream is = loadTemplate("args/args2.html");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        assertEquals("(template (elements (args @ args (templateArgument (qualifiedName FooBar) myfoobar) , (templateArgument (qualifiedName Integer) myinteger))))", pt.toStringTree(parser));
    }

    @Test
    public void testArgs3() {
        InputStream is = loadTemplate("args/args3.html");
        org.rythmengine.internal.parser.RythmParser parser = createParser(is);
        ParseTree pt = parser.template();
        assertEquals("(template (elements (comment @ // \\n)) (elements (args @ args (templateArgument (qualifiedName FooBar) myfoobar))))", pt.toStringTree(parser));
    }
}
