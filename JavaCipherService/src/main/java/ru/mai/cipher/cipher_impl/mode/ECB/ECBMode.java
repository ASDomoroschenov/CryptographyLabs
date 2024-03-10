package ru.mai.cipher.cipher_impl.mode.ECB;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mai.cipher.cipher_impl.mode.utils.utils_impl.CollectText;
import ru.mai.cipher.cipher_impl.mode.utils.utils_impl.ThreadCipher;
import ru.mai.cipher.cipher_interface.ICipher;
import ru.mai.cipher.cipher_interface.ICipherMode;

@Slf4j
@AllArgsConstructor
public class ECBMode implements ICipherMode {
    private ICipher cipher;

    @Override
    public byte[] encrypt(byte[] text) throws IllegalArgumentException {
        if (text == null || text.length == 0) {
            throw new IllegalArgumentException("Illegal bytes text");
        }

        return new ThreadCipher(
                cipher.getTextBlockSize(),
                new ThreadTaskEncryptECB(cipher),
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
                new ThreadTaskDecryptECB(cipher),
                new CollectText()
        ).cipher(text);
    }
}
