package ru.mai.primality_test.test;

import ru.mai.primality_test.AbstractIPrimalityTest;
import ru.mai.service.ModuloService;
import ru.mai.utils.utils_impl.BigIntegerRandomGenerator;

import java.math.BigInteger;

public class SoloveStrassenTestI extends AbstractIPrimalityTest {
    @Override
    protected boolean singleIteration(BigInteger number, BigIntegerRandomGenerator random) {
        BigInteger randomBigInteger = random.generateInBounds(BigInteger.TWO, number.subtract(BigInteger.ONE));
        int jacobi = ModuloService.jacobi(randomBigInteger, number);

        return (jacobi != 0) &&
               (ModuloService.fastPowMod(
                                 randomBigInteger,
                                 number.subtract(BigInteger.ONE).divide(BigInteger.TWO),
                                 number
                             )
                             .equals(BigInteger.valueOf(jacobi).add(number).mod(number)));
    }
}
