package ru.mai.RSA.stream_mode;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.mai.RSA.RSA;

import java.util.Random;

import static org.testng.internal.junit.ArrayAsserts.assertArrayEquals;
import static ru.mai.RSA.RSA.PrimeTest.MILLER_RABIN;

public class StreamRSATest {
    @DataProvider(name = "testDataText")
    public Object[][] testDataText() {
        return new Object[][]{
                {
                        "Hello world!".getBytes()
                },
                {
                        "".getBytes()
                },
                {
                        """
                                Я лишился девственности в 20 лет. Не так, как это обычно бывает с парнями моего возраста. Все куда тяжелее - и никакой романтики.
                        """.getBytes()
                },
                {
                        """
                                Мой хомяк
                        """.getBytes()
                }
        };
    }

    @Test(dataProvider = "testDataText")
    public void testOFBBytes(byte[] testDataText) {
        StreamRSA streamRSA = new StreamRSA(
                new RSA(
                        MILLER_RABIN,
                        0.999,
                        new Random().nextInt(200) + 50),
                StreamRSA.STREAM.OFB
        );

        byte[] encryptedBytes = streamRSA.encrypt(testDataText);
        byte[] decryptedBytes = streamRSA.decrypt(encryptedBytes);

        assertArrayEquals(testDataText, decryptedBytes);
    }

    @Test(dataProvider = "testDataText")
    public void testCTRBytes(byte[] testDataText) {
        StreamRSA streamRSA = new StreamRSA(
                new RSA(
                        MILLER_RABIN,
                        0.999,
                        new Random().nextInt(200) + 50),
                StreamRSA.STREAM.CTR
        );

        byte[] encryptedBytes = streamRSA.encrypt(testDataText);
        byte[] decryptedBytes = streamRSA.decrypt(encryptedBytes);

        assertArrayEquals(testDataText, decryptedBytes);
    }
}