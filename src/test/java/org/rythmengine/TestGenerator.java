package org.rythmengine;

import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.rythmengine.internal.exceptions.RythmGenerateException;
import org.rythmengine.internal.generator.GeneratedTemplateSource;
import org.rythmengine.internal.generator.ISourceGenerator;

/**
 * Created by igmar on 26/09/16.
 */
public class TestGenerator implements ISourceGenerator {
    @Override
    public GeneratedTemplateSource generateSource(final String source, final String path, final ParseTree pt, final TokenStream tokenStream) throws RythmGenerateException {
        return null;
    }
}
