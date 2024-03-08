package ru.mai;

import lombok.extern.slf4j.Slf4j;
import ru.mai.encryption.encryption_service.EncryptionService;
import ru.mai.encryption.encryption_service.EncryptionService.CipherAlgorithm;
import ru.mai.encryption.encryption_service.EncryptionService.StuffingMode;
import ru.mai.encryption.encryption_service.EncryptionService.EncryptionMode;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

@Slf4j
public class Main {
    public static void main(String[] args) {
        byte[] key = {1, 2, 3, 4, 5, 6, 7};
        byte[] text1 = {1, 1, 1, 1, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1};
        byte[] text2 = {1, 1, 1, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1};

        EncryptionService service = new EncryptionService(
                key,
                CipherAlgorithm.DES,
                EncryptionMode.ECB,
                StuffingMode.ANSI_X_923,
                new byte[] {1, 2, 3, 4, 5, 6, 7, 8});

        try {
            byte[] encryptText1 = service.encrypt(text1);
            byte[] encryptText2 = service.encrypt(text2);
            byte[] decryptText1 = service.decipher(encryptText1);
            byte[] decryptText2 = service.decipher(encryptText2);

            System.out.println(Arrays.equals(text1, decryptText1));
            System.out.println(Arrays.equals(text2, decryptText2));
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
