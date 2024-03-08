package ru.mai.cipher.cipher_impl.mode.utils.utils_interface;

import ru.mai.cipher.cipher_impl.mode.utils.utils_impl.PairMode;

public interface IThreadTask {
    PairMode apply(byte[] text, int indexBegin, int textBlockSize, int countBlocks);
}
