package ru.mai.cipher.cipher_service;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.testng.internal.junit.ArrayAsserts.assertArrayEquals;

public class DEALTest {
    private static final byte[] key = {1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8};
    private static final byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8};

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
                {"/home/alexandr/CryptographyLabs/JavaCipherService/src/main/resources/ru/mai/test/input/text.txt"}
        };
    }

    @Test(dataProvider = "testDataText")
    public void testTextECB(byte[] testDataText) {
        CipherService cipherService = new CipherService(
                key,
                CipherService.CipherAlgorithm.DEAL,
                CipherService.EncryptionMode.ECB,
                CipherService.StuffingMode.PKCS7,
                iv
        );

        byte[] encryptedBytes = cipherService.encrypt(testDataText);
        byte[] decryptedBytes = cipherService.decrypt(encryptedBytes);

        assertArrayEquals(testDataText, decryptedBytes);
    }

    @Test(dataProvider = "testDataFile")
    public void testFileECB(String testDataFile) throws IOException {
        CipherService cipherService = new CipherService(
                key,
                CipherService.CipherAlgorithm.DEAL,
                CipherService.EncryptionMode.ECB,
                CipherService.StuffingMode.PKCS7,
                iv
        );

        String pathToEncryptedFile = cipherService.encrypt(testDataFile);
        String pathToDecryptedFile = cipherService.decrypt(pathToEncryptedFile);

        byte[] originalBytes = Files.readAllBytes(Paths.get(testDataFile));
        byte[] decryptedBytes = Files.readAllBytes(Paths.get(pathToDecryptedFile));

        assertArrayEquals(originalBytes, decryptedBytes);
    }

    @Test(dataProvider = "testDataText")
    public void testTextCBC(byte[] testDataText) {
        CipherService cipherService = new CipherService(
                key,
                CipherService.CipherAlgorithm.DEAL,
                CipherService.EncryptionMode.CBC,
                CipherService.StuffingMode.PKCS7,
                iv
        );

        byte[] encryptedBytes = cipherService.encrypt(testDataText);
        byte[] decryptedBytes = cipherService.decrypt(encryptedBytes);

        assertArrayEquals(testDataText, decryptedBytes);
    }

    @Test(dataProvider = "testDataFile")
    public void testFileCBC(String testDataFile) throws IOException {
        CipherService cipherService = new CipherService(
                key,
                CipherService.CipherAlgorithm.DEAL,
                CipherService.EncryptionMode.CBC,
                CipherService.StuffingMode.PKCS7,
                iv
        );

        String pathToEncryptedFile = cipherService.encrypt(testDataFile);
        String pathToDecryptedFile = cipherService.decrypt(pathToEncryptedFile);

        byte[] originalBytes = Files.readAllBytes(Paths.get(testDataFile));
        byte[] decryptedBytes = Files.readAllBytes(Paths.get(pathToDecryptedFile));

        assertArrayEquals(originalBytes, decryptedBytes);
    }

    @Test(dataProvider = "testDataText")
    public void testTextPCBC(byte[] testDataText) {
        CipherService cipherService = new CipherService(
                key,
                CipherService.CipherAlgorithm.DEAL,
                CipherService.EncryptionMode.PCBC,
                CipherService.StuffingMode.PKCS7,
                iv
        );

        byte[] encryptedBytes = cipherService.encrypt(testDataText);
        byte[] decryptedBytes = cipherService.decrypt(encryptedBytes);

        assertArrayEquals(testDataText, decryptedBytes);
    }

    @Test(dataProvider = "testDataFile")
    public void testFilePCBC(String testDataFile) throws IOException {
        CipherService cipherService = new CipherService(
                key,
                CipherService.CipherAlgorithm.DEAL,
                CipherService.EncryptionMode.PCBC,
                CipherService.StuffingMode.PKCS7,
                iv
        );

        String pathToEncryptedFile = cipherService.encrypt(testDataFile);
        String pathToDecryptedFile = cipherService.decrypt(pathToEncryptedFile);

        byte[] originalBytes = Files.readAllBytes(Paths.get(testDataFile));
        byte[] decryptedBytes = Files.readAllBytes(Paths.get(pathToDecryptedFile));

        assertArrayEquals(originalBytes, decryptedBytes);
    }

    @Test(dataProvider = "testDataText")
    public void testTextCFB(byte[] testDataText) {
        CipherService cipherService = new CipherService(
                key,
                CipherService.CipherAlgorithm.DEAL,
                CipherService.EncryptionMode.CFB,
                CipherService.StuffingMode.PKCS7,
                iv
        );

        byte[] encryptedBytes = cipherService.encrypt(testDataText);
        byte[] decryptedBytes = cipherService.decrypt(encryptedBytes);

        assertArrayEquals(testDataText, decryptedBytes);
    }

    @Test(dataProvider = "testDataFile")
    public void testFileCFB(String testDataFile) throws IOException {
        CipherService cipherService = new CipherService(
                key,
                CipherService.CipherAlgorithm.DEAL,
                CipherService.EncryptionMode.CFB,
                CipherService.StuffingMode.PKCS7,
                iv
        );

        String pathToEncryptedFile = cipherService.encrypt(testDataFile);
        String pathToDecryptedFile = cipherService.decrypt(pathToEncryptedFile);

        byte[] originalBytes = Files.readAllBytes(Paths.get(testDataFile));
        byte[] decryptedBytes = Files.readAllBytes(Paths.get(pathToDecryptedFile));

        assertArrayEquals(originalBytes, decryptedBytes);
    }

    @Test(dataProvider = "testDataText")
    public void testTextOFB(byte[] testDataText) {
        CipherService cipherService = new CipherService(
                key,
                CipherService.CipherAlgorithm.DEAL,
                CipherService.EncryptionMode.OFB,
                CipherService.StuffingMode.PKCS7,
                iv
        );

        byte[] encryptedBytes = cipherService.encrypt(testDataText);
        byte[] decryptedBytes = cipherService.decrypt(encryptedBytes);

        assertArrayEquals(testDataText, decryptedBytes);
    }

    @Test(dataProvider = "testDataFile")
    public void testFileOFB(String testDataFile) throws IOException {
        CipherService cipherService = new CipherService(
                key,
                CipherService.CipherAlgorithm.DEAL,
                CipherService.EncryptionMode.OFB,
                CipherService.StuffingMode.PKCS7,
                iv
        );

        String pathToEncryptedFile = cipherService.encrypt(testDataFile);
        String pathToDecryptedFile = cipherService.decrypt(pathToEncryptedFile);

        byte[] originalBytes = Files.readAllBytes(Paths.get(testDataFile));
        byte[] decryptedBytes = Files.readAllBytes(Paths.get(pathToDecryptedFile));

        assertArrayEquals(originalBytes, decryptedBytes);
    }

    @Test(dataProvider = "testDataText")
    public void testTextCTR(byte[] testDataText) {
        CipherService cipherService = new CipherService(
                key,
                CipherService.CipherAlgorithm.DEAL,
                CipherService.EncryptionMode.CTR,
                CipherService.StuffingMode.PKCS7,
                iv
        );

        byte[] encryptedBytes = cipherService.encrypt(testDataText);
        byte[] decryptedBytes = cipherService.decrypt(encryptedBytes);

        assertArrayEquals(testDataText, decryptedBytes);
    }

    @Test(dataProvider = "testDataFile")
    public void testFileCTR(String testDataFile) throws IOException {
        CipherService cipherService = new CipherService(
                key,
                CipherService.CipherAlgorithm.DEAL,
                CipherService.EncryptionMode.CTR,
                CipherService.StuffingMode.PKCS7,
                iv
        );

        String pathToEncryptedFile = cipherService.encrypt(testDataFile);
        String pathToDecryptedFile = cipherService.decrypt(pathToEncryptedFile);

        byte[] originalBytes = Files.readAllBytes(Paths.get(testDataFile));
        byte[] decryptedBytes = Files.readAllBytes(Paths.get(pathToDecryptedFile));

        assertArrayEquals(originalBytes, decryptedBytes);
    }

    @Test(dataProvider = "testDataText")
    public void testTextRD(byte[] testDataText) {
        CipherService cipherService = new CipherService(
                key,
                CipherService.CipherAlgorithm.DEAL,
                CipherService.EncryptionMode.RD,
                CipherService.StuffingMode.PKCS7,
                iv
        );

        byte[] encryptedBytes = cipherService.encrypt(testDataText);
        byte[] decryptedBytes = cipherService.decrypt(encryptedBytes);
        assertArrayEquals(testDataText, decryptedBytes);
    }

    @Test(dataProvider = "testDataFile")
    public void testFileRDDES(String testDataFile) throws IOException {
        CipherService cipherService = new CipherService(
                key,
                CipherService.CipherAlgorithm.DEAL,
                CipherService.EncryptionMode.RD,
                CipherService.StuffingMode.PKCS7,
                iv
        );

        String pathToEncryptedFile = cipherService.encrypt(testDataFile);
        String pathToDecryptedFile = cipherService.decrypt(pathToEncryptedFile);

        byte[] originalBytes = Files.readAllBytes(Paths.get(testDataFile));
        byte[] decryptedBytes = Files.readAllBytes(Paths.get(pathToDecryptedFile));

        assertArrayEquals(originalBytes, decryptedBytes);
    }
}