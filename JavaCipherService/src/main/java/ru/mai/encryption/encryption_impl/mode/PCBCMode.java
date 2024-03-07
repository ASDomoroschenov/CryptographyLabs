package ru.mai.encryption.encryption_impl.mode;

import ru.mai.encryption.encryption_interface.CipherMode;

public class PCBCMode implements CipherMode {
    @Override
    public byte[] couple(byte[] firstBlock, byte[] secondBlock) {
        return new byte[0];
    }
}
