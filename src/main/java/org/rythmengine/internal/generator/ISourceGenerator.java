package org.rythmengine.internal.generator;

import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.rythmengine.internal.exceptions.RythmGenerateException;

/**
 * Created by igmar on 22/07/16.
 */
public interface ISourceGenerator {
    String generateSource(String path, ParseTree pt, TokenStream tokenStream) throws RythmGenerateException;
}
