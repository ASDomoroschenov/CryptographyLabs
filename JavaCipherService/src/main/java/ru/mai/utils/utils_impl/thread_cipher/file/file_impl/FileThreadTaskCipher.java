package ru.mai.utils.utils_impl.thread_cipher.file.file_impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mai.RSA.RSA;
import ru.mai.utils.utils_impl.thread_cipher.file.file_interface.IFileThreadTask;
import ru.mai.utils.utils_impl.thread_cipher.text.text_impl.CollectText;
import ru.mai.utils.utils_impl.thread_cipher.text.text_impl.TextThreadCipher;
import ru.mai.utils.utils_impl.thread_cipher.text.text_impl.TextThreadTaskCipher;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;

@Slf4j
@AllArgsConstructor
public class FileThreadTaskCipher implements IFileThreadTask {
    private RSA rsa;

    @Override
    public byte[] apply(String pathToInputFile, long skipValue, long sizePartBytesThread, BigInteger firstPartKey, BigInteger secondPartKey, int sizeInputBlock, int sizeOutputBlock) throws Exception {
        byte[] text = new byte[(int) sizePartBytesThread];

        try (RandomAccessFile file = new RandomAccessFile(pathToInputFile, "r")) {
            file.seek(skipValue);
            int countBytes = file.read(text);

            if (countBytes != sizePartBytesThread) {
                byte[] trimText = new byte[countBytes];
                System.arraycopy(text, 0, trimText, 0, countBytes);
                text = trimText;
            }
        } catch (IOException ex) {
            throw new IOException(ex);
        }

        return new TextThreadCipher(
                sizeInputBlock,
                sizeOutputBlock,
                new TextThreadTaskCipher(rsa),
                new CollectText()
        ).cipher(text, firstPartKey, secondPartKey);
    }
}
