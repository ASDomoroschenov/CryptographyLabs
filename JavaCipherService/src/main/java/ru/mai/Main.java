package ru.mai;

import lombok.extern.slf4j.Slf4j;
import ru.mai.RSA.RSA;
import ru.mai.RSA.stream_mode.CTR.CTRMode;
import ru.mai.RSA.stream_mode.StreamRSA;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Slf4j
public class Main {
    public static void main(String[] args) throws IOException {
        StreamRSA streamRSA = new StreamRSA(
                new RSA(
                        RSA.PrimeTest.MILLER_RABIN,
                        0.999,
                        100
                ),
                StreamRSA.STREAM.CTR
        );

        byte[] text = Files.readAllBytes(Path.of("/home/alexandr/CryptographyLabs/JavaCipherService/src/main/resources/file.txt"));
        byte[] encText = streamRSA.encrypt(text);
        byte[] decText = streamRSA.decrypt(encText);
        System.out.println(Arrays.toString(text));
        System.out.println(Arrays.toString(decText));
        System.out.println(Arrays.equals(text, decText));
    }
}