package ru.mai.primality_test;

import java.math.BigInteger;

public interface IPrimalityTest {
    boolean isProbablyPrime(BigInteger number, double minProbability);
}
