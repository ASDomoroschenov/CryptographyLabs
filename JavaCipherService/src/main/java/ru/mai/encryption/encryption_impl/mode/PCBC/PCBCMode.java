package ru.mai.encryption.encryption_impl.mode.PCBC;

import lombok.AllArgsConstructor;
import ru.mai.encryption.encryption_interface.ICipher;
import ru.mai.encryption.encryption_interface.ICipherMode;
import ru.mai.utils.BytesUtil;

@AllArgsConstructor
public class PCBCMode implements ICipherMode {
    private ICipher cipher;
    private byte[] initialVector;

    @Override
    public byte[] encrypt(byte[] text) {
        int textBlockSize = cipher.getTextBlockSize();
        byte[] textBlock = new byte[textBlockSize];
        byte[] prevTextBlock = new byte[textBlockSize];
        byte[] cipherBlock = initialVector;
        byte[] result = new byte[text.length];

        for (int i = 0; i < text.length; i += textBlockSize) {
            System.arraycopy(text, i, textBlock, 0, textBlockSize);
            cipherBlock = cipher.encrypt(BytesUtil.xor(BytesUtil.xor(textBlock, prevTextBlock), cipherBlock));
            prevTextBlock = textBlock.clone();
            System.arraycopy(cipherBlock, 0, result, i, textBlockSize);
        }

        return result;
    }

    @Override
    public byte[] decrypt(byte[] text) {
        int textBlockSize = cipher.getTextBlockSize();
        byte[] textBlock = new byte[textBlockSize];
        byte[] prevCipherBlock = new byte[textBlockSize];
        byte[] deCipherBlock = initialVector;
        byte[] result = new byte[text.length];

        for (int i = 0; i < text.length; i += textBlockSize) {
            System.arraycopy(text, i, textBlock, 0, textBlockSize);
            deCipherBlock = BytesUtil.xor(BytesUtil.xor(cipher.decrypt(textBlock), prevCipherBlock), deCipherBlock);
            prevCipherBlock = textBlock.clone();
            System.arraycopy(deCipherBlock, 0, result, i, textBlockSize);
        }

        return result;
    }
}
