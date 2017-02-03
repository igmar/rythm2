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

import org.rythmengine.conf.RythmConfiguration;
import org.rythmengine.internal.exceptions.RythmCompileException;
import org.rythmengine.internal.parser.ParsedTemplate;
import org.rythmengine.template.TemplateBase;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;

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
            final URL[] urls = new URL[] {baseDir.toURI().toURL()};
            final Class<?> c = (Class<?>) AccessController.doPrivileged(new PrivilegedAction() {
                @Override
                public Object run() {
                    try {
                        final ClassLoader cl = new URLClassLoader(urls, classLoader);
                        return cl.loadClass(pt.getName());
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                }
            });
            if (c != TemplateBase.class && !TemplateBase.class.isAssignableFrom(c)) {
                throw new RythmCompileException("Loaded class does not extends TemplateBase");
            }
            final Class<? extends TemplateBase> ct = (Class<? extends TemplateBase>) c;
            return new CompiledTemplate(pt, ct);
        } catch (MalformedURLException e) {
            throw new RythmCompileException(e);
        }
    }


}
