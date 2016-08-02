package org.rythmengine.conf;

import org.apache.commons.lang3.StringUtils;
import org.rythmengine.internal.ILogger;
import org.rythmengine.internal.IResourceLoader;
import org.rythmengine.internal.exceptions.RythmConfigException;
import org.rythmengine.internal.generator.DefaultSourceGeneratorProvider;
import org.rythmengine.internal.generator.ISourceGenerator;
import org.rythmengine.internal.logger.JDKLogger;
import org.rythmengine.internal.logger.Logger;
import org.rythmengine.internal.resourceloader.DefaultResourceLoaderProvider;

import javax.inject.Provider;

import java.io.File;

public final class RythmConfiguration {
    private String id;
    private ClassLoader classLoader;
    private Boolean writeEnabled;
    private RythmEngineMode engineMode;
    private File homedir;
    private File tempdir;
    private Provider<IResourceLoader> resourceLoaderProvider;
    private Provider<ISourceGenerator> sourceGeneratorProvider;

    private RythmConfiguration(Builder builder) {
        this.id = builder.id;
        this.classLoader = builder.classLoader;
        this.writeEnabled = builder.writeEnabled;
        this.engineMode = builder.engineMode;
        this.homedir = builder.homedir;
        this.tempdir = builder.tempdir;
        this.resourceLoaderProvider = builder.resourceLoaderProvider;
        this.sourceGeneratorProvider = builder.sourceGeneratorProvider;
    }

    public String getId() {
        return id;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public Boolean getWriteEnabled() {
        return writeEnabled;
    }

    public RythmEngineMode getEngineMode() {
        return engineMode;
    }

    public File getHomedir() {
        return homedir;
    }

    public File getTempdir() {
        return tempdir;
    }

    public Provider<IResourceLoader> getResourceLoaderProvider() {
        return resourceLoaderProvider;
    }

    public Provider<ISourceGenerator> getSourceGeneratorProvider() {
        return sourceGeneratorProvider;
    }

    public static class Builder {
        private String id;
        private ClassLoader classLoader;
        private Boolean writeEnabled;
        private RythmEngineMode engineMode;
        private File homedir;
        private File tempdir;

        private Provider<IResourceLoader> resourceLoaderProvider;
        private Provider<ISourceGenerator> sourceGeneratorProvider;
        private Logger logger;

        public Builder() {
            // FIXME : Add random prefix
            this.id = "re-";
            this.classLoader = this.getClass().getClassLoader();
            this.writeEnabled = true;
            this.engineMode = RythmEngineMode.DEV;
            // FIXME : can throw a NPE
            //this.homedir = new File(Thread.currentThread().getContextClassLoader().getResource("rythm").getFile());
            this.homedir = new File("/tmp");
            this.tempdir = new File(System.getProperty("java.io.tmpdir"), "__rythm");
            this.resourceLoaderProvider = new DefaultResourceLoaderProvider();
            this.sourceGeneratorProvider = new DefaultSourceGeneratorProvider();
            Logger.register(JDKLogger.class);
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder classLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
            return this;
        }

        public Builder writeEnabled(Boolean writeEnabled) {
            this.writeEnabled = writeEnabled;
            return this;
        }

        public Builder engineMode(RythmEngineMode engineMode) {
            this.engineMode = engineMode;
            return this;
        }

        public Builder homeDir(String homeDir) {
            if (StringUtils.isEmpty(homeDir)) {
                throw new RythmConfigException("homedir is empty");
            }
            this.homedir = new File(homeDir);
            if (!this.homedir.exists()) {
                throw new RythmConfigException("homedir does not exist");
            }
            if ((!this.homedir.isDirectory())) {
                throw new RythmConfigException("homedir is not a directory");
            }

            return this;
        }

        public Builder tempDir(String tempDir) {
            if (StringUtils.isEmpty(tempDir)) {
                throw new RythmConfigException("tempdir is empty");
            }
            this.tempdir = new File(tempDir);
            if (!this.tempdir.exists()) {
                throw new RythmConfigException("tempdir does not exist");
            }
            if ((!this.tempdir.isDirectory())) {
                throw new RythmConfigException("tempdir is not a directory");
            }

            return this;
        }

        public Builder resourceLoaderProvider(Provider<IResourceLoader> provider) {
            if (provider == null) {
                throw new RythmConfigException("resourceloader is null");
            }
            this.resourceLoaderProvider = provider;
            return this;
        }

        public Builder withLogger(ILogger logger) {
            if (logger == null) {
                throw new RythmConfigException("logger is null");
            }
            Logger.register(logger);
            return this;
        }

        public Builder sourceGeneratorProvider(Provider<ISourceGenerator> provider) {
            if (provider == null) {
                throw new RythmConfigException("sourcegenerator provider is null");
            }
            this.sourceGeneratorProvider = provider;
            return this;
        }

        public RythmConfiguration build() {
            return new RythmConfiguration(this);
        }
    }
}
