package ru.mai.primality_test;

import java.math.BigInteger;

public interface PrimalityTest {
    boolean isProbablyPrime(BigInteger number, double minProbability);
}
