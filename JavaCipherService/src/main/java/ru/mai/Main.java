package ru.mai;

import lombok.extern.slf4j.Slf4j;
import ru.mai.RSA.RSA;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

import static ru.mai.RSA.RSA.PrimeTest.MILLER_RABIN;

@Slf4j
public class Main {
    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            RSA rsa = new RSA(MILLER_RABIN, 0.999, new Random().nextInt(200) + 50);
            BigInteger[][] keys = rsa.getKey();
            BigInteger e = keys[0][0];
            BigInteger d = keys[1][0];
            BigInteger N = keys[1][1];
            byte[] text = new BigInteger(10000, new Random()).toByteArray();
            byte[] encrypt = rsa.encryptParallel(text, e, N);
            byte[] decrypt = rsa.decryptParallel(encrypt, d, N);
            System.out.println(Arrays.equals(text, decrypt));
        }
    }
}