package org.rythmengine.internal.compiler;

import org.rythmengine.conf.RythmConfiguration;
import org.rythmengine.internal.exceptions.RythmCompileException;
import org.rythmengine.internal.parser.ParsedTemplate;
import org.rythmengine.template.TemplateBase;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public final class CompiledTemplateLoader {
    private final RythmConfiguration configuration;
    private final ClassLoader classLoader;

    public CompiledTemplateLoader(final RythmConfiguration configuration, final ClassLoader classLoader) {
        this.configuration = configuration;
        this.classLoader = classLoader;
    }

    public CompiledTemplate load(final ParsedTemplate pt) {
        File baseDir = configuration.getTemplatedir();
        if (!baseDir.exists()) {
            throw new RythmCompileException("Can't find compiled templates basedir");
        }

        try {
            final URL[] urls = new URL[]{ baseDir.toURI().toURL() };
            final ClassLoader cl = new URLClassLoader(urls, classLoader);
            Class<?> c = cl.loadClass(pt.getName());
            if (c != TemplateBase.class && !TemplateBase.class.isAssignableFrom(c)) {
                throw new RythmCompileException("Loaded class does not extends TemplateBase");
            }
            final Class<? extends TemplateBase> ct = (Class<? extends TemplateBase>) c;
            return new CompiledTemplate(pt, ct);
        } catch (MalformedURLException | ClassNotFoundException e) {
            throw new RythmCompileException(e);
        }
    }

    
}
