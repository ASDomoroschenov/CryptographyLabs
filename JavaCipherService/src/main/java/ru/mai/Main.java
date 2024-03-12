package ru.mai;

import lombok.extern.slf4j.Slf4j;
import ru.mai.cipher.cipher_service.CipherService;
import ru.mai.cipher.cipher_service.CipherService.CipherAlgorithm;
import ru.mai.cipher.cipher_service.CipherService.EncryptionMode;
import ru.mai.cipher.cipher_service.CipherService.StuffingMode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@Slf4j
public class Main {
    public static void main(String[] args) throws IOException {
        byte[] keyDEAL = {1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8};

        CipherService service = new CipherService(
                keyDEAL,
                CipherAlgorithm.DEAL,
                EncryptionMode.ECB,
                StuffingMode.ANSI_X_923,
                keyDEAL);

        long begin = System.currentTimeMillis();
        String source = "/home/alexandr/CryptographyLabs/JavaCipherService/src/main/resources/ru/mai/test/input/text.txt";
        String encrypt = service.encrypt(source);
        String decrypt = service.decrypt(encrypt);

        System.out.println(Arrays.equals(
                Files.readAllBytes(Paths.get(source)),
                Files.readAllBytes(Paths.get(decrypt))
        ));

        System.out.println(System.currentTimeMillis() - begin);
    }
}
