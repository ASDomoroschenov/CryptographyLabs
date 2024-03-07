package ru.mai.encryption.encryption_impl.mode.ECB;

import lombok.AllArgsConstructor;
import ru.mai.encryption.encryption_impl.mode.utils.utils_impl.CollectTextMode;
import ru.mai.encryption.encryption_impl.mode.utils.utils_impl.ThreadCipher;
import ru.mai.encryption.encryption_interface.ICipherMode;
import ru.mai.encryption.encryption_interface.ISymmetricCipher;

import java.util.concurrent.ExecutionException;

@AllArgsConstructor
public class ECBMode implements ICipherMode {
    ISymmetricCipher cipher;

    @Override
    public byte[] encryptText(byte[] text) throws ExecutionException, InterruptedException {
        return new ThreadCipher(
                cipher.getTextBlockSize(),
                new ThreadTaskEncryptECB(cipher),
                new CollectTextMode()).cipher(text);
    }

    @Override
    public byte[] decryptText(byte[] text) throws ExecutionException, InterruptedException {
        return new ThreadCipher(
                cipher.getTextBlockSize(),
                new ThreadTaskDecryptECB(cipher),
                new CollectTextMode()).cipher(text);
    }
}
