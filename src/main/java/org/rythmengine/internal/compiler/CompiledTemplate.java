package org.rythmengine.internal.compiler;

import org.rythmengine.internal.IHttpContext;
import org.rythmengine.internal.exceptions.RythmCompileException;
import org.rythmengine.internal.exceptions.RythmTemplateException;
import org.rythmengine.internal.hash.sha1.SHA1;
import org.rythmengine.internal.parser.ParsedTemplate;
import org.rythmengine.template.TemplateBase;

public final class CompiledTemplate {
    private String path;
    private String source;
    private String hash;
    private Class<?> c;
    private ClassLoader cl;

    public CompiledTemplate(final ParsedTemplate pt) {
        this.path = pt.path();
        this.source = pt.source();
        this.hash = SHA1.sha1Hex(this.source);

        // Load classfile
        cl = Thread.currentThread().getContextClassLoader();
        try {
            this.c = cl.loadClass(pt.getCanonicalName());
            if (!this.c.isAssignableFrom(TemplateBase.class)) {
                throw new RythmCompileException(String.format("%s is not a subclass of TemplateBase.class", this.c.getCanonicalName()));
            }
        } catch (ClassNotFoundException e) {
            throw new RythmCompileException(String.format("Can't load compiled class %s", pt.getCanonicalName()));
        }
    }

    public String path() {
        return path;
    }

    public String source() { return source; }

    public String hash() {
        return hash;
    }

    public String execute(IHttpContext context) {
        try {
            TemplateBase t = (TemplateBase) c.newInstance();
            return t.execute(context);
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RythmTemplateException("Can't instantiate class");
        }
    }
}
