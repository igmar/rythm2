/*
 * Copyright (c) 2016-2017, Igmar Palsenberg. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rythmengine;

import org.apache.commons.io.IOUtils;
import org.rythmengine.conf.RythmConfiguration;
import org.rythmengine.conf.RythmEngineMode;
import org.rythmengine.conf.RythmGenerator;
import org.rythmengine.internal.IResourceLoader;
import org.rythmengine.internal.compiler.CompiledTemplate;
import org.rythmengine.internal.compiler.jdk7.JDK7TemplateCompiler;
import org.rythmengine.internal.exceptions.RythmCompileException;
import org.rythmengine.internal.exceptions.RythmConfigException;
import org.rythmengine.internal.generator.ISourceGenerator;
import org.rythmengine.internal.generator.java7.Java7SourceGenerator;
import org.rythmengine.internal.hash.fnv.FNV;
import org.rythmengine.internal.parser.ParsedTemplate;
import org.rythmengine.internal.parser.TemplateParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class RythmEngine implements AutoCloseable {
    private static Logger logger = LoggerFactory.getLogger(RythmEngine.class);

    private static final Integer DEFAULT_TIMEOUT = 5;
    private RythmConfiguration configuration;
    private String id;
    private ExecutorService compilePool;
    private ExecutorService parsePool;
    private ConcurrentMap<String, CompiledTemplate> templates;
    private ISourceGenerator sourceGenerator;
    private ClassLoader classLoader;

    public RythmEngine(final RythmConfiguration configuration, final ClassLoader classLoader) {
        this.configuration = configuration;
        this.id = configuration.getId();
        this.sourceGenerator = getSourceGenerator(this.configuration.getSourceGenerator());
        compilePool = Executors.newCachedThreadPool();
        parsePool = Executors.newCachedThreadPool();
        templates = new ConcurrentHashMap<>();
        this.classLoader = classLoader == null ? Thread.currentThread().getContextClassLoader() : classLoader;

        logger.info("Starting RythmEngine {}", this.id);
    }

    public RythmEngine(final RythmConfiguration configuration) {
        this(configuration, null);
    }

    public RythmConfiguration getConfiguration() {
        return configuration;
    }

    public String getId() {
        return id;
    }

    public CompiledTemplate compile(final String identifier, final File input) throws RythmCompileException {
        try (InputStream is = new FileInputStream(input)) {
            return compile(identifier, is);
        } catch (IOException e) {
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
                return compileTemplate(identifier, input);
            } else {
                if (configuration.getEngineMode().equals(RythmEngineMode.DEV)) {
                    String inputHash = getHash(input);
                    CompiledTemplate ct = templates.get(identifier);
                    if (ct.path().equals(inputHash)) {
                        return ct;
                    }
                    return compileTemplate(identifier, input);
                } else {
                    return templates.get(identifier);
                }
            }
        } catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
            final String msg = String.format("Compilation of '%s' failed.", identifier);
            throw new RythmCompileException(msg, e);
        }
    }

    public String execute(final IHttpContext context, final Map<String, Object> args, final CompiledTemplate template) {
        return template.execute(context, args);
    }

    @Override
    public void close() throws Exception {
        try {
            parsePool.shutdown();
            parsePool.awaitTermination(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            compilePool.shutdown();
            compilePool.awaitTermination(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            templates.clear();
        } catch (InterruptedException e) {
            // Eat it
        } finally {
            parsePool.shutdownNow();
            compilePool.shutdownNow();
        }
    }

    private ISourceGenerator getSourceGenerator(final RythmGenerator sourceGenerator) {
        switch (sourceGenerator) {
            case JDK7:
                return new Java7SourceGenerator(this.configuration, this.classLoader);
            default:
                throw new RythmConfigException(String.format("%s has no source generator", sourceGenerator.name()));
        }
    }

    private CompiledTemplate compileTemplate(final String identifier, final InputStream is) throws InterruptedException, java
            .util.concurrent.ExecutionException, java.util.concurrent.TimeoutException, IOException {
        final IResourceLoader resourceLoader = this.configuration.getResourceLoaderProvider().get();
        final Future<ParsedTemplate> parseFuture = parsePool.submit(new TemplateParser(identifier, sourceGenerator,
                resourceLoader, is));
        final ParsedTemplate pt = parseFuture.get(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        final Future<Map<ParsedTemplate, CompiledTemplate>> compileFuture = compilePool.submit(new JDK7TemplateCompiler(
                configuration, Collections.singletonList(pt), this.classLoader));
        final Map<ParsedTemplate, CompiledTemplate> ct = compileFuture.get(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        return ct == null ? null : ct.get(pt);
    }

    private String getHash(InputStream is) throws IOException {
        byte[] data = IOUtils.toByteArray(is);
        return FNV.fnv1a_64(data);
    }
}
