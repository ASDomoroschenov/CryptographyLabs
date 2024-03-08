package ru.mai.encryption.cipher_interface;

public interface IFeistelNetwork {
    byte[] apply(byte[] bytes, byte[] roundKey, int numRounds);
}
