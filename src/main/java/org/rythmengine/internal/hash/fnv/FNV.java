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
