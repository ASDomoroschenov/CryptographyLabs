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
        byte[] key = {1, 2, 3, 4, 5, 6, 7};

        CipherService service = new CipherService(
                key,
                CipherAlgorithm.DES,
                EncryptionMode.ECB,
                StuffingMode.ANSI_X_923,
                new byte[]{1, 2, 3, 4, 5, 6, 7, 8});

        long begin = System.currentTimeMillis();
        String source = "/home/alexandr/CryptographyLabs/JavaCipherService/src/main/resources/ru/mai/test/input/video.mp4";
        String encrypt = service.encrypt(source);
        System.out.println(System.currentTimeMillis() - begin);
    }
}
