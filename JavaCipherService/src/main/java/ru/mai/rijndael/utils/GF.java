package ru.mai.rijndael.utils;

import java.util.ArrayList;
import java.util.List;

public class GF {
    private static final String ILLEGAL_MODULO_EXCEPTION = "Illegal modulo";

    private GF() {}

    public static byte sum(byte firstPolynomial, byte secondPolynomial) {
        return (byte) (firstPolynomial ^ secondPolynomial);
    }

    private static char multiplication(byte firstPolynomial, byte secondPolynomial) {
        char resMultiplication = 0;

        for (int i = 0; i < Byte.SIZE; i++) {
            if (((secondPolynomial >> i) & 1) == 1) {
                resMultiplication ^= resetFirstBitsChar((char) (firstPolynomial << i), Byte.SIZE - i);
            }
        }

        return resMultiplication;
    }

    public static byte mod(char polynomial, char modulo) {
        byte sizeModulo = getSize(modulo);
        int leftShiftValue = 16;

        while (leftShiftValue != 0) {
            if (((polynomial >> leftShiftValue) & 1) == 1) {
                polynomial ^= (char) (modulo << (leftShiftValue - sizeModulo + 1));
            }

            leftShiftValue--;
        }

        return (byte) polynomial;
    }

    public static byte multiplicationModulo(byte firstPolynomial, byte secondPolynomial, char modulo) {
        if ((modulo >> (Byte.SIZE + 1)) != 0) {
            throw new IllegalArgumentException(ILLEGAL_MODULO_EXCEPTION);
        }

        return mod(multiplication(firstPolynomial, secondPolynomial), modulo);
    }

    public static byte invert(byte polynomial, char modulo) {
        int power = (1 << Byte.SIZE) - 2;
        byte result = 1;

        while (power != 0) {
            if ((power & 1) == 1) {
                result = multiplicationModulo(result, polynomial, modulo);
            }

            polynomial = multiplicationModulo(polynomial, polynomial, modulo);
            power >>= 1;
        }

        return result;
    }

    public static boolean isIrreducible(char polynomial) {
        if ((polynomial >> (Byte.SIZE + 1)) != 0) {
            throw new IllegalArgumentException(ILLEGAL_MODULO_EXCEPTION);
        }

        for (char i = 2; i < (1 << (Byte.SIZE / 2 + 1)); i++) {
            if (mod(polynomial, i) == 0) {
                return false;
            }
        }

        return (polynomial >> (Byte.SIZE + 1)) == 0;
    }

    public static List<Character> getIrreducible() {
        List<Character> result = new ArrayList<>();

        for (char i = 1 << Byte.SIZE; i < 1 << (Byte.SIZE + 1); i++) {
            if (isIrreducible(i)) {
                result.add(i);
            }
        }

        return result;
    }

    public static char resetFirstBitsChar(char charItem, int countBits) {
        for (int i = 0; i < countBits; i++) {
            charItem &= (char) ~(1 << (Character.SIZE - i - 1));
        }

        return charItem;
    }

    public static byte getSize(char polynomial) {
        byte size = Character.SIZE;

        while (((polynomial >> (size - 1)) & 1) == 0) {
            size--;
        }

        return size;
    }
}