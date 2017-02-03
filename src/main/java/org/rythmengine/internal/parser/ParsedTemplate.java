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
package org.rythmengine.internal.parser;

import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.rythmengine.internal.exceptions.RythmGenerateException;
import org.rythmengine.internal.exceptions.RythmParserException;
import org.rythmengine.internal.exceptions.RythmTemplateException;
import org.rythmengine.internal.generator.GeneratedTemplateSource;
import org.rythmengine.internal.generator.ISourceGenerator;

public final class ParsedTemplate {
    private String path;
    private ParseTree pt;
    private TokenStream tokenStream;
    private ISourceGenerator sourceGenerator;
    private String source;
    private GeneratedTemplateSource generatedSource;

    public ParsedTemplate(final String path, final ISourceGenerator sourceGenerator, final ParseTree pt, TokenStream tokenstream, final String source) throws RythmGenerateException {
        assert path != null;
        assert sourceGenerator != null;
        assert pt != null;
        assert source != null;
        assert tokenstream != null;

        this.path = path;
        this.pt = pt;
        this.tokenStream = tokenstream;
        this.sourceGenerator = sourceGenerator;
        this.source = source;
        this.generatedSource = generateTemplateSource();
    }

    public String path() {
        return path;
    }

    public String source() {
        return source;
    }

    public String hash() {
        return generatedSource.hash();
    }

    public String getGeneratedSource() {
        if (generatedSource == null) {
            throw new RythmTemplateException("No sources generated");
        }
        return generatedSource.getSource();
    }

    public String getName() {
        if (generatedSource == null) {
            throw new RythmTemplateException("No sources generated");
        }
        return generatedSource.getName();
    }

    private GeneratedTemplateSource generateTemplateSource() throws RythmGenerateException {
        this.generatedSource = sourceGenerator.generateSource(source, path, pt, tokenStream);
        if (this.generatedSource == null) {
            throw new RythmParserException("Failed to generate sources");
        }
        return this.generatedSource;
    }
}
