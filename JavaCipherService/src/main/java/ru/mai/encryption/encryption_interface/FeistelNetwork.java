package ru.mai.encryption.encryption_interface;

public interface FeistelNetwork {
    byte[] apply(byte[] bytes, byte[] roundKey, int numRounds);
}
