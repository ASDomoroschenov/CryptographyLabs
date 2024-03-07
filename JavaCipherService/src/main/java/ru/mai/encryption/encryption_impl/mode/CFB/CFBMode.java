package ru.mai.encryption.encryption_impl.mode.CFB;

import ru.mai.encryption.encryption_impl.mode.utils.utils_impl.CollectTextMode;
import ru.mai.encryption.encryption_impl.mode.utils.utils_impl.ThreadCipher;
import ru.mai.encryption.encryption_interface.ICipherMode;
import ru.mai.encryption.encryption_interface.ISymmetricCipher;
import ru.mai.utils.BytesUtil;

import java.util.concurrent.ExecutionException;

public class CFBMode implements ICipherMode {
    ISymmetricCipher cipher;
    byte[] initialVector;

    public CFBMode(ISymmetricCipher cipher, byte[] initialVector) {
        this.cipher = cipher;
        this.initialVector = initialVector;
    }

    @Override
    public byte[] encryptText(byte[] text) {
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
    public byte[] decryptText(byte[] text) throws ExecutionException, InterruptedException {
        return new ThreadCipher(
                cipher.getTextBlockSize(),
                new ThreadTaskDecryptCFB(cipher, initialVector),
                new CollectTextMode()
        ).cipher(text);
    }
}
