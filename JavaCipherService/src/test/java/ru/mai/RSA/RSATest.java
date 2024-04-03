package ru.mai.RSA;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import static org.testng.internal.junit.ArrayAsserts.assertArrayEquals;
import static ru.mai.RSA.RSA.PrimeTest.MILLER_RABIN;

public class RSATest {
    @DataProvider(name = "testDataText")
    public Object[][] testDataText() {
        return new Object[][]{
                {
                        "Hello world!".getBytes()
                },
                {
                        "".getBytes()
                },
                {
                        """
                                Я лишился девственности в 20 лет. Не так, как это обычно бывает с парнями моего возраста. Все куда тяжелее - и никакой романтики.
                                """.getBytes()
                },
                {
                        """
                                Мой хомяк
                                """.getBytes()
                },
                {
                        null
                }
        };
    }

    @DataProvider(name = "testDataFile")
    public Object[][] testDataFile() {
        return new Object[][]{
                {"/home/alexandr/CryptographyLabs/JavaCipherService/src/test/java/ru/mai/RSA/resources/file1.txt"},
                {"/home/alexandr/CryptographyLabs/JavaCipherService/src/test/java/ru/mai/RSA/resources/file2.txt"}
        };
    }

    @Test(dataProvider = "testDataText")
    public void testCipherText(byte[] testDataText) {
        RSA rsa = new RSA(
                MILLER_RABIN,
                0.999,
                new Random().nextInt(200) + 50);

        BigInteger[][] keys = rsa.getKey();
        BigInteger e = keys[0][0];
        BigInteger d = keys[1][0];
        BigInteger N = keys[1][1];

        byte[] encryptedBytes = rsa.encrypt(testDataText, e, N);
        byte[] decryptedBytes = rsa.decrypt(encryptedBytes, d, N);

        assertArrayEquals(testDataText, decryptedBytes);
    }

    @Test(dataProvider = "testDataFile")
    public void testCipherFile(String testDataFile) throws IOException {
        RSA rsa = new RSA(
                MILLER_RABIN,
                0.999,
                new Random().nextInt(200) + 50);

        BigInteger[][] keys = rsa.getKey();
        BigInteger e = keys[0][0];
        BigInteger d = keys[1][0];
        BigInteger N = keys[1][1];

        String encryptedFile = rsa.encryptFile(testDataFile, e, N);
        String decryptedFile = rsa.decryptFile(encryptedFile, d, N);

        assertArrayEquals(
                Files.readAllBytes(Path.of(testDataFile)),
                Files.readAllBytes(Path.of(decryptedFile))
        );

        new File(encryptedFile).delete();
        new File(decryptedFile).delete();
    }
}