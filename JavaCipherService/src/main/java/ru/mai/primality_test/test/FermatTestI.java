package ru.mai.primality_test.test;

import ru.mai.primality_test.AbstractIPrimalityTest;
import ru.mai.service.ModuloService;
import ru.mai.utils.utils_impl.BigIntegerRandomGenerator;

import java.math.BigInteger;

public class FermatTestI extends AbstractIPrimalityTest {
    @Override
    protected boolean singleIteration(BigInteger number, BigIntegerRandomGenerator random) {


        return ModuloService.fastPowMod(
                                random.generateInBounds(BigInteger.TWO, number.subtract(BigInteger.ONE)),
                                number.subtract(BigInteger.ONE),
                                number
                            )
                            .equals(BigInteger.ONE);
    }
}
