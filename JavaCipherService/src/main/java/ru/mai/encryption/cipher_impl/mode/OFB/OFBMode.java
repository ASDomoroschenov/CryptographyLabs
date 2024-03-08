package ru.mai.encryption.cipher_impl.mode.OFB;

import lombok.AllArgsConstructor;
import ru.mai.encryption.cipher_impl.mode.utils.utils_impl.CollectTextMode;
import ru.mai.encryption.cipher_impl.mode.utils.utils_impl.ThreadCipher;
import ru.mai.encryption.cipher_interface.ICipher;
import ru.mai.encryption.cipher_interface.ICipherMode;

import java.util.concurrent.ExecutionException;

@AllArgsConstructor
public class OFBMode implements ICipherMode {
    private ICipher cipher;
    private byte[] initialVector;

    @Override
    public byte[] encrypt(byte[] text) throws ExecutionException, InterruptedException {
        return new ThreadCipher(
                cipher.getTextBlockSize(),
                new ThreadTaskOFB(cipher, text, initialVector),
                new CollectTextMode()).cipher(text);
    }

    @Override
    public byte[] decrypt(byte[] text) throws ExecutionException, InterruptedException {
        return new ThreadCipher(
                cipher.getTextBlockSize(),
                new ThreadTaskOFB(cipher, text, initialVector),
                new CollectTextMode()).cipher(text);
    }
}
