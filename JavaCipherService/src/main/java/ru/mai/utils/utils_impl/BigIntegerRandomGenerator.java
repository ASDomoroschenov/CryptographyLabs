package ru.mai.utils.utils_impl;

import ru.mai.primality_test.IPrimalityTest;
import ru.mai.service.ModuloService;

import java.math.BigInteger;
import java.util.Random;

public class BigIntegerRandomGenerator {
    private final Random random;

    public BigIntegerRandomGenerator() {
        random = new Random();
    }

    public BigInteger generate(int bitLength) {
        return new BigInteger(random.nextInt(bitLength), random);
    }

    public BigInteger generateInBounds(BigInteger lowerBound, BigInteger upperBound) {
        int randomNumBits = random.nextInt(upperBound.bitLength() - lowerBound.bitLength() + 1) + lowerBound.bitLength();
        BigInteger randomBigInteger;

        do {
            randomBigInteger = new BigInteger(randomNumBits, random);
        } while (randomBigInteger.compareTo(lowerBound) < 0 ||
                randomBigInteger.compareTo(upperBound) > 0);

        return randomBigInteger;
    }

    public BigInteger generatePositive(int bitLength) {
        BigInteger randomBigInteger = new BigInteger(bitLength, random);
        byte[] bytesRandomBigIntegerTemp = randomBigInteger.toByteArray();
        bytesRandomBigIntegerTemp[0] = (byte) (bytesRandomBigIntegerTemp[0] & ((1 << (Byte.SIZE)) - 1));
        randomBigInteger = new BigInteger(bytesRandomBigIntegerTemp);
        return randomBigInteger;
    }

    public BigInteger generateNegative(int bitLength) {
        BigInteger randomBigInteger = new BigInteger(bitLength, random);
        byte[] bytesRandomBigIntegerTemp = randomBigInteger.toByteArray();
        bytesRandomBigIntegerTemp[0] = (byte) (bytesRandomBigIntegerTemp[0] | (1 << (Byte.SIZE - 1)));
        randomBigInteger = new BigInteger(bytesRandomBigIntegerTemp);
        return randomBigInteger;
    }

    public BigInteger generatePrime(int bitLength, IPrimalityTest test, double minProbability) {
        BigInteger randomBigInteger;

        do {
            randomBigInteger = generatePositive(bitLength);
        } while (!test.isProbablyPrime(randomBigInteger, minProbability));

        return randomBigInteger;
    }

    public BigInteger generateRelativelyPrime(BigInteger number) {
        BigInteger relativelyPrime;

        do {
            relativelyPrime = generateInBounds(BigInteger.TWO, number.subtract(BigInteger.ONE));
        } while (!ModuloService.gcd(number, relativelyPrime).equals(BigInteger.ONE));

        return relativelyPrime;
    }
}