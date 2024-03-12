package ru.mai.cipher.cipher_impl.mode.CFB;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mai.utils.utils_impl.CollectText;
import ru.mai.utils.utils_impl.ThreadCipher;
import ru.mai.cipher.cipher_interface.ICipher;
import ru.mai.cipher.cipher_interface.ICipherMode;
import ru.mai.utils.utils_impl.BytesUtil;

@Slf4j
@AllArgsConstructor
public class CFBMode implements ICipherMode {
    private ICipher cipher;
    private byte[] initialVector;

    @Override
    public byte[] encrypt(byte[] text) throws IllegalArgumentException {
        if (text == null || text.length == 0) {
            throw new IllegalArgumentException("Illegal bytes text");
        }

        int textBlockSize = cipher.getTextBlockSize();
        byte[] cipherBlock = initialVector;
        byte[] result = new byte[text.length];
        byte[] textBlock = new byte[textBlockSize];

        for (int i = 0; i < text.length; i += textBlockSize) {
            System.arraycopy(text, i, textBlock, 0, textBlockSize);
            byte[] cipherBlockText = BytesUtil.xor(cipher.encrypt(cipherBlock), textBlock);
            System.arraycopy(cipherBlockText, 0, result, i, textBlockSize);
            cipherBlock = cipherBlockText;
        }

        return result;
    }

    @Override
    public byte[] decrypt(byte[] text) throws IllegalArgumentException {
        if (text == null || text.length == 0) {
            throw new IllegalArgumentException("Illegal bytes text");
        }

        return new ThreadCipher(
                cipher.getTextBlockSize(),
                new ThreadTaskDecryptCFB(cipher, initialVector),
                new CollectText()
        ).cipher(text);
    }
}
