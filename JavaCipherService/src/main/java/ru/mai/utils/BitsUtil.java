package ru.mai.utils;

public class BitsUtil {
    public static long cyclicLeftShift(long number, int bits, int k) {
        return ((number & ((1L << (bits - k)) - 1)) << k) | ((number & (((1L << k) - 1) << (bits - k))) >> (bits - k));
    }

    public static long getFirstNBits(long number, int bits) {
        return ((number & (((1L << bits) - 1) << (Long.SIZE - bits))) >>> (Long.SIZE - bits));
    }
}