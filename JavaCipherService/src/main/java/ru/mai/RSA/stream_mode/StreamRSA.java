package ru.mai.RSA.stream_mode;

import ru.mai.RSA.RSA;
import ru.mai.RSA.stream_mode.CTR.CTRMode;
import ru.mai.RSA.stream_mode.OFB.OFBMode;

public class StreamRSA {
    public enum STREAM {
        OFB,
        CTR
    }

    private RSA rsa;
    private IStreamCipher streamRSA;

    public StreamRSA(RSA rsa, STREAM stream) {
        this.rsa = rsa;

        switch (stream) {
            case OFB -> this.streamRSA = new OFBMode(rsa);
            case CTR -> this.streamRSA = new CTRMode(rsa);
        }
    }

    public byte[] encrypt(byte[] text) {
        return streamRSA.encrypt(text);
    }

    public byte[] decrypt(byte[] text) {
        return streamRSA.decrypt(text);
    }
}
