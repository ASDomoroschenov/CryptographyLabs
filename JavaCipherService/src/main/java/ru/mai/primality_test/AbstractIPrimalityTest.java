package ru.mai.primality_test;

import ru.mai.utils.utils_impl.BigIntegerRandomGenerator;

import java.math.BigInteger;

public abstract class AbstractIPrimalityTest implements IPrimalityTest {
    protected abstract boolean singleIteration(BigInteger number, BigIntegerRandomGenerator random);

    @Override
    public boolean isProbablyPrime(BigInteger number, double minProbability) {
        if (number.compareTo(BigInteger.ONE) <= 0 || minProbability < 0.5 || minProbability >= 1.0) {
            throw new IllegalArgumentException("Illegal arguments prime file");
        }

        if (number.equals(BigInteger.TWO)) {
            return true;
        }

        if (number.compareTo(BigInteger.TWO) < 0 || number.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            return false;
        }

        BigIntegerRandomGenerator random = new BigIntegerRandomGenerator();
        int countIterations = (int) Math.ceil(Math.log(1.0 / (1.0 - minProbability)) / Math.log(2));

        for (int i = 0; i < countIterations; i++) {
            if (!singleIteration(number, random)) {
                return false;
            }
        }

        return true;
    }
}
