package ru.mai.cipher.cipher_impl.mode.CBC;

import lombok.AllArgsConstructor;
import ru.mai.cipher.cipher_impl.mode.utils.utils_impl.CollectTextMode;
import ru.mai.cipher.cipher_impl.mode.utils.utils_impl.ThreadCipher;
import ru.mai.cipher.cipher_impl.mode.utils.utils_interface.IThreadCipher;
import ru.mai.cipher.cipher_interface.ICipherMode;
import ru.mai.cipher.cipher_interface.ICipher;
import ru.mai.utils.BytesUtil;

import java.util.concurrent.ExecutionException;

@AllArgsConstructor
public class CBCMode implements ICipherMode {
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
            byte[] cipherBlockText = cipher.encrypt(BytesUtil.xor(textBlock, cipherBlock));
            System.arraycopy(cipherBlockText, 0, result, i, textBlockSize);
            cipherBlock = cipherBlockText;
        }

        return result;
    }

    @Override
    public byte[] decrypt(byte[] text) throws ExecutionException, InterruptedException {
        IThreadCipher threadCipher = new ThreadCipher(
                cipher.getTextBlockSize(),
                new ThreadTaskDecryptCBC(cipher, initialVector),
                new CollectTextMode());

        return threadCipher.cipher(text);
    }
}
