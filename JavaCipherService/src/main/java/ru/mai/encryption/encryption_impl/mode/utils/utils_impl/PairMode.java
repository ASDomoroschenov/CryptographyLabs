package ru.mai.encryption.encryption_impl.mode.utils.utils_impl;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PairMode {
    private Integer index;
    private byte[] text;
}
