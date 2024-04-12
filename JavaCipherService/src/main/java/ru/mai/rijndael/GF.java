package ru.mai.rijndael;

public class GF {
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

    private static byte mod(char polynomial, byte modulo) {
        int leftShiftValue = 6;

        while (leftShiftValue >= 0) {
            if (((polynomial >> (Byte.SIZE + leftShiftValue)) & 1) == 1) {
                polynomial ^= (char) (modulo << leftShiftValue);
                polynomial &= (char) (~(1 << (Byte.SIZE + leftShiftValue)));
            }

            --leftShiftValue;
        }

        return (byte) polynomial;
    }

    public static byte multiplicationModulo(byte firstPolynomial, byte secondPolynomial, byte modulo) {
        return mod(multiplication(firstPolynomial, secondPolynomial), modulo);
    }

    public static byte invert(byte polynomial, byte modulo) {
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

    private static char resetFirstBitsChar(char charItem, int countBits) {
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

    public static byte newMod(char polynomial, char modulo) {
        byte sizeModulo = getSize(modulo);
        int leftShiftValue = 16;

        while (leftShiftValue != 0) {
            if (((polynomial >> leftShiftValue) & 1) == 1) {
                polynomial ^= (char) (modulo << (Math.abs(leftShiftValue - sizeModulo + 1)));
            }

            leftShiftValue--;
        }

        return (byte) polynomial;
    }

    public static boolean isIrreducible(char polynomial) {
        for (char i = 2; i < (1 << (Byte.SIZE / 2)); i++) {
            if (newMod(polynomial, i) == 0) {
                return false;
            }
        }

        return (polynomial >> (Byte.SIZE + 1)) == 0;
    }

    public static void printBits(char item) {
        for (int i = 0; i < Character.SIZE; i++) {
            System.out.print((item >> (Character.SIZE - i - 1)) & 1);
        }
        System.out.println();
    }

    public static void printBits(byte item) {
        for (int i = 0; i < Byte.SIZE; i++) {
            System.out.print((item >> (Byte.SIZE - i - 1)) & 1);
        }
        System.out.println();
    }
}


//111
//100
//