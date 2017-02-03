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
import org.rythmengine.internal.exceptions.RythmTemplateException;
import org.rythmengine.internal.hash.sha1.SHA1;
import org.rythmengine.internal.parser.ParsedTemplate;
import org.rythmengine.template.TemplateBase;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public final class CompiledTemplate {
    private String path;
    private String source;
    private String hash;
    private Class<? extends TemplateBase> c;

    CompiledTemplate(final ParsedTemplate pt, Class<? extends TemplateBase> c) {
        this.path = pt.path();
        this.source = pt.getGeneratedSource();
        this.hash = SHA1.sha1Hex(this.source);
        this.c = c;
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

    public String execute(final IHttpContext context, final Map<String, Object> args) {
        try {
            TemplateBase t = c.getDeclaredConstructor(IHttpContext.class, Map.class).newInstance(context, args);
            return t.execute();
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new RythmTemplateException(String.format("Can't instantiate class : %s", e));
        }
    }
}
