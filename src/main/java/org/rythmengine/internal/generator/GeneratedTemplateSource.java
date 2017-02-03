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
package org.rythmengine.internal.generator;


import org.rythmengine.internal.hash.sha1.SHA1;

public final class GeneratedTemplateSource {
    final private String source;
    final private String name;
    final private String hash;

    public GeneratedTemplateSource(final String name, final String source) {
        this.source = source;
        this.name = name;
        this.hash = SHA1.sha1Hex(source);
    }

    public String getSource() {
        return source;
    }

    public String getName() {
        return name;
    }

    public String hash() {
        return hash;
    }
}
