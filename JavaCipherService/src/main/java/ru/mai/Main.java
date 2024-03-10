package ru.mai;

import lombok.extern.slf4j.Slf4j;
import ru.mai.cipher.cipher_service.CipherService;
import ru.mai.cipher.cipher_service.CipherService.CipherAlgorithm;
import ru.mai.cipher.cipher_service.CipherService.EncryptionMode;
import ru.mai.cipher.cipher_service.CipherService.StuffingMode;

import java.util.Arrays;

@Slf4j
public class Main {
    public static void main(String[] args) {
        byte[] key = {1, 2, 3, 4, 5, 6, 7};
        byte[] text = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        CipherService service = new CipherService(
                key,
                CipherAlgorithm.DES,
                EncryptionMode.ECB,
                StuffingMode.ANSI_X_923,
                new byte[]{1, 2, 3, 4, 5, 6, 7, 8});

        byte[] encrypt = service.encrypt(text);
        byte[] decrypt = service.decrypt(encrypt);

        System.out.println(Arrays.equals(decrypt, text));
    }
}
