package ru.mai;

import lombok.extern.slf4j.Slf4j;
import ru.mai.cipher.cipher_impl.DES.DES;
import ru.mai.cipher.cipher_impl.cipher_conversion.CipherConversion;
import ru.mai.cipher.cipher_impl.feistel_network.FeistelNetwork;
import ru.mai.cipher.cipher_impl.mode.ECB.ECBMode;
import ru.mai.cipher.cipher_interface.ICipher;
import ru.mai.cipher.cipher_interface.ICipherConversion;
import ru.mai.cipher.cipher_interface.IFeistelNetwork;
import ru.mai.cipher.cipher_service.CipherService;
import ru.mai.cipher.cipher_service.CipherService.CipherAlgorithm;
import ru.mai.cipher.cipher_service.CipherService.EncryptionMode;
import ru.mai.cipher.cipher_service.CipherService.StuffingMode;
import ru.mai.utils.BytesUtil;

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

        String source = "/home/alexandr/CryptographyLabs/JavaCipherService/src/main/resources/ru/mai/tests/input/image.jpg";
        String encrypt = service.encrypt(source);
        String decrypt = service.decrypt(encrypt);

        byte[] sourceBytes = Files.readAllBytes(Paths.get(source));
        byte[] decryptBytes = Files.readAllBytes(Paths.get(decrypt));

        System.out.println(Arrays.equals(sourceBytes, decryptBytes));
    }
}
