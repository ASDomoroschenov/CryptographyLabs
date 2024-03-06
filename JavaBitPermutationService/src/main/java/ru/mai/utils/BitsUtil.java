package ru.mai.utils;

public class BitsUtil {
    public static byte[] permutation(byte[] arrayBits, int[] permutationValues) throws IllegalArgumentException {
        byte[] permutationResult = new byte[(permutationValues.length + Byte.SIZE - 1) / Byte.SIZE];

        for (int i = 0; i < permutationValues.length; i++) {
            permutationResult[i / Byte.SIZE] |= (byte) (getBit(arrayBits, permutationValues[i]) << (Byte.SIZE - (i % Byte.SIZE) - 1));
        }

        return permutationResult;
    }

    public static byte getBit(byte[] arrayBits, int indexBit) {
        if (indexBit > arrayBits.length * Byte.SIZE || indexBit <= 0) {
            throw new IllegalArgumentException("Index out of arrayBits bounds");
        }

        int indexBlock = (indexBit - 1) / Byte.SIZE;
        int indexBitInBlock = indexBit - indexBlock * Byte.SIZE;

        return (byte) ((arrayBits[indexBlock] >> (Byte.SIZE - indexBitInBlock)) & 1);
    }

    public static void outputBits(byte[] arrayBits) {
        for (int i = 0; i < arrayBits.length * Byte.SIZE; i++) {
            System.out.print(getBit(arrayBits, i + 1));
        }
        System.out.println();
    }

    public static byte[] xor(byte[] first, byte[] second) {
        return null;
    }
}
