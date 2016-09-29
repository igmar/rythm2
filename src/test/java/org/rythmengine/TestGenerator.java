package org.rythmengine;

import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.rythmengine.internal.exceptions.RythmGenerateException;
import org.rythmengine.internal.generator.ISourceGenerator;

/**
 * Created by igmar on 26/09/16.
 */
public class TestGenerator implements ISourceGenerator {
    @Override
    public String generateSource(String path, ParseTree pt, TokenStream tokenStream) throws RythmGenerateException {
        return "";
    }
}
