/*
 * Copyright (c) 2016-2017, Igmar Palsenberg. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rythmengine.conf;

import org.apache.commons.lang3.StringUtils;
import org.rythmengine.internal.IResourceLoader;
import org.rythmengine.internal.exceptions.RythmConfigException;
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
    private File templatedir;
    private Provider<IResourceLoader> resourceLoaderProvider;
    private RythmGenerator sourceGenerator;
    private RythmCompiler compiler;
    private String compiledPackage;

    private RythmConfiguration(Builder builder) {
        this.id = builder.id;
        this.classLoader = builder.classLoader;
        this.writeEnabled = builder.writeEnabled;
        this.engineMode = builder.engineMode;
        this.homedir = builder.homedir;
        this.tempdir = builder.tempdir;
        this.templatedir = builder.templatedir;
        this.resourceLoaderProvider = builder.resourceLoaderProvider;
        this.sourceGenerator = builder.sourceGenerator;
        this.compiler = builder.compiler;
        this.compiledPackage = builder.compiledPackage;
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

    public File getTemplatedir() {
        return templatedir;
    }

    public Provider<IResourceLoader> getResourceLoaderProvider() {
        return resourceLoaderProvider;
    }

    public RythmGenerator getSourceGenerator() {
        return sourceGenerator;
    }

    public RythmCompiler getCompiler() {
        return compiler;
    }

    public String getCompiledPackage() {
        return compiledPackage;
    }

    public static class Builder {
        private String id;
        private ClassLoader classLoader;
        private Boolean writeEnabled;
        private RythmEngineMode engineMode;
        private File homedir;
        private File tempdir;
        private File templatedir;

        private Provider<IResourceLoader> resourceLoaderProvider;
        private RythmGenerator sourceGenerator;
        private RythmCompiler compiler;
        private String compiledPackage;

        public Builder() {
            // FIXME : Add random prefix
            this.id = "re-";
            this.classLoader = this.getClass().getClassLoader();
            this.writeEnabled = true;
            this.engineMode = RythmEngineMode.DEV;
            this.homedir = new File("/tmp");
            this.tempdir = new File(System.getProperty("java.io.tmpdir"), "__rythm");
            this.templatedir = new File("__rythm_compiled");
            this.resourceLoaderProvider = new DefaultResourceLoaderProvider();
            this.sourceGenerator = RythmGenerator.JDK7;
            this.compiler = RythmCompiler.JDT;
            this.compiledPackage = "rythmengine.compiled";
        }

        public Builder id(final String id) {
            this.id = id;
            return this;
        }

        public Builder classLoader(final ClassLoader classLoader) {
            this.classLoader = classLoader;
            return this;
        }

        public Builder writeEnabled(final Boolean writeEnabled) {
            this.writeEnabled = writeEnabled;
            return this;
        }

        public Builder engineMode(final RythmEngineMode engineMode) {
            this.engineMode = engineMode;
            return this;
        }

        public Builder homeDir(final String homeDir) {
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

        public Builder tempDir(final String tempDir) {
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

        public Builder resourceLoaderProvider(final Provider<IResourceLoader> provider) {
            if (provider == null) {
                throw new RythmConfigException("resourceloader is null");
            }
            this.resourceLoaderProvider = provider;
            return this;
        }

        public Builder sourceGenerator(final RythmGenerator generator) {
            if (generator == null) {
                throw new RythmConfigException("source generator is null");
            }
            this.sourceGenerator = generator;
            return this;
        }

        public Builder withCompiler(final RythmCompiler compiler) {
            if (compiler == null) {
                throw new RythmConfigException("compiler is null");
            }
            this.compiler = compiler;
            return this;
        }

        public Builder withCompiledPackage(final String compiledPackage) {
            if (StringUtils.isEmpty(compiledPackage)) {
                throw new RythmConfigException("Package name cannot be empty");
            }
            this.compiledPackage = compiledPackage;

            return this;
        }

        public RythmConfiguration build() {
            return new RythmConfiguration(this);
        }
    }
}
