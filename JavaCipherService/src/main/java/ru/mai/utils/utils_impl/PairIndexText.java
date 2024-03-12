package ru.mai.utils.utils_impl;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PairIndexText {
    private Integer index;
    private byte[] text;
}
