package ru.mai;

import lombok.extern.slf4j.Slf4j;
import ru.mai.primality_test.PrimalityTest;
import ru.mai.primality_test.test.MillerRabinTest;

import java.math.BigInteger;

@Slf4j
public class Main {
    public static void main(String[] args) {
        PrimalityTest test = new MillerRabinTest();
        System.out.println(test.isProbablyPrime(
                new BigInteger("113"),
                0.5
        ));
    }
}