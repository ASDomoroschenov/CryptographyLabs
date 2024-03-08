package ru.mai.cipher.cipher_impl.padding;

import ru.mai.cipher.cipher_interface.IPadding;

public class ZerosPadding implements IPadding {
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
