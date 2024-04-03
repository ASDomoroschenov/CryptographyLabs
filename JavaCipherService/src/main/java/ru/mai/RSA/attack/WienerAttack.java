package ru.mai.RSA.attack;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import ru.mai.service.ModuloService;
import ru.mai.utils.utils_impl.BigIntegerRandomGenerator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class WienerAttack {
    public Triple<BigInteger, BigInteger, List<Pair<BigInteger, BigInteger>>> attack(BigInteger e, BigInteger N) {
        BigIntegerRandomGenerator random = new BigIntegerRandomGenerator();
        BigInteger test = random.generateInBounds(BigInteger.TWO, N.subtract(BigInteger.ONE));
        List<BigInteger> continuedFraction = getContinuedFraction(e, N);
        List<Pair<BigInteger, BigInteger>> suitableFractions = new ArrayList<>();
        BigInteger eulerFunction = null;
        BigInteger d = null;

        BigInteger denominator = BigInteger.ONE;
        BigInteger numerator = BigInteger.ZERO;
        BigInteger denominatorPrev = continuedFraction.get(0);
        BigInteger numeratorPrev = BigInteger.ONE;

        suitableFractions.add(Pair.of(numerator, denominator));
        suitableFractions.add(Pair.of(numeratorPrev, denominatorPrev));

        for (int i = 1; i < continuedFraction.size(); i++) {
            BigInteger continuedFractionItem = continuedFraction.get(i);
            BigInteger denominatorNext = continuedFractionItem.multiply(denominator).add(denominatorPrev);
            BigInteger numeratorNext = continuedFractionItem.multiply(numerator).add(numeratorPrev);

            suitableFractions.add(Pair.of(numeratorNext, denominatorNext));

            denominatorPrev = denominator;
            numeratorPrev = numerator;
            denominator = denominatorNext;
            numerator = numeratorNext;

            if (ModuloService.fastPowMod(test, e.multiply(denominator), N).equals(test)) {
                d = denominator;
                eulerFunction = e.multiply(d).subtract(BigInteger.ONE).divide(numerator);
                return Triple.of(d, eulerFunction, suitableFractions);
            }
        }

        return Triple.of(d, eulerFunction, suitableFractions);
    }

    public List<BigInteger> getContinuedFraction(BigInteger numerator, BigInteger denominator) {
        List<BigInteger> result = new ArrayList<>();

        while (!denominator.equals(BigInteger.ZERO)) {
            result.add(numerator.divide(denominator));

            numerator = numerator.mod(denominator);

            BigInteger temp = numerator;
            numerator = denominator;
            denominator = temp;
        }

        return result;
    }
}
