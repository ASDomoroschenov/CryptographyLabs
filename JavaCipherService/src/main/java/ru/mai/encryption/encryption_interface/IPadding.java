package ru.mai.encryption.encryption_interface;

public interface IPadding {
    byte[] addPAdding(byte[] bytes, int numBytes);

    byte[] removePadding(byte[] bytes);
}
