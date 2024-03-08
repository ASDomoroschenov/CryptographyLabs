package ru.mai.encryption.encryption_impl.DES.key_generate;

import lombok.AllArgsConstructor;
import ru.mai.encryption.encryption_interface.IRoundKeyGenerator;

@AllArgsConstructor
public class DESEncryptKeyGenerator implements IRoundKeyGenerator {
    private final DESKeyGenerator keyGenerator;

    @Override
    public byte[][] generate(byte[] key) {
        return keyGenerator.getRoundKeys();
    }
}
