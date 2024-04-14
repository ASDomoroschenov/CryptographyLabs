package ru.mai;

import lombok.extern.slf4j.Slf4j;
import ru.mai.rijndael.Rijndael;
import ru.mai.rijndael.RijndaelImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class Main {
    public static void main(String[] args) throws IOException {
        byte[] key = {
                (byte) 0x2b, (byte) 0x28, (byte) 0xab, (byte) 0x09,
                (byte) 0x7e, (byte) 0xae, (byte) 0xf7, (byte) 0xcf,
                (byte) 0x15, (byte) 0xd2, (byte) 0x15, (byte) 0x4f,
                (byte) 0x16, (byte) 0xa6, (byte) 0x88, (byte) 0x3c
        };
        String pathToFile = Objects.requireNonNull(Main.class.getClassLoader().getResource("./ru/mai/test/input/text.txt")).getPath();

        Rijndael rijndael = new RijndaelImpl(key, 128, 128, (char) 0b100011011, Rijndael.PaddingMode.ANSI_X_923);

        long begin = System.currentTimeMillis();

        String encryptedFile = rijndael.encryptFile(pathToFile);

        log.info("Time encrypt is {}s", (System.currentTimeMillis() - begin) / 1000.0);

        String decryptedFile = rijndael.decryptFile(encryptedFile);

        log.info("Result of cipher is {}", Arrays.equals(
                Files.readAllBytes(Path.of(pathToFile)),
                Files.readAllBytes(Path.of(decryptedFile))
        ));
    }
}