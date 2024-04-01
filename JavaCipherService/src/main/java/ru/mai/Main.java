package ru.mai;

import lombok.extern.slf4j.Slf4j;
import ru.mai.RSA.RSA;

import java.math.BigInteger;

@Slf4j
public class Main {
    public static void main(String[] args) {
        RSA.GenerateKeyRSA generateKeyRSA = new RSA.GenerateKeyRSA(
                RSA.PrimeTest.MILLER_RABIN,
                0.5,
                100);

        BigInteger[][] keys = generateKeyRSA.generateKey();
        BigInteger N = keys[0][1];
        BigInteger e = keys[0][0];
        BigInteger d = keys[1][0];

        BigInteger number = new BigInteger("123");
        BigInteger cipherNumber = number.modPow(e, N);
        BigInteger decipherNumber = cipherNumber.modPow(d, N);

        System.out.println("number \t\t\t= " + number);
        System.out.println("cipherNumber \t= " + cipherNumber);
        System.out.println("decipherNumber \t= " + decipherNumber);
    }
}