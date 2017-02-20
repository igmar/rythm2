package org.rythmengine.internal.generator;

import org.junit.Test;
import org.rythmengine.RythmEngine;
import org.rythmengine.TestBase;
import org.rythmengine.conf.RythmConfiguration;
import org.rythmengine.internal.compiler.CompiledTemplate;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class GeneratorJava7Test extends TestBase {
    @Test
    public void generatorTest1() {
        RythmConfiguration config = createRythmConfiguration();
        RythmEngine engine = new RythmEngine(config);

        InputStream is = loadTemplate("generator/generator1.html");
        String expected = loadFileRaw("generator/generator1.java.generated");
        CompiledTemplate result = engine.compile("generator/generator1.html", is);
        String actual = result.source();

        assertEquals(expected, actual);
    }

    @Test
    public void generatorTest2() {
        RythmConfiguration config = createRythmConfiguration();
        RythmEngine engine = new RythmEngine(config);

        InputStream is = loadTemplate("generator/generator2.html");
        String expected = loadFileRaw("generator/generator2.java.generated");
        CompiledTemplate result = engine.compile("generator/generator2.html", is);
        String actual = result.source();

        assertEquals(expected, actual);
    }

    @Test
    public void generatorTest3() {
        RythmConfiguration config = createRythmConfiguration();
        RythmEngine engine = new RythmEngine(config);

        InputStream is = loadTemplate("generator/generator3.html");
        String expected = loadFileRaw("generator/generator3.java.generated");
        CompiledTemplate result = engine.compile("generator/generator3.html", is);
        String actual = result.source();

        assertEquals(expected, actual);
    }

    @Test
    public void generatorTest4() {
        RythmConfiguration config = createRythmConfiguration();
        RythmEngine engine = new RythmEngine(config);

        InputStream is = loadTemplate("generator/generator4.html");
        String expected = loadFileRaw("generator/generator4.java.generated");
        CompiledTemplate result = engine.compile("generator/generator4.html", is);
        String actual = result.source();

        assertEquals(expected, actual);
    }

    @Test
    public void generatorTest5() {
        RythmConfiguration config = createRythmConfiguration();
        RythmEngine engine = new RythmEngine(config);

        InputStream is = loadTemplate("generator/generator5.html");
        String expected = loadFileRaw("generator/generator5.java.generated");
        CompiledTemplate result = engine.compile("generator/generator5.html", is);
        String actual = result.source();

        assertEquals(expected, actual);
    }

    @Test
    public void generatorTest6() {
        RythmConfiguration config = createRythmConfiguration();
        RythmEngine engine = new RythmEngine(config);

        InputStream is = loadTemplate("generator/generator6.html");
        String expected = loadFileRaw("generator/generator6.java.generated");
        CompiledTemplate result = engine.compile("generator/generator6.html", is);
        String actual = result.source();

        assertEquals(expected, actual);
    }
}
