package ru.mai;

import lombok.extern.slf4j.Slf4j;
import ru.mai.rijndael.Rijndael;

import java.util.Arrays;

@Slf4j
public class Main {
    public static void main(String[] args) {
        byte[] key = {
                (byte) 0x2b, (byte) 0x28, (byte) 0xab, (byte) 0x09,
                (byte) 0x7e, (byte) 0xae, (byte) 0xf7, (byte) 0xcf,
                (byte) 0x15, (byte) 0xd2, (byte) 0x15, (byte) 0x4f,
                (byte) 0x16, (byte) 0xa6, (byte) 0x88, (byte) 0x3c
        };
        byte[] testText128 = {
                (byte) 0x32, (byte) 0x88, (byte) 0x31, (byte) 0xe0,
                (byte) 0x43, (byte) 0x5a, (byte) 0x31, (byte) 0x37,
                (byte) 0xf6, (byte) 0x30, (byte) 0x98, (byte) 0x07,
                (byte) 0xa8, (byte) 0x8d, (byte) 0xa2, (byte) 0x34
        };
        byte[] testText192 = {
                (byte) 0x32, (byte) 0x88, (byte) 0x31, (byte) 0xe0, (byte) 0x31, (byte) 0xe0,
                (byte) 0x43, (byte) 0x5a, (byte) 0x31, (byte) 0x37, (byte) 0x31, (byte) 0xe0,
                (byte) 0xf6, (byte) 0x30, (byte) 0x98, (byte) 0x07, (byte) 0x31, (byte) 0xe0,
                (byte) 0xa8, (byte) 0x8d, (byte) 0xa2, (byte) 0x34, (byte) 0x31, (byte) 0xe0
        };
        byte[] testText256 = {
                (byte) 0x32, (byte) 0x88, (byte) 0x31, (byte) 0xe0, (byte) 0x32, (byte) 0x88, (byte) 0x31, (byte) 0xe0,
                (byte) 0x43, (byte) 0x5a, (byte) 0x31, (byte) 0x37, (byte) 0x32, (byte) 0x88, (byte) 0x31, (byte) 0xe0,
                (byte) 0xf6, (byte) 0x30, (byte) 0x98, (byte) 0x07, (byte) 0x32, (byte) 0x88, (byte) 0x31, (byte) 0xe0,
                (byte) 0xa8, (byte) 0x8d, (byte) 0xa2, (byte) 0x34, (byte) 0x32, (byte) 0x88, (byte) 0x31, (byte) 0xe0
        };
        Rijndael rijndael = new Rijndael(key, 256, 128, (char) 0b100011011);
        byte[] encryptedText = rijndael.encryptBlock(testText256);
        byte[] decryptedText = rijndael.decryptBlock(encryptedText);
        System.out.println(Arrays.toString(testText256));
        System.out.println(Arrays.toString(decryptedText));
        System.out.println(Arrays.equals(testText256, decryptedText));
    }
}
