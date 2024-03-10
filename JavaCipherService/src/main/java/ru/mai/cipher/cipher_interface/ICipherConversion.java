package ru.mai.cipher.cipher_interface;

public interface ICipherConversion {
    byte[] apply(byte[] bytes, byte[] roundKey) throws IllegalArgumentException;
}
