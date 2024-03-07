package ru.mai.encryption.encryption_impl.mode;

import ru.mai.encryption.encryption_interface.CipherMode;

public class OFBMode implements CipherMode {
    @Override
    public byte[] couple(byte[] textBlock, byte[] otherBlock) {
        return new byte[0];
    }
}
