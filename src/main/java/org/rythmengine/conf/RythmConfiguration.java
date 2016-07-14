package org.rythmengine.conf;

import org.rythmengine.internal.ILogger;
import org.rythmengine.internal.IResourceLoader;
import javax.inject.Provider;

import java.io.File;

import static org.rythmengine.conf.RythmEngineMode.DEV;

public final class RythmConfiguration {
    private String id;
    private ClassLoader classLoader;
    private Boolean writeEnabled;
    private RythmEngineMode engineMode;
    private File homedir;
    private File tempdir;

    private RythmConfiguration(Builder builder) {
        this.id = builder.id;
        this.classLoader = builder.classLoader;
        this.writeEnabled = builder.writeEnabled;
        this.engineMode = builder.engineMode;
        this.homedir = builder.homedir;
        this.tempdir = builder.tempdir;
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

    public static class Builder {
        private String id;
        private ClassLoader classLoader;
        private Boolean writeEnabled;
        private RythmEngineMode engineMode;
        private File homedir;
        private File tempdir;

        private Provider<IResourceLoader> resourceLoaderProvider;
        private Provider<ILogger> loggerProvider;

        public Builder() {
            // FIXME : Add random prefix
            this.id = "re-";
            this.classLoader = this.getClass().getClassLoader();
            this.writeEnabled = true;
            this.engineMode = DEV;
            // FIXME : can throw a NPE
            this.homedir = new File(Thread.currentThread().getContextClassLoader().getResource("rythm").getFile());
            this.tempdir = new File(System.getProperty("java.io.tmpdir"), "__rythm");
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder classLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
            return this;
        }


        public RythmConfiguration build() {
            return new RythmConfiguration(this);
        }
    }
}
