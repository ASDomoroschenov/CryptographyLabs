package ru.mai.utils;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BigIntegerRandomGenerator {
    private final Random random;

    public BigIntegerRandomGenerator() {
        random = new Random();
    }

    public BigInteger generate(int numBits) {
        return new BigInteger(random.nextInt(numBits), random);
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

    public BigInteger generatePositive(int numBits) {
        BigInteger randomBigInteger = new BigInteger(numBits, random);
        byte[] bytesRandomBigIntegerTemp = randomBigInteger.toByteArray();
        bytesRandomBigIntegerTemp[0] = (byte) (bytesRandomBigIntegerTemp[0] & ((1 << (Byte.SIZE)) - 1));
        randomBigInteger = new BigInteger(bytesRandomBigIntegerTemp);
        return randomBigInteger;
    }

    public BigInteger generateNegative(int numBits) {
        BigInteger randomBigInteger = new BigInteger(numBits, random);
        byte[] bytesRandomBigIntegerTemp = randomBigInteger.toByteArray();
        bytesRandomBigIntegerTemp[0] = (byte) (bytesRandomBigIntegerTemp[0] | (1 << (Byte.SIZE - 1)));
        randomBigInteger = new BigInteger(bytesRandomBigIntegerTemp);
        return randomBigInteger;
    }
}