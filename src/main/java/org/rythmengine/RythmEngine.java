package org.rythmengine;

import org.rythmengine.conf.RythmConfiguration;
import org.rythmengine.internal.compiler.CompiledTemplate;
import org.rythmengine.internal.exceptions.RythmCompileException;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class RythmEngine implements AutoCloseable {
    private RythmConfiguration configuration;
    private String id;
    private ExecutorService compilePool;
    private ConcurrentMap<String, CompiledTemplate> templates;

    public RythmEngine(RythmConfiguration configuration) {
        this.configuration = configuration;
        this.id = configuration.getId();
        compilePool = Executors.newCachedThreadPool();
        templates = new ConcurrentHashMap<>();
    }

    public RythmConfiguration getConfiguration() {
        return configuration;
    }

    public String getId() {
        return id;
    }

    public CompiledTemplate compile(File input) throws RythmCompileException {
        return null;
    }

    public CompiledTemplate compile(String input) throws RythmCompileException {
        return null;
    }

    public CompiledTemplate compile(InputStream input) throws RythmCompileException {
        return null;
    }

    @Override
    public void close() throws Exception {
        compilePool.shutdown();
        // FIXME : Is this OK ?
        compilePool.awaitTermination(1, TimeUnit.SECONDS);
        templates.clear();
    }
}
