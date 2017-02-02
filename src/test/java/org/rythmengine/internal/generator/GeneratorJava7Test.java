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
        System.out.println("Loaded ");
        String expected = loadFile("generator/generator1.java");
        CompiledTemplate result = engine.compile("generator/generator1.html", is);
        String actual = result.source();

        assertEquals(expected, actual);
    }
}
