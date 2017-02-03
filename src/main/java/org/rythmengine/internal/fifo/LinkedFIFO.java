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
package org.rythmengine.internal.fifo;

import java.util.LinkedList;
import java.util.List;

public class LinkedFIFO<T> implements FIFO<T> {
    private List<T> backed;

    public LinkedFIFO() {
        backed = new LinkedList<>();
    }

    @Override
    public void push(T t) {
        backed.add(t);
    }

    @Override
    public T pop() {
        if (backed.size() == 0) {
            return null;
        }
        final T t = this.backed.get(0);
        backed.remove(0);
        return t;
    }

    @Override
    public T pop(int idx) {
        if (backed.size() < idx) {
            return null;
        }
        T t = backed.get(idx);
        backed.remove(idx);
        return t;
    }

    @Override
    public T peek() {
        return backed.size() == 0 ? null : this.backed.get(0);
    }

    @Override
    public T peek(int idx) {
        if (backed.size() < idx) {
            return null;
        }
        return backed.get(idx);
    }

    @Override
    public int size() {
        return backed.size();
    }

    @Override
    public void clear() {
        backed.clear();
    }
}
