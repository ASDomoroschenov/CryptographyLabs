package ru.mai.rijndael.thread_cipher.text.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PairIndexText {
    private Integer index;
    private byte[] text;
}