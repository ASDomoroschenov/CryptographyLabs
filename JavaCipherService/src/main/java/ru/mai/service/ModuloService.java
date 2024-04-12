package ru.mai.service;

import java.math.BigInteger;

public class ModuloService {
    public static int legendre(BigInteger number, BigInteger modulo) {
        if (modulo.compareTo(BigInteger.TWO) <= 0 || modulo.isProbablePrime(100)) {
            throw new IllegalArgumentException("p must be odd-numbered");
        }

        if (number.mod(modulo).equals(BigInteger.ZERO)) {
            return 0;
        }

        return jacobi(number, modulo);
    }

    public static int jacobi(BigInteger number, BigInteger modulo) {
        if (modulo.compareTo(BigInteger.ZERO) <= 0 || !modulo.testBit(0)) {
            throw new IllegalArgumentException("modulo must be an odd positive number");
        }

        if (!gcd(number, modulo).equals(BigInteger.ONE)) {
            return 0;
        }

        int jacobi = 1;

        if (number.compareTo(BigInteger.ZERO) < 0) {
            number = number.negate();

            if (modulo.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
                jacobi = -jacobi;
            }
        }

        while (number.compareTo(BigInteger.ZERO) > 0) {
            int counter = 0;

            while (!number.testBit(0)) {
                number = number.shiftRight(1);
                counter++;
            }

            if ((counter & 1) != 0 &&
                    (modulo.mod(BigInteger.valueOf(8)).equals(BigInteger.valueOf(3)) || modulo.mod(BigInteger.valueOf(8)).equals(BigInteger.valueOf(5)))) {
                jacobi = -jacobi;
            }

            if (number.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3)) && modulo.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
                jacobi = -jacobi;
            }

            BigInteger temp = number;
            number = modulo.mod(number);
            modulo = temp;
        }

        return jacobi;
    }

    public static BigInteger gcd(BigInteger number1, BigInteger number2) {
        if (number2.equals(BigInteger.ZERO)) {
            return number1;
        }

        return gcd(number2, number1.mod(number2));
    }

    public static BigInteger[] gcdExtended(BigInteger number, BigInteger modulo) {
        BigInteger moduloCopy = modulo;
        BigInteger x = new BigInteger("1");
        BigInteger xPrev = new BigInteger("0");
        BigInteger y = new BigInteger("0");
        BigInteger yPrev = new BigInteger("1");

        while (modulo.compareTo(BigInteger.ZERO) > 0) {
            BigInteger quotient = number.divide(modulo);
            BigInteger reminder = number.mod(modulo);

            number = modulo;
            modulo = reminder;

            BigInteger tempX = x.subtract(xPrev.multiply(quotient));

            x = xPrev;
            xPrev = tempX;

            BigInteger tempY = y.subtract(yPrev.multiply(quotient));

            y = yPrev;
            yPrev = tempY;
        }

        return new BigInteger[] {number, x.add(moduloCopy).mod(moduloCopy), y}; // {gcd, reverse, y}
    }

    public static BigInteger fastPowMod(BigInteger number, BigInteger power, BigInteger modulo) {
        BigInteger result = BigInteger.ONE;

        while (!power.equals(BigInteger.ZERO)) {
            if (power.testBit(0)) {
                result = number.multiply(result).mod(modulo);
            }

            number = number.multiply(number).mod(modulo);
            power = power.shiftRight(1);
        }

        return result;
    }
}
