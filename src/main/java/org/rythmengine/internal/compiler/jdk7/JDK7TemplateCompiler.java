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

    public JDK7TemplateCompiler(final RythmConfiguration configuration, final List<ParsedTemplate> parsedTemplates) throws RythmCompileException {
        super(configuration, parsedTemplates);
        initialised();

    }

    private void initialised() throws RythmCompileException {
        compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new RythmCompileException("Can't create compiler");
        }

        if (!configuration.getTemplatedir().exists()) {
            if (!configuration.getTemplatedir().mkdir()) {
                throw new RythmCompileException(String.format("Can't create template dir %s", configuration.getTemplatedir().getAbsolutePath()));
            }
        }
    }

    @Override
    public Map<ParsedTemplate, CompiledTemplate> call() throws Exception {
        logger.debug("Compiling templates");
        final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        final List<JavaSource> units = new ArrayList<>();
        for (ParsedTemplate pt : this.sources) {
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
            StringBuilder sb = new StringBuilder();
            List<Diagnostic<? extends JavaFileObject>> errors = diagnostics.getDiagnostics();
            for (Diagnostic<? extends JavaFileObject> error : errors) {
                sb.append(String.format("%s at line %s\n", error.getMessage(Locale.getDefault()), error.getLineNumber()));
            }
            logger.error("%s", sb.toString());
            throw new RythmCompileException(sb.toString());
        }

        Map<ParsedTemplate, CompiledTemplate> t = new HashMap<>();
        for (ParsedTemplate pt : this.sources) {
            CompiledTemplate ct = new CompiledTemplate(pt);
            t.put(pt, ct);
        }

        return t;
    }

    class JavaSource extends SimpleJavaFileObject {
        private final ParsedTemplate parsedTemplate;

        protected JavaSource(ParsedTemplate parsedTemplate) {
            super(URI.create("string:///" + parsedTemplate.getCanonicalName().replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
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
