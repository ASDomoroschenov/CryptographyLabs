package ru.mai.encryption.encryption_interface;

public interface SymmetricCipher {
    byte[] encrypt(byte[] bytes);

    byte[] decipher(byte[] bytes);
}
