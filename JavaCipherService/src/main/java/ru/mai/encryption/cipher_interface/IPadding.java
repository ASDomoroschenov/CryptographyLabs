package ru.mai.encryption.cipher_interface;

public interface IPadding {
    byte[] addPAdding(byte[] bytes, int numBytes);

    byte[] removePadding(byte[] bytes);
}
