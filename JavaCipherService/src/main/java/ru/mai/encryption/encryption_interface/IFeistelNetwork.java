package ru.mai.encryption.encryption_interface;

public interface IFeistelNetwork {
    byte[] apply(byte[] bytes, byte[] roundKey, int numRounds);
}
