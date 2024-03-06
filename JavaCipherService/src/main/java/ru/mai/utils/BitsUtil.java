package ru.mai.utils;

public class BitsUtil {
    public static long cyclicLeftShift(long number, int bits, int k) {
        return ((number & ((1L << (bits - k)) - 1)) << k) | ((number & (((1L << k) - 1) << (bits - k))) >> (bits - k));
    }

    public static long getFirstNBits(long number, int bits) {
        return ((number & (((1L << bits) - 1) << (Long.SIZE - bits))) >>> (Long.SIZE - bits));
    }

    public static void printBits(long number) {
        byte[] output = new byte[Long.BYTES];

        for (int i = 0; i < Long.BYTES; i++) {
            output[Long.BYTES - i - 1] = (byte) (number & ((1 << Byte.SIZE) - 1));
            number >>= Byte.SIZE;
        }

        for (int i = 0; i < Long.BYTES; i++) {
            for (int j = 0; j < Byte.SIZE; j++) {
                System.out.print(((output[i] << j) >> (Byte.SIZE - 1)) & 1);
            }
            System.out.print(" ");
        }
        System.out.println();
    }
}