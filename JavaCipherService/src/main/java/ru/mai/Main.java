package ru.mai;

import lombok.extern.slf4j.Slf4j;
import ru.mai.cipher.cipher_service.CipherService;
import ru.mai.cipher.cipher_service.CipherService.CipherAlgorithm;
import ru.mai.cipher.cipher_service.CipherService.EncryptionMode;
import ru.mai.cipher.cipher_service.CipherService.StuffingMode;
import ru.mai.utils.utils_impl.BytesUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@Slf4j
public class Main {
    public static void main(String[] args) throws IOException {
        byte[] keyDEAL = {1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8};
        byte[] keyDES = {1, 2, 3, 4, 5, 6, 7, 8};

        CipherService service = new CipherService(
                keyDES,
                CipherAlgorithm.DES,
                EncryptionMode.CTR,
                StuffingMode.ANSI_X_923,
                keyDES);

        long begin = System.currentTimeMillis();
        String source = "/home/alexandr/CryptographyLabs/JavaCipherService/src/main/resources/ru/mai/test/input/text.txt";
        String encrypt = service.encrypt(source);
//        String decrypt = service.decrypt(encrypt);
//
//        log.info(String.valueOf(Arrays.equals(
//                Files.readAllBytes(Paths.get(source)),
//                Files.readAllBytes(Paths.get(decrypt))
//        )));
//
//        log.info((double) (System.currentTimeMillis() - begin) / 1000 + "s");
    }
}
