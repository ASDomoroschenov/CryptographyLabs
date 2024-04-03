package ru.mai.utils.utils_impl.thread_cipher.text;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PairIndexText {
    private Integer index;
    private byte[] text;
}