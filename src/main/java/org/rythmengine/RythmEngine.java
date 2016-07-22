package org.rythmengine;

import org.apache.commons.io.IOUtils;
import org.rythmengine.conf.RythmConfiguration;
import org.rythmengine.conf.RythmEngineMode;
import org.rythmengine.internal.IHttpContext;
import org.rythmengine.internal.ILogger;
import org.rythmengine.internal.compiler.CompiledTemplate;
import org.rythmengine.internal.compiler.jdk7.JDK7TemplateCompiler;
import org.rythmengine.internal.exceptions.RythmCompileException;
import org.rythmengine.internal.hash.fnv.FNV;
import org.rythmengine.internal.parser.ParsedTemplate;
import org.rythmengine.internal.parser.TemplateParser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class RythmEngine implements AutoCloseable {
    private RythmConfiguration configuration;
    private String id;
    private ExecutorService compilePool;
    private ExecutorService parsePool;
    private ConcurrentMap<String, CompiledTemplate> templates;
    private ILogger iLogger;

    public RythmEngine(final RythmConfiguration configuration) {
        this.configuration = configuration;
        this.id = configuration.getId();
        compilePool = Executors.newCachedThreadPool();
        parsePool = Executors.newCachedThreadPool();
        templates = new ConcurrentHashMap<>();
    }

    public RythmConfiguration getConfiguration() {
        return configuration;
    }

    public String getId() {
        return id;
    }

    public CompiledTemplate compile(final String identifier, final File input) throws RythmCompileException {
        try {
            InputStream is = new FileInputStream(input);
            return compile(identifier, is);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public CompiledTemplate compile(final String identifier, final String input) throws RythmCompileException {
        InputStream is = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        return compile(identifier, is);
    }

    public CompiledTemplate compile(final String identifier, final InputStream input) throws RythmCompileException {
        try {
            if (!templates.containsKey(identifier)) {
                return compileTemplate(input);
            } else {
                if (configuration.getEngineMode().equals(RythmEngineMode.DEV)) {
                    String inputHash = getHash(input);
                    CompiledTemplate ct = templates.get(identifier);
                    if (ct.path().equals(inputHash)) {
                        return ct;
                    }
                    return compileTemplate(input);
                } else {
                    return templates.get(identifier);
                }
            }
        } catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
            throw new RythmCompileException();
        }
    }

    public String execute(final IHttpContext context, final CompiledTemplate template) {
        return template.execute(context);
    }

    @Override
    public void close() throws Exception {
        try {
            parsePool.shutdown();
            parsePool.awaitTermination(5, TimeUnit.SECONDS);
            compilePool.shutdown();
            compilePool.awaitTermination(5, TimeUnit.SECONDS);
            templates.clear();
        } catch (InterruptedException e) {
            // Eat it
        } finally {
            parsePool.shutdownNow();
            compilePool.shutdownNow();
        }
    }

    private CompiledTemplate compileTemplate(InputStream is) throws InterruptedException, java.util.concurrent.ExecutionException, java.util.concurrent.TimeoutException {
        Future<ParsedTemplate> parseFuture = parsePool.submit(new TemplateParser(this.configuration.getResourceLoaderProvider(), is));
        ParsedTemplate pt = parseFuture.get(5, TimeUnit.SECONDS);
        Future<CompiledTemplate> compileFuture = compilePool.submit(new JDK7TemplateCompiler(pt));
        CompiledTemplate ct = compileFuture.get(5, TimeUnit.SECONDS);
        return ct;
    }

    private String getHash(InputStream is) throws IOException {
        byte[] data = IOUtils.toByteArray(is);
        return FNV.fnv1a_64(data);
    }
}
