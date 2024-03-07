package ru.mai.encryption.encryption_interface;

public interface Padding {
    byte[] addPAdding(byte[] bytes, int numBytes);
}
