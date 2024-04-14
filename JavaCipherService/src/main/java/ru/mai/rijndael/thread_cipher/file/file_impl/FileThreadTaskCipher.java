package ru.mai.rijndael.thread_cipher.file.file_impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mai.rijndael.Rijndael;
import ru.mai.rijndael.thread_cipher.file.file_interface.IFileThreadCipher;
import ru.mai.rijndael.thread_cipher.file.file_interface.IFileThreadTaskCipher;

import java.io.IOException;
import java.io.RandomAccessFile;

@Slf4j
@AllArgsConstructor
public class FileThreadTaskCipher implements IFileThreadTaskCipher {
    private Rijndael rijndael;

    @Override
    public byte[] apply(String pathToInputFile, long skipValue, long sizePartBytesThread, IFileThreadCipher.CipherAction action) throws IOException {
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

        return switch (action) {
            case ENCRYPT -> rijndael.encryptWithoutPadding(text);
            case DECRYPT -> rijndael.decryptWithoutPadding(text);
        };
    }
}
