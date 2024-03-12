package ru.mai.cipher.cipher_interface;

public interface IFeistelNetwork {
    byte[] apply(byte[] bytes, byte[] key, int numRounds) throws IllegalArgumentException;
}
