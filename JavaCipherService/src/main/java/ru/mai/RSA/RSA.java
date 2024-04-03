package ru.mai.RSA;

import lombok.extern.slf4j.Slf4j;
import ru.mai.RSA.padding.IPadding;
import ru.mai.RSA.padding.padding_impl.ANSIX923Padding;
import ru.mai.primality_test.IPrimalityTest;
import ru.mai.primality_test.test.FermatTestI;
import ru.mai.primality_test.test.MillerRabinTestI;
import ru.mai.primality_test.test.SoloveStrassenTestI;
import ru.mai.service.ModuloService;
import ru.mai.utils.utils_impl.BigIntegerRandomGenerator;
import ru.mai.utils.utils_impl.thread_cipher.file.file_impl.FileThreadCipher;
import ru.mai.utils.utils_impl.thread_cipher.file.file_impl.FileThreadTaskCipher;
import ru.mai.utils.utils_impl.thread_cipher.text.text_impl.CollectText;
import ru.mai.utils.utils_impl.thread_cipher.text.text_impl.TextThreadCipher;
import ru.mai.utils.utils_impl.thread_cipher.text.text_impl.TextThreadTaskCipher;

import java.io.File;
import java.math.BigInteger;
import java.util.Arrays;

@Slf4j
public class RSA {
    private final GenerateKeyRSA generateKeyRSA;
    private final IPadding padding;

    public enum PrimeTest {
        FERMAT,
        MILLER_RABIN,
        SOLOVE_STRASSEN
    }

    public RSA(PrimeTest primeTest, double minProbability, int bitLength) {
        this.generateKeyRSA = new GenerateKeyRSA(primeTest, minProbability, bitLength);
        this.padding = new ANSIX923Padding();
    }

    public BigInteger[][] getKey() {
        return generateKeyRSA.generateKey();
    }

    public byte[] cipherConversionBlock(byte[] textBlock, BigInteger firstPartKey, BigInteger secondPartKey, int sizeOutputBlock) {
        byte[] result = ModuloService.fastPowMod(new BigInteger(textBlock), firstPartKey, secondPartKey).toByteArray();

        if (result.length != sizeOutputBlock) {
            byte[] temp = new byte[sizeOutputBlock];

            if (result.length > sizeOutputBlock) {
                System.arraycopy(result, result.length - sizeOutputBlock, temp, 0, sizeOutputBlock);
            } else {
                System.arraycopy(result, 0, temp, sizeOutputBlock - result.length, result.length);
            }

            return temp;
        }

        return result;
    }

    public byte[] encryptParallel(byte[] text, BigInteger e, BigInteger N) {
        try {
            int sizeInputBlock = N.toByteArray().length - 2;
            int sizeOutputBlock = N.toByteArray().length;

            return new TextThreadCipher(
                    sizeInputBlock,
                    sizeOutputBlock,
                    new TextThreadTaskCipher(this),
                    new CollectText()
            ).cipher(padding.addPAdding(text, sizeInputBlock), e, N);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }

        return null;
    }

    public byte[] encrypt(byte[] text, BigInteger e, BigInteger N) {
        try {
            int sizeInputBlock = N.toByteArray().length - 2;
            int sizeOutputBlock = N.toByteArray().length;
            text = padding.addPAdding(text, sizeInputBlock);
            return cipherConversion(
                    text,
                    e,
                    N,
                    sizeInputBlock,
                    sizeOutputBlock
            );
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }

        return null;
    }

    public byte[] decryptParallel(byte[] text, BigInteger d, BigInteger N) {
        try {
            int sizeInputBlock = N.toByteArray().length;
            int sizeOutputBlock = N.toByteArray().length - 2;

            return padding.removePadding(new TextThreadCipher(
                    sizeInputBlock,
                    sizeOutputBlock,
                    new TextThreadTaskCipher(this),
                    new CollectText()
            ).cipher(text, d, N));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }

        return null;
    }

    public byte[] decrypt(byte[] text, BigInteger d, BigInteger N) {
        try {
            int sizeInputBlock = N.toByteArray().length;
            int sizeOutputBlock = N.toByteArray().length - 2;
            return padding.removePadding(cipherConversion(
                    text,
                    d,
                    N,
                    sizeInputBlock,
                    sizeOutputBlock
            ));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }

        return null;
    }

