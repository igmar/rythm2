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
package org.rythmengine.internal.hash.sha1;


import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * This class mainly exists to prevent wheeling in a whole third-party library
 */
public final class SHA1 {
    private static final char[] DIGITS_LC = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String sha1Hex(String in) {

        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(in.getBytes(Charset.defaultCharset()));
            return byteToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String byteToHex(byte[] in) {
        final int len = in.length;

        if ((len & 0x01) != 0) {
            return null;
        }

        final char[] out = new char[len << 1];

        for (int i = 0, j = 0; i < len; i++) {
            out[j++] = DIGITS_LC[(0xf0 & in[i]) >>> 4];
            out[j++] = DIGITS_LC[0x0f & in[i]];
        }

        return new String(out);
    }
}
