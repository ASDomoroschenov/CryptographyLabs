package ru.mai.encryption.encryption_impl.padding;

import ru.mai.encryption.encryption_interface.Padding;

import java.util.Random;

public class ISO10126Padding implements Padding {
    @Override
    public byte[] addPAdding(byte[] bytes, int textBlockSize) {
        byte valuePadding = (byte) (textBlockSize - bytes.length % textBlockSize);

        byte[] bytesWithPadding = new byte[bytes.length + valuePadding];

        System.arraycopy(bytes, 0, bytesWithPadding, 0, bytes.length);

        for (int i = 0; i < bytesWithPadding.length - bytes.length - 1; i++) {
            bytesWithPadding[bytes.length + i] = (byte) (new Random().nextInt(Byte.MAX_VALUE - Byte.MIN_VALUE) + Byte.MIN_VALUE);
        }

        bytesWithPadding[bytesWithPadding.length - 1] = valuePadding;

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
