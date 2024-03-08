package ru.mai.tests;

import lombok.AllArgsConstructor;
import ru.mai.cipher.cipher_service.CipherService;

@AllArgsConstructor
public class TestCipher {
    private CipherService service;

    public byte[] testText(byte[] text) {
        return null;
    }

    public String testFile(String pathToInputFile) {
        return null;
    }
}
