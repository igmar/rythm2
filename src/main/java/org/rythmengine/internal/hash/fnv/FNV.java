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
package org.rythmengine.internal.hash.fnv;

import java.math.BigInteger;

public final class FNV {
    private static final BigInteger INIT64 = new BigInteger("cbf29ce484222325", 16);
    private static final BigInteger PRIME64 = new BigInteger("100000001b3", 16);
    private static final BigInteger MOD64 = new BigInteger("2").pow(64);

    public static String fnv1_64(byte[] data) {
        BigInteger hash = INIT64;

        for (byte b : data) {
            hash = hash.multiply(PRIME64).mod(MOD64);
            hash = hash.xor(BigInteger.valueOf((int) b & 0xff));
        }

        return hash.toString();
    }

    public static String fnv1a_64(byte[] data) {
        BigInteger hash = INIT64;

        for (byte b : data) {
            hash = hash.xor(BigInteger.valueOf((int) b & 0xff));
            hash = hash.multiply(PRIME64).mod(MOD64);
        }

        return hash.toString();
    }
}
