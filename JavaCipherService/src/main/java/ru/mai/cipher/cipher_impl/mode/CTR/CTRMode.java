package ru.mai.cipher.cipher_impl.mode.CTR;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mai.utils.utils_impl.CollectText;
import ru.mai.utils.utils_impl.ThreadCipher;
import ru.mai.cipher.cipher_interface.ICipher;
import ru.mai.cipher.cipher_interface.ICipherMode;

@Slf4j
@AllArgsConstructor
public class CTRMode implements ICipherMode {
    private ICipher cipher;
    private byte[] initialVector;

    @Override
    public byte[] encrypt(byte[] text) throws IllegalArgumentException {
        if (text == null || text.length == 0) {
            throw new IllegalArgumentException("Illegal bytes text");
        }

        return new ThreadCipher(
                cipher.getTextBlockSize(),
                new ThreadTaskCipherCTR(cipher, text, initialVector),
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
                new ThreadTaskCipherCTR(cipher, text, initialVector),
                new CollectText()
        ).cipher(text);
    }
}
