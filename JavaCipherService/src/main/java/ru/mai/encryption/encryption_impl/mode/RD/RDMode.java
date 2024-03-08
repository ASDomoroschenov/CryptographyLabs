package ru.mai.encryption.encryption_impl.mode.RD;

import lombok.AllArgsConstructor;
import ru.mai.encryption.encryption_impl.mode.utils.utils_impl.CollectTextMode;
import ru.mai.encryption.encryption_impl.mode.utils.utils_impl.ThreadCipher;
import ru.mai.encryption.encryption_interface.ICipher;
import ru.mai.encryption.encryption_interface.ICipherMode;

import java.util.concurrent.ExecutionException;

@AllArgsConstructor
public class RDMode implements ICipherMode {
    ICipher cipher;
    byte[] initialVector;

    @Override
    public byte[] encrypt(byte[] text) throws ExecutionException, InterruptedException {
        return new ThreadCipher(
                cipher.getTextBlockSize(),
                new ThreadTaskEncryptRD(cipher, text, initialVector),
                new CollectTextMode()
        ).cipher(text);
    }

    @Override
    public byte[] decrypt(byte[] text) throws ExecutionException, InterruptedException {
        return new ThreadCipher(
                cipher.getTextBlockSize(),
                new ThreadTaskDecryptRD(cipher, text, initialVector),
                new CollectTextMode()
        ).cipher(text);
    }
}
