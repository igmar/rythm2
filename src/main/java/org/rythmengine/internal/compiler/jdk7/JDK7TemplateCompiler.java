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
package org.rythmengine.internal.compiler.jdk7;

import org.rythmengine.conf.RythmConfiguration;
import org.rythmengine.internal.ILogger;
import org.rythmengine.internal.compiler.CompiledTemplate;
import org.rythmengine.internal.compiler.TemplateCompiler;
import org.rythmengine.internal.exceptions.RythmCompileException;
import org.rythmengine.internal.logger.Logger;
import org.rythmengine.internal.parser.ParsedTemplate;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.StringWriter;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JDK7TemplateCompiler extends TemplateCompiler {
    private static ILogger logger = Logger.get(JDK7TemplateCompiler.class);
    private JavaCompiler compiler;

    public JDK7TemplateCompiler(final RythmConfiguration configuration, final List<ParsedTemplate> parsedTemplates, final ClassLoader classLoader) throws RythmCompileException {
        super(configuration, parsedTemplates, classLoader);
        initialised();
    }

    private void initialised() throws RythmCompileException {
        compiler = ToolProvider.getSystemJavaCompiler();

        if (compiler == null) {
            throw new RythmCompileException("Can't create compiler");
        }

        if (!configuration.getTemplatedir().exists()) {
            if (!configuration.getTemplatedir().mkdir()) {
                throw new RythmCompileException(
                        String.format("Can't create template dir %s", configuration.getTemplatedir().getAbsolutePath())
                );
            }
        }
    }

    @Override
    public Map<ParsedTemplate, CompiledTemplate> call() throws Exception {
        logger.debug("Compiling templates");
        final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        final List<JavaSource> units = new ArrayList<>();
        for (ParsedTemplate pt : this.sources) {
            logger.debug("Adding class for compilation : %s%n", pt.getName());
            units.add(new JavaSource(pt));
        }

        final StringWriter sw = new StringWriter();
        final StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, Locale.ENGLISH, Charset.defaultCharset());
        final List<String> options = new ArrayList<>();

        options.add("-d");
        options.add(configuration.getTemplatedir().getAbsolutePath());
        final JavaCompiler.CompilationTask task = compiler.getTask(sw, fileManager, diagnostics, options, null, units);
        final Boolean success = task.call();

        if (!success) {
            /*
             * We should never get here : If we do, the parser + postprocessor failed to handle this case, and
             * give a decent error about this.
             */
            final StringBuilder sb = new StringBuilder();
            final List<Diagnostic<? extends JavaFileObject>> errors = diagnostics.getDiagnostics();
            for (Diagnostic<? extends JavaFileObject> error : errors) {
                switch (error.getKind()) {
                    case ERROR:
                        sb.append(String.format("%s at line %s%n", error.getMessage(Locale.getDefault()), error.getLineNumber()));
                        break;
                    case WARNING:
                    case MANDATORY_WARNING:
                    case NOTE:
                    case OTHER:
                        break;
                }
            }
            logger.error("%s", sb.toString());
            throw new RythmCompileException(sb.toString());
        }

        final Map<ParsedTemplate, CompiledTemplate> t = new HashMap<>();
        for (ParsedTemplate pt : this.sources) {
            CompiledTemplate ct = templateLoader.load(pt);
            t.put(pt, ct);
        }

        return t;
    }

    static class JavaSource extends SimpleJavaFileObject {
        private final ParsedTemplate parsedTemplate;

        JavaSource(ParsedTemplate parsedTemplate) {
            super(URI.create("string:///" + parsedTemplate.getName().replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.parsedTemplate = parsedTemplate;
        }

        protected ParsedTemplate getParsedTemplate() {
            return parsedTemplate;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return parsedTemplate.getGeneratedSource();
        }
    }
}
