package ru.mai.encryption.encryption_service;

public class EncryptionService {
    public enum EncryptionMode {
        ECB,
        CBC,
        PCBC,
        CFB,
        OFB,
        CTR,
        RANDOM_DELTA
    }

    public enum StuffingMode {
        ZEROS,
        ANSI_X_923,
        PKCS7,
        ISO_10126
    }

    public enum CipherAlgorithm {
        DES
    }

    private byte[] key;
    private CipherAlgorithm cipherAlgorithm;
    private EncryptionMode encryptionMode;
    private StuffingMode stuffingMode;
    private byte[] initialVector;

    public EncryptionService(byte[] key, CipherAlgorithm cipherAlgorithm, EncryptionMode encryptionMode, StuffingMode stuffingMode, Object... additionalArgs) {
        this.key = key;
        this.cipherAlgorithm = cipherAlgorithm;
        this.encryptionMode = encryptionMode;
        this.stuffingMode = stuffingMode;
        initialVector = null;
    }

    public EncryptionService(byte[] key, CipherAlgorithm cipherAlgorithm, EncryptionMode encryptionMode, StuffingMode stuffingMode, byte[] initialVector, Object... additionalArgs) {
        this.key = key;
        this.cipherAlgorithm = cipherAlgorithm;
        this.encryptionMode = encryptionMode;
        this.stuffingMode = stuffingMode;
        this.initialVector = initialVector;
    }

    public byte[] encrypt(byte[] text) {
        return null;
    }

    public String encrypt(String inputFilePath) {
        return null;
    }

    public byte[] decipher(byte[] text) {
        return null;
    }

    public String decipher(String inputFilePath) {
        return null;
    }
}
