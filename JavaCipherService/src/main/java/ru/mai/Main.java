package ru.mai;

import lombok.extern.slf4j.Slf4j;
import ru.mai.encryption.encryption_service.EncryptionService;
import ru.mai.encryption.encryption_service.EncryptionService.CipherAlgorithm;
import ru.mai.encryption.encryption_service.EncryptionService.StuffingMode;
import ru.mai.encryption.encryption_service.EncryptionService.EncryptionMode;

import java.util.Arrays;

@Slf4j
public class Main {
    public static void main(String[] args) {
        byte[] key = {1, 2, 3, 4, 5, 6, 7};
        byte[] text = {1, 1, 1, 1, 5, 6, 7};

        EncryptionService service = new EncryptionService(
                key,
                CipherAlgorithm.DES,
                EncryptionMode.ECB,
                StuffingMode.ZEROS);

        byte[] encryptText = service.encrypt(text);

        System.out.println(Arrays.toString(encryptText));
        System.out.println(Arrays.toString(service.decipher(encryptText)));
    }
}

//1 2 3 4 5 6 7 8
//  2 3 4 5 6 7 8