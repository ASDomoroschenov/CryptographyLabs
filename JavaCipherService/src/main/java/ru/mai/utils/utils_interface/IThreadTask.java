package ru.mai.utils.utils_interface;

import ru.mai.utils.utils_impl.PairIndexText;

import java.util.concurrent.ExecutionException;

public interface IThreadTask {
    PairIndexText apply(byte[] text, int indexBegin, int textBlockSize, int countBlocks) throws ExecutionException, InterruptedException;
}
