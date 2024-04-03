package ru.mai.RSA.attack;

import org.apache.commons.lang3.tuple.Pair;
import ru.mai.service.ModuloService;

import java.math.BigInteger;

public class FermatAttack {
    public Pair<BigInteger, BigInteger> attack(BigInteger e, BigInteger N) {
        BigInteger nSqrt = N.sqrt().add(BigInteger.ONE);
        BigInteger xSqrt = nSqrt.pow(2).subtract(N);

        while (!isSquare(xSqrt)) {
            nSqrt = nSqrt.add(BigInteger.ONE);
            xSqrt = nSqrt.pow(2).subtract(N);
        }

        BigInteger p = nSqrt.subtract(xSqrt.sqrt());
        BigInteger q = nSqrt.add(xSqrt.sqrt());
        BigInteger eulerFunction = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger d = ModuloService.gcdExtended(e, eulerFunction)[1];

        return Pair.of(d, eulerFunction);
    }

    private boolean isSquare(BigInteger number) {
        return number.sqrt().pow(2).equals(number);
    }
}
