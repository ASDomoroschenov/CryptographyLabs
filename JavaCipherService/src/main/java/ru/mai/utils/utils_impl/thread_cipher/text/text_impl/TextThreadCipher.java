package ru.mai.utils.utils_impl.thread_cipher.text.text_impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mai.utils.utils_impl.thread_cipher.text.PairIndexText;
import ru.mai.utils.utils_impl.thread_cipher.text.text_interface.ICollectText;
import ru.mai.utils.utils_impl.thread_cipher.text.text_interface.ITextThreadCipher;
import ru.mai.utils.utils_impl.thread_cipher.text.text_interface.ITextThreadTask;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@AllArgsConstructor
public class TextThreadCipher implements ITextThreadCipher {
    private int sizeInputBlock;
    private int sizeOutputBlock;
    private ITextThreadTask threadTask;
    private ICollectText collectText;

    @Override
    public byte[] cipher(byte[] text, BigInteger firstPartKey, BigInteger secondPartKey) throws Exception {
        if (text == null) {
            throw new IllegalArgumentException("Illegal bytes text");
        }

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(availableProcessors);
        List<Future<PairIndexText>> futures = new ArrayList<>();
        int countBlocks = ((text.length / sizeInputBlock) + availableProcessors - 1) / availableProcessors;

        for (int i = 0; i < text.length; i += sizeInputBlock * countBlocks) {
            int finalI = i;
            int finalCountBlocks = i + sizeInputBlock * countBlocks < text.length ? countBlocks : (text.length - i) / sizeInputBlock;
            futures.add(service.submit(() -> threadTask.apply(text, firstPartKey, secondPartKey, finalI, finalCountBlocks)));
        }

        byte[] result;

        try {
            result = collectText.collect(futures, (text.length / sizeInputBlock) * sizeOutputBlock);
        } catch (InterruptedException | ExecutionException ex) {
            throw new Exception(ex);
        } finally {
            service.shutdown();

            try {
                if (!service.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                    service.shutdownNow();
                }
            } catch (InterruptedException e) {
                service.shutdownNow();
            }
        }

        return result;
    }
}
