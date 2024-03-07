package ru.mai.encryption.encryption_impl.padding;

import ru.mai.encryption.encryption_interface.Padding;

public class ZerosPadding implements Padding {
    @Override
    public byte[] addPAdding(byte[] bytes, int textBlockSize) {
        byte[] bytesWithPadding = new byte[((bytes.length + textBlockSize - 1) / textBlockSize) * textBlockSize];

        System.arraycopy(bytes, 0, bytesWithPadding, 0, bytes.length);

        return bytesWithPadding;
    }

    @Override
    public byte[] removePadding(byte[] bytes) {
        return bytes;
    }
}
