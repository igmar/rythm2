package org.rythmengine.internal.resourceloader;

import org.rythmengine.internal.IResourceLoader;

import javax.inject.Provider;

/**
 * Created by igmar on 22/07/16.
 */
public class DefaultResourceLoaderProvider implements Provider<IResourceLoader> {
    @Override
    public IResourceLoader get() {
        return new DefaultResourceLoader();
    }
}
