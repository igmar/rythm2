package org.rythmengine.internal.generator;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Created by igmar on 22/07/16.
 */
public interface ISourceGenerator {
    String generateSource(ParseTree pt);
}
