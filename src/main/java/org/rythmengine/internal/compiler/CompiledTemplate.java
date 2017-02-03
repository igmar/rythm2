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
                throw new RythmCompileException(
                        String.format("%s is not a subclass of TemplateBase.class", this.c.getCanonicalName())
                );
            }
        } catch (ClassNotFoundException e) {
            throw new RythmCompileException(String.format("Can't load compiled class %s", pt.getCanonicalName()));
        }
    }

    public String path() {
        return path;
    }

    public String source() {
        return source;
    }

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
