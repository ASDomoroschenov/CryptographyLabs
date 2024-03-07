package ru.mai.encryption.encryption_impl.mode.random_delta;

import lombok.AllArgsConstructor;
import ru.mai.encryption.encryption_interface.ICipherMode;
import ru.mai.encryption.encryption_interface.ISymmetricCipher;

@AllArgsConstructor
public class RandomDeltaMode implements ICipherMode {
    ISymmetricCipher cipher;

    @Override
    public byte[] encryptText(byte[] text) {
        return new byte[0];
    }

    @Override
    public byte[] decryptText(byte[] text) {
        return new byte[0];
    }
}
