package ru.mai.cipher.cipher_interface;

public interface ICipherMode {
    byte[] encrypt(byte[] text) throws IllegalArgumentException;

    byte[] decrypt(byte[] text) throws IllegalArgumentException;
}
