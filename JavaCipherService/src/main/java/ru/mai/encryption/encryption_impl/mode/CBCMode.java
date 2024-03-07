package ru.mai.encryption.encryption_impl.mode;

import ru.mai.encryption.encryption_interface.CipherMode;
import ru.mai.utils.BytesUtil;

public class CBCMode implements CipherMode {
    @Override
    public byte[] couple(byte[] textBlock, byte[] otherBlock) {
        return BytesUtil.xor(textBlock, otherBlock);
    }
}
