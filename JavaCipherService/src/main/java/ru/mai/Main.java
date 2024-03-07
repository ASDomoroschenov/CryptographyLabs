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
        byte[] text = {1, 1, 1, 1, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1, 1, 1, 1, 5, 6, 7, 8, 1};

        EncryptionService service = new EncryptionService(
                key,
                CipherAlgorithm.DES,
                EncryptionMode.CBC,
                StuffingMode.ANSI_X_923,
                new byte[] {1, 2, 3, 4, 5, 6, 7, 8});

        try {
            byte[] encryptText = service.encrypt(text);
            byte[] decryptText = service.decipher(encryptText);
            System.out.println(Arrays.equals(text, decryptText));
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
