package ru.mai.encryption.encryption_interface;

public interface CipherMode {
    byte[] couple(byte[] firstBlock, byte[] secondBlock);
}
