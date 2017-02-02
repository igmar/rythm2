package org.rythmengine.internal.generator;

import org.rythmengine.internal.generator.java7.Java7SourceGenerator;

import javax.inject.Provider;

public class DefaultSourceGeneratorProvider implements Provider<ISourceGenerator> {
    @Override
    public ISourceGenerator get() {
        return new Java7SourceGenerator();
    }
}
