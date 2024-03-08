package ru.mai.encryption.encryption_impl.DES.key_generate;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import ru.mai.encryption.encryption_interface.IRoundKeyGenerator;

@AllArgsConstructor
public class DESDecryptKeyGenerator implements IRoundKeyGenerator {
    private final DESKeyGenerator keyGenerator;

    @Override
    public byte[][] generate(byte[] key) {
        byte[][] keys = keyGenerator.getRoundKeys().clone();
        ArrayUtils.reverse(keys);
        return keys;
    }
}
