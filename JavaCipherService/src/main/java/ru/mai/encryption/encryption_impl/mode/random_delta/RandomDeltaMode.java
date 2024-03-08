package ru.mai.encryption.encryption_impl.mode.random_delta;

import lombok.AllArgsConstructor;
import ru.mai.encryption.encryption_interface.ICipherMode;
import ru.mai.encryption.encryption_interface.ICipher;

@AllArgsConstructor
public class RandomDeltaMode implements ICipherMode {
    ICipher cipher;

    @Override
    public byte[] encrypt(byte[] text) {
        return new byte[0];
    }

    @Override
    public byte[] decrypt(byte[] text) {
        return new byte[0];
    }
}
