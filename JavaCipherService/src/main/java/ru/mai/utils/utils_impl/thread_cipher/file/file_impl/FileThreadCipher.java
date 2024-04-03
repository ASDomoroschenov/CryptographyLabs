package ru.mai.utils.utils_impl.thread_cipher.file.file_impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mai.utils.utils_impl.thread_cipher.file.file_interface.IFileThreadCipher;
import ru.mai.utils.utils_impl.thread_cipher.file.file_interface.IFileThreadTask;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@AllArgsConstructor
public class FileThreadCipher implements IFileThreadCipher {
    private int blockSize;
    private IFileThreadTask threadTask;

    @Override
    public String cipher(String pathToInputFile, BigInteger firstPartKey, BigInteger secondPartKey) {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(availableProcessors);
        List<Future<byte[]>> futures = new ArrayList<>();

        try (RandomAccessFile file = new RandomAccessFile(pathToInputFile, "r")) {
            long skipValue = 0;
            long sizePartsThread = ((file.length() / blockSize) + availableProcessors - 1) / availableProcessors;
            long sizePartBytesThread = sizePartsThread * blockSize;

            while (skipValue < file.length()) {
                long finalSkipValue = skipValue;
                futures.add(service.submit(() -> threadTask.apply(pathToInputFile, finalSkipValue, sizePartBytesThread, firstPartKey, secondPartKey)));
                skipValue += sizePartBytesThread;
            }
        } catch (IOException ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }

        String pathToOutputFile = "/home/alexandr/CryptographyLabs/JavaCipherService/src/main/resources/test.txt";

        try (RandomAccessFile file = new RandomAccessFile(pathToOutputFile, "rw")) {
            for (Future<byte[]> future : futures) {
                byte[] text = future.get();
                file.write(text);
            }
        } catch (IOException | ExecutionException | InterruptedException ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }

        service.shutdown();

        try {
            if (!service.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            service.shutdownNow();
        }

        return pathToOutputFile;
    }
}
