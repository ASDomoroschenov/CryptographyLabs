package ru.mai.cipher.cipher_interface;

public interface IPadding {
    byte[] addPAdding(byte[] bytes, int numBytes);

    byte[] removePadding(byte[] bytes);
}
