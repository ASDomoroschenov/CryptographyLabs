package ru.mai.encryption.encryption_impl.padding;

import ru.mai.encryption.encryption_interface.Padding;

public class ZerosPadding implements Padding {
    @Override
    public byte[] addPAdding(byte[] bytes, int numBytes) {
        return new byte[0];
    }
}
