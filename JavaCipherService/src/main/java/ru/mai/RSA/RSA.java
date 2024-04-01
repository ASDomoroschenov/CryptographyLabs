package ru.mai.RSA;

import ru.mai.primality_test.PrimalityTest;
import ru.mai.primality_test.test.FermatTest;
import ru.mai.primality_test.test.MillerRabinTest;
import ru.mai.primality_test.test.SoloveStrassenTest;
import ru.mai.service.ModuloService;
import ru.mai.utils.BigIntegerRandomGenerator;

import java.math.BigInteger;

public class RSA {
    public enum PrimeTest {
        FERMAT,
        MILLER_RABIN,
        SOLOVE_STRASSEN
    }

    public static class GenerateKeyRSA {
        private final PrimalityTest test;
        private final double minProbability;
        private final int bitLength;
        private final BigIntegerRandomGenerator random;

        public GenerateKeyRSA(PrimeTest primeTest, double minProbability, int bitLength) {
            this.test = getPrimalityTest(primeTest);
            this.minProbability = minProbability;
            this.bitLength = bitLength;
            this.random = new BigIntegerRandomGenerator();
        }

        private PrimalityTest getPrimalityTest(PrimeTest primeTest) {
            switch (primeTest) {
                case FERMAT -> {
                    return new FermatTest();
                }
                case MILLER_RABIN -> {
                    return new MillerRabinTest();
                }
                case SOLOVE_STRASSEN -> {
                    return new SoloveStrassenTest();
                }
            }

            return null;
        }

        public BigInteger[][] generateKey() {
            BigInteger p = random.generatePrime(bitLength, test, minProbability);
            BigInteger q = random.generatePrime(bitLength, test, minProbability);
            BigInteger N = p.multiply(q);
            BigInteger eulerFunction = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
            BigInteger e = random.generateRelativelyPrime(eulerFunction);
            BigInteger d = ModuloService.gcdExtended(e, eulerFunction)[1];

            return new BigInteger[][]{
                    {e, N},
                    {d, N}
            };
        }
    }
}
