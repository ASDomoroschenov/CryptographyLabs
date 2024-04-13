package ru.mai;

import lombok.extern.slf4j.Slf4j;
import ru.mai.rijndael.Rijndael;

import java.util.Arrays;

@Slf4j
public class Main {
    public static void main(String[] args) {
        Rijndael rijndael = new Rijndael(null, 128, 192, (char) 0b100011011);
        byte[][] testMixColumns = {
                {(byte) 0xd4, (byte) 0xe0, (byte) 0xb8, (byte) 0x1e},
                {(byte) 0xbf, (byte) 0xb4, (byte) 0x41, (byte) 0x27},
                {(byte) 0x5d, (byte) 0x52, (byte) 0x11, (byte) 0x98},
                {(byte) 0x30, (byte) 0xae, (byte) 0xf1, (byte) 0xe5}
        };
        byte[][] testSubBytes = {
                {(byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0x3d, (byte) 0xf4, (byte) 0xc6, (byte) 0xf8},
                {(byte) 0xe3, (byte) 0xe2, (byte) 0x8d, (byte) 0x48},
                {(byte) 0xbe, (byte) 0x2b, (byte) 0x2a, (byte) 0x08}
        };
        byte[][] resultMiXColumns = rijndael.mixColumns(testMixColumns);
        byte[][] resultSubBytes = rijndael.subBytes(testSubBytes);

        for (int i = 0; i < resultSubBytes.length; i++) {
            System.out.print("{ ");
            for (int j = 0; j < resultSubBytes[i].length; j++) {
                System.out.print(String.format("0x%x\t", resultSubBytes[i][j]));
            }
            System.out.println(" }");
        }
    }
}
