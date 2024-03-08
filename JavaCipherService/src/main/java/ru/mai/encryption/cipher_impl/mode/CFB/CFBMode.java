package ru.mai.encryption.cipher_impl.mode.CFB;

import lombok.AllArgsConstructor;
import ru.mai.encryption.cipher_impl.mode.utils.utils_impl.CollectTextMode;
import ru.mai.encryption.cipher_impl.mode.utils.utils_impl.ThreadCipher;
import ru.mai.encryption.cipher_interface.ICipherMode;
import ru.mai.encryption.cipher_interface.ICipher;
import ru.mai.utils.BytesUtil;

import java.util.concurrent.ExecutionException;

@AllArgsConstructor
public class CFBMode implements ICipherMode {
    private ICipher cipher;
    private byte[] initialVector;

    @Override
    public byte[] encrypt(byte[] text) {
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
    public byte[] decrypt(byte[] text) throws ExecutionException, InterruptedException {
        return new ThreadCipher(
                cipher.getTextBlockSize(),
                new ThreadTaskDecryptCFB(cipher, initialVector),
                new CollectTextMode()
        ).cipher(text);
    }
}
