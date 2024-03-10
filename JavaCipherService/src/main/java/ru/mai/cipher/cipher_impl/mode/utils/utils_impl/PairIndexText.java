package ru.mai.cipher.cipher_impl.mode.utils.utils_impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class PairIndexText {
    private Integer index;
    private byte[] text;
}
