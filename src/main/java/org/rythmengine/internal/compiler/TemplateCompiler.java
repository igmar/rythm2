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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public abstract class TemplateCompiler implements Callable<Map<ParsedTemplate, CompiledTemplate>> {
    protected List<ParsedTemplate> sources;
    protected RythmConfiguration configuration;
    protected CompiledTemplateLoader templateLoader;

    public TemplateCompiler(final RythmConfiguration configuration, final List<ParsedTemplate> parsedTemplates, final ClassLoader classLoader) throws RythmCompileException {
        this.configuration = configuration;
        this.sources = parsedTemplates;
        this.templateLoader = new CompiledTemplateLoader(configuration, classLoader);
    }
}
