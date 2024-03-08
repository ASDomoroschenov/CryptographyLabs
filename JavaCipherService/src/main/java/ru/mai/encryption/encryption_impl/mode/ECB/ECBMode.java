package ru.mai.encryption.encryption_impl.mode.ECB;

import lombok.AllArgsConstructor;
import ru.mai.encryption.encryption_impl.mode.utils.utils_impl.CollectTextMode;
import ru.mai.encryption.encryption_impl.mode.utils.utils_impl.ThreadCipher;
import ru.mai.encryption.encryption_interface.ICipherMode;
import ru.mai.encryption.encryption_interface.ICipher;

import java.util.concurrent.ExecutionException;

@AllArgsConstructor
public class ECBMode implements ICipherMode {
    private ICipher cipher;

    @Override
    public byte[] encrypt(byte[] text) throws ExecutionException, InterruptedException {
        return new ThreadCipher(
                cipher.getTextBlockSize(),
                new ThreadTaskEncryptECB(cipher),
                new CollectTextMode()).cipher(text);
    }

    @Override
    public byte[] decrypt(byte[] text) throws ExecutionException, InterruptedException {
        return new ThreadCipher(
                cipher.getTextBlockSize(),
                new ThreadTaskDecryptECB(cipher),
                new CollectTextMode()).cipher(text);
    }
}
