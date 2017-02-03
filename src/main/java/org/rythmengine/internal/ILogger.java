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
package org.rythmengine.internal;

public interface ILogger {
    boolean isTraceEnabled();

    void trace(String format, Object... args);

    void trace(Throwable t, String format, Object... args);

    boolean isDebugEnabled();

    void debug(String format, Object... args);

    void debug(Throwable t, String format, Object... args);

    boolean isInfoEnabled();

    void info(String format, Object... args);

    void info(Throwable t, String format, Object... args);

    boolean isWarnEnabled();

    void warn(String format, Object... args);

    void warn(Throwable t, String format, Object... args);

    boolean isErrorEnabled();

    void error(String format, Object... args);

    void error(Throwable t, String format, Object... args);
}
