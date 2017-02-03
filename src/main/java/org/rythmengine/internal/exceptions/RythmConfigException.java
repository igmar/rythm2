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
package org.rythmengine.internal.exceptions;

/*
 * Thrown when a RythmEngine can't be create due to an error in the supplied config
 */
public final class RythmConfigException extends RuntimeException {
    public RythmConfigException() {
        super();
    }

    public RythmConfigException(final String message) {
        super(message);
    }

    public RythmConfigException(final Throwable cause) {
        super(cause);
    }
}
