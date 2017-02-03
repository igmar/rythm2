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
package org.rythmengine.template;

import org.rythmengine.exceptions.RythmTemplateRuntimeException;
import org.rythmengine.internal.IHttpContext;

import java.util.Map;

public abstract class TemplateBase {
    protected IHttpContext httpContext;
    protected Map<String, Object> args;

    protected TemplateBase(IHttpContext httpContext, Map<String, Object> args) {
        if (httpContext == null) {
            throw new RythmTemplateRuntimeException("httpContext cannot be null");
        }
        if (args == null) {
            throw new RythmTemplateRuntimeException("args cannot be null");
        }
        this.httpContext = httpContext;
        this.args = args;
    }

    public abstract String execute();
}
