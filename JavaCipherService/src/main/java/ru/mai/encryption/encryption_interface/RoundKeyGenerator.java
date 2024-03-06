package ru.mai.encryption.encryption_interface;

public interface RoundKeyGenerator {
    byte[][] generate(byte[] key);
}