package ru.mai.primality_test.test;

import ru.mai.primality_test.AbstractPrimalityTest;
import ru.mai.service.ModuloService;
import ru.mai.utils.BigIntegerRandomGenerator;

import java.math.BigInteger;

public class FermatTest extends AbstractPrimalityTest {
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
