package ru.mai.encryption.encryption_impl.padding;

import ru.mai.encryption.encryption_interface.Padding;

public class PKCS7Padding implements Padding {
    @Override
    public byte[] addPAdding(byte[] bytes, int numBytes) {
        return new byte[0];
    }
}
