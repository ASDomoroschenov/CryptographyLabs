package ru.mai.encryption.cipher_impl.padding;

import ru.mai.encryption.cipher_interface.IPadding;

public class PKCS7Padding implements IPadding {
    @Override
    public byte[] addPAdding(byte[] bytes, int textBlockSize) {
        byte valuePadding = (byte) (textBlockSize - bytes.length % textBlockSize);

        byte[] bytesWithPadding = new byte[bytes.length + valuePadding];

        System.arraycopy(bytes, 0, bytesWithPadding, 0, bytes.length);

        for (int i = 0; i < bytesWithPadding.length - bytes.length; i++) {
            bytesWithPadding[bytes.length + i] = valuePadding;
        }

        return bytesWithPadding;
    }

    @Override
    public byte[] removePadding(byte[] bytes) {
        byte valuePadding = bytes[bytes.length - 1];
        byte[] bytesWithoutPadding = new byte[bytes.length - valuePadding];

        System.arraycopy(bytes, 0, bytesWithoutPadding, 0, bytes.length - valuePadding);

        return bytesWithoutPadding;
    }
}
