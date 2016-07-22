package org.rythmengine.internal.generator;

import org.rythmengine.internal.generator.java7.Java7SourceGenerator;

import javax.inject.Provider;

/**
 * Created by igmar on 22/07/16.
 */
public class DefaultSourceLoaderProvider implements Provider<ISourceGenerator> {
    @Override
    public ISourceGenerator get() {
        return new Java7SourceGenerator();
    }
}
