package ru.mai.encryption.encryption_impl.mode.utils.utils_interface;

import ru.mai.encryption.encryption_impl.mode.utils.utils_impl.PairMode;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface ICollectText {
    byte[] collect(List<Future<PairMode>> futures, int textLength) throws ExecutionException, InterruptedException;
}
