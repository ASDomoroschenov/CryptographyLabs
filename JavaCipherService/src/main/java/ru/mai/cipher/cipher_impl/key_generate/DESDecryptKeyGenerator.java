package ru.mai.cipher.cipher_impl.key_generate;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import ru.mai.cipher.cipher_interface.IRoundKeyGenerator;

@AllArgsConstructor
public class DESDecryptKeyGenerator implements IRoundKeyGenerator {
    private final DESKeyGenerator keyGenerator;

    @Override
    public byte[][] generate(byte[] key) throws IllegalArgumentException {
        if (key == null || key.length == 0) {
            throw new IllegalArgumentException("Illegal bytes key");
        }

        byte[][] keys = keyGenerator.getRoundKeys().clone();
        ArrayUtils.reverse(keys);
        return keys;
    }
}
