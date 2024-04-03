package ru.mai;

import lombok.extern.slf4j.Slf4j;
import ru.mai.RSA.RSA;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;

import static ru.mai.RSA.RSA.PrimeTest.MILLER_RABIN;

@Slf4j
public class Main {
    public static void main(String[] args) throws IOException {
        RSA rsa = new RSA(MILLER_RABIN, 0.999, new Random().nextInt(200) + 50);
        BigInteger[][] keys = rsa.getKey();
        BigInteger e = keys[0][0];
        BigInteger d = keys[1][0];
        BigInteger N = keys[1][1];

        try {
            String pathToInputFile = "/home/alexandr/CryptographyLabs/JavaCipherService/src/main/resources/file.txt";
            String encryptFile = rsa.encryptFile(pathToInputFile, e, N);
            String decryptFile = rsa.decryptFile(encryptFile, d, N);

            System.out.println(Arrays.equals(
                    Files.readAllBytes(Path.of(pathToInputFile)),
                    Files.readAllBytes(Path.of(decryptFile)))
            );
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }
    }
}