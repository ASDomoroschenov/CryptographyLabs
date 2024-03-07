package ru.mai.encryption.encryption_impl.mode;

import ru.mai.encryption.encryption_interface.CipherMode;

public class CBCMode implements CipherMode {
    @Override
    public byte[] couple(byte[] firstBlock, byte[] secondBlock) {
        return new byte[0];
    }
}
