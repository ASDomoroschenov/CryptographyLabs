package ru.mai.RSA.stream_mode.OFB;

import lombok.AllArgsConstructor;
import ru.mai.RSA.RSA;
import ru.mai.utils.utils_impl.thread_cipher.text.text_impl.TextThreadCipher;

@AllArgsConstructor
public class OFBMode {
    private RSA rsa;
    private byte[] initialVector;

    public byte[] encrypt(byte[] text) {

        return null;
    }
}
