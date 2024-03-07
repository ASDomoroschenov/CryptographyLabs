package ru.mai.encryption.encryption_interface;

public interface SymmetricCipher {
    byte[] encrypt(byte[] bytes);

    byte[] decrypt(byte[] bytes);

    int getTextBlockSize();
}
