package ru.mai.cipher.cipher_impl.mode.RD;

import lombok.AllArgsConstructor;
import ru.mai.utils.utils_impl.CollectText;
import ru.mai.utils.utils_impl.ThreadCipher;
import ru.mai.cipher.cipher_interface.ICipher;
import ru.mai.cipher.cipher_interface.ICipherMode;

import java.util.Arrays;

@AllArgsConstructor
public class RDMode implements ICipherMode {
    ICipher cipher;
    byte[] initialVector;

    @Override
    public byte[] encrypt(byte[] text) throws IllegalArgumentException {
        if (text == null || text.length == 0) {
            throw new IllegalArgumentException("Illegal bytes text");
        }

        return new ThreadCipher(
                cipher.getTextBlockSize(),
                new ThreadTaskEncryptRD(cipher, text, initialVector),
                new CollectText()
        ).cipher(text);
    }

    @Override
    public byte[] decrypt(byte[] text) throws IllegalArgumentException {
        if (text == null || text.length == 0) {
            throw new IllegalArgumentException("Illegal bytes text");
        }

        return new ThreadCipher(
                cipher.getTextBlockSize(),
                new ThreadTaskDecryptRD(cipher, text, initialVector),
                new CollectText()
        ).cipher(text);
    }
}