    public byte[] cipherConversion(byte[] text, BigInteger firstPartKey, BigInteger secondPartKey, int sizeInputBlock, int sizeOutputBlock) {
        byte[] result = new byte[(text.length / sizeInputBlock) * sizeOutputBlock];

        for (int i = 0; i < text.length; i += sizeInputBlock) {
            byte[] block = new byte[sizeInputBlock + 1];
            System.arraycopy(text, i, block, 1, sizeInputBlock);
            block = cipherConversionBlock(block, firstPartKey, secondPartKey, sizeOutputBlock);
            System.arraycopy(block, 0, result, (i / sizeInputBlock) * sizeOutputBlock, sizeOutputBlock);
        }

        return result;
    }

    public String encryptFile(String pathToInputFile, BigInteger e, BigInteger N) {
        try {
            int sizeInputBlock = N.toByteArray().length - 2;
            int sizeOutputBlock = N.toByteArray().length;
            String pathToFileWithPadding = padding.addPAdding(pathToInputFile, sizeInputBlock);
            String pathToOutputFile = addPostfixToFileName(pathToInputFile, "-enc");

            if (new File(pathToOutputFile).exists()) {
                new File(pathToOutputFile).delete();
            }

            String pathToFileEncrypt = new FileThreadCipher(
                    sizeInputBlock,
                    sizeOutputBlock,
                    new FileThreadTaskCipher(this)
            ).cipher(pathToFileWithPadding, pathToOutputFile, e, N);

            if (!(new File(pathToFileWithPadding).delete())) {
                log.error("There is no such file");
            }

            return pathToFileEncrypt;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }

        return "";
    }

    public String decryptFile(String pathToInputFile, BigInteger d, BigInteger N) {
        try {
            int sizeInputBlock = N.toByteArray().length;
            int sizeOutputBlock = N.toByteArray().length - 2;
            String pathToDecryptFile = addPostfixToFileName(pathToInputFile, "-dec");

            if (new File(pathToDecryptFile).exists()) {
                new File(pathToDecryptFile).delete();
            }

            String pathToFileDecrypt = padding.removePadding(new FileThreadCipher(
                    sizeInputBlock,
                    sizeOutputBlock,
                    new FileThreadTaskCipher(this)
            ).cipher(pathToInputFile, addPostfixToFileName(pathToInputFile, "-dec"), d, N));

            if (!(new File(pathToDecryptFile).delete())) {
                log.error("There is no such file");
            }

            if (!(new File(pathToFileDecrypt).renameTo(new File(pathToDecryptFile)))) {
                log.error("There is no such file");
            }

            return pathToDecryptFile;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }

        return "";
    }

    private String addPostfixToFileName(String pathToInputFile, String postfix) {
        int dotIndex = pathToInputFile.lastIndexOf('.');
        String baseName = pathToInputFile.substring(0, dotIndex);
        String extension = pathToInputFile.substring(dotIndex);
        return baseName + postfix + extension;
    }

    public static class GenerateKeyRSA {
        private final IPrimalityTest test;
        private final double minProbability;
        private final int bitLength;
        private final BigIntegerRandomGenerator random;

        public GenerateKeyRSA(PrimeTest primeTest, double minProbability, int bitLength) {
            this.test = getPrimalityTest(primeTest);
            this.minProbability = minProbability;
            this.bitLength = bitLength;
            this.random = new BigIntegerRandomGenerator();
        }

        private IPrimalityTest getPrimalityTest(PrimeTest primeTest) {
            switch (primeTest) {
                case FERMAT -> {
                    return new FermatTestI();
                }
                case MILLER_RABIN -> {
                    return new MillerRabinTestI();
                }
                case SOLOVE_STRASSEN -> {
                    return new SoloveStrassenTestI();
                }
            }

            return null;
        }

        public BigInteger[][] generateKey() {
            BigInteger[] bigIntegerPair;
            BigInteger p;
            BigInteger q;
            BigInteger N;
            BigInteger eulerFunction;
            BigInteger e;
            BigInteger d;

            do {
                bigIntegerPair = generatePairPrime();
                p = bigIntegerPair[0];
                q = bigIntegerPair[1];
                N = p.multiply(q);
                eulerFunction = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
                e = random.generateRelativelyPrime(eulerFunction);
                d = ModuloService.gcdExtended(e, eulerFunction)[1];
            } while (d.compareTo(N.sqrt().sqrt().divide(BigInteger.valueOf(3))) < 0);

            return new BigInteger[][]{
                    {e, N},
                    {d, N}
            };
        }

        private BigInteger[] generatePairPrime() {
            BigInteger p = random.generatePrime(bitLength, test, minProbability);
            BigInteger q;

            do {
                q = random.generatePrime(bitLength, test, minProbability);
            } while (!(test.isProbablyPrime(q, minProbability) &&
                    p.subtract(q).abs().compareTo(p.multiply(q).sqrt()) > 0));

            return new BigInteger[]{p, q};
        }
    }
}
