package org.rythmengine.internal.hash.sha1;


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
            md.update(in.getBytes());
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
