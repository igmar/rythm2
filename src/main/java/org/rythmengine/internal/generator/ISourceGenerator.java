package org.rythmengine.internal.generator;

import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.rythmengine.internal.exceptions.RythmGenerateException;

public interface ISourceGenerator {
    GeneratedTemplateSource generateSource(String source, String path, ParseTree pt, TokenStream tokenStream) throws RythmGenerateException;
}
