package ru.mai.primality_test.test;

import ru.mai.primality_test.AbstractPrimalityTest;
import ru.mai.service.ModuloService;
import ru.mai.utils.BigIntegerRandomGenerator;

import java.math.BigInteger;

public class MillerRabinTest extends AbstractPrimalityTest {
    @Override
    protected boolean singleIteration(BigInteger number, BigIntegerRandomGenerator random) {
        BigInteger d = number.subtract(BigInteger.ONE);
        int step = 0;

        while (d.testBit(0)) {
            d = d.shiftRight(1);
            step++;
        }

        BigInteger a = random.generateInBounds(BigInteger.TWO, number.subtract(BigInteger.ONE));
        BigInteger x = ModuloService.fastPowMod(a, d, number);

        if (x.equals(BigInteger.ONE) || x.equals(number.subtract(BigInteger.ONE))) {
            return true;
        }

        for (int j = 0; j < step - 1 && x.equals(number.subtract(BigInteger.ONE)); j++) {
            x = ModuloService.fastPowMod(x, BigInteger.TWO, number);

            if (x.equals(BigInteger.ONE)) {
                return false;
            }
        }

        return x.equals(number.subtract(BigInteger.ONE));
    }
}
