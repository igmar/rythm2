package org.rythmengine.internal.executor;

import org.junit.Test;
import org.rythmengine.RythmEngine;
import org.rythmengine.TestBase;
import org.rythmengine.conf.RythmConfiguration;
import org.rythmengine.internal.IHttpContext;
import org.rythmengine.internal.compiler.CompiledTemplate;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ExecutorTest extends TestBase {
    @Test
    public void executorTest1() {
        RythmConfiguration config = createRythmConfiguration();
        RythmEngine engine = new RythmEngine(config);

        Map<String, Object> args = new HashMap<>();
        args.put("foo", Boolean.TRUE);
        args.put("bar", Boolean.FALSE);
        args.put("gecko", "This is a string");
        IHttpContext httpContext = new IHttpContext() {
        };

        InputStream is = loadTemplate("executor/executor1.html");
        String expected = loadFileRaw("executor/executor1.html.exp");
        CompiledTemplate result = engine.compile("executor/executor1.html", is);
        String actual = result.execute(httpContext, args);

        assertEquals(expected, actual);
    }

    @Test
    public void executorTest2() {
        RythmConfiguration config = createRythmConfiguration();
        RythmEngine engine = new RythmEngine(config);

        Map<String, Object> args = new HashMap<>();
        args.put("foo", Boolean.TRUE);
        args.put("bar", Boolean.FALSE);
        args.put("gecko", "This is a string");
        IHttpContext httpContext = new IHttpContext() {
        };

        InputStream is = loadTemplate("executor/executor2.html");
        String expected = loadFileRaw("executor/executor2.html.exp");
        CompiledTemplate result = engine.compile("executor/executor2.html", is);
        String actual = result.execute(httpContext, args);

        assertEquals(expected, actual);
    }
}
