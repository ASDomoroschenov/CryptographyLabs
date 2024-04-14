package ru.mai.rijndael;

import org.testng.annotations.Test;

import static org.testng.internal.junit.ArrayAsserts.assertArrayEquals;

public class RijndaelTest {
    private static final byte[] KEY = {
            (byte) 0x2b, (byte) 0x28, (byte) 0xab, (byte) 0x09,
            (byte) 0x7e, (byte) 0xae, (byte) 0xf7, (byte) 0xcf,
            (byte) 0x15, (byte) 0xd2, (byte) 0x15, (byte) 0x4f,
            (byte) 0x16, (byte) 0xa6, (byte) 0x88, (byte) 0x3c
    };
    private static final Rijndael RIJNDAEL128 = new RijndaelImpl(KEY, 128, 128, (char) 0b100011011, Rijndael.PaddingMode.ANSI_X_923);
    private static final Rijndael RIJNDAEL192 = new RijndaelImpl(KEY, 192, 128, (char) 0b100011011, Rijndael.PaddingMode.ANSI_X_923);
    private static final Rijndael RIJNDAEL256 = new RijndaelImpl(KEY, 256, 128, (char) 0b100011011, Rijndael.PaddingMode.ANSI_X_923);

    @Test
    public void testSubBytes() {
        byte[][] testSubBytes = {
                {(byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0x3d, (byte) 0xf4, (byte) 0xc6, (byte) 0xf8},
                {(byte) 0xe3, (byte) 0xe2, (byte) 0x8d, (byte) 0x48},
                {(byte) 0xbe, (byte) 0x2b, (byte) 0x2a, (byte) 0x08}
        };
        byte[][] expected = {
                {(byte) 0xd4, (byte) 0xe0, (byte) 0xb8, (byte) 0x1e},
                {(byte) 0x27, (byte) 0xbf, (byte) 0xb4, (byte) 0x41},
                {(byte) 0x11, (byte) 0x98, (byte) 0x5d, (byte) 0x52},
                {(byte) 0xae, (byte) 0xf1, (byte) 0xe5, (byte) 0x30}
        };

        assertArrayEquals(expected, RIJNDAEL128.subBytes(testSubBytes));
    }

    @Test
    public void testInvSubBytes() {
        byte[][] test128 = {
                {(byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0x3d, (byte) 0xf4, (byte) 0xc6, (byte) 0xf8},
                {(byte) 0xe3, (byte) 0xe2, (byte) 0x8d, (byte) 0x48},
                {(byte) 0xbe, (byte) 0x2b, (byte) 0x2a, (byte) 0x08}
        };
        byte[][] test192 = {
                {(byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0x3d, (byte) 0xf4, (byte) 0xc6, (byte) 0xf8, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0xe3, (byte) 0xe2, (byte) 0x8d, (byte) 0x48, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0xbe, (byte) 0x2b, (byte) 0x2a, (byte) 0x08, (byte) 0x9a, (byte) 0xe9}
        };
        byte[][] test256 = {
                {(byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9, (byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0x3d, (byte) 0xf4, (byte) 0xc6, (byte) 0xf8, (byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0xe3, (byte) 0xe2, (byte) 0x8d, (byte) 0x48, (byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0xbe, (byte) 0x2b, (byte) 0x2a, (byte) 0x08, (byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9}
        };

        assertArrayEquals(test128, RIJNDAEL128.invSubBytes(RIJNDAEL128.subBytes(test128)));
        assertArrayEquals(test192, RIJNDAEL192.invSubBytes(RIJNDAEL192.subBytes(test192)));
        assertArrayEquals(test256, RIJNDAEL256.invSubBytes(RIJNDAEL256.subBytes(test256)));
    }

    @Test
    public void testShiftRows() {
        byte[][] testShiftRows = {
                {(byte) 0xd4, (byte) 0xe0, (byte) 0xb8, (byte) 0x1e},
                {(byte) 0x27, (byte) 0xbf, (byte) 0xb4, (byte) 0x41},
                {(byte) 0x11, (byte) 0x98, (byte) 0x5d, (byte) 0x52},
                {(byte) 0xae, (byte) 0xf1, (byte) 0xe5, (byte) 0x30}
        };
        byte[][] expected = {
                {(byte) 0xd4, (byte) 0xe0, (byte) 0xb8, (byte) 0x1e},
                {(byte) 0xbf, (byte) 0xb4, (byte) 0x41, (byte) 0x27},
                {(byte) 0x5d, (byte) 0x52, (byte) 0x11, (byte) 0x98},
                {(byte) 0x30, (byte) 0xae, (byte) 0xf1, (byte) 0xe5}
        };

        assertArrayEquals(expected, RIJNDAEL128.shiftRows(testShiftRows));
    }

    @Test
    public void testInvShiftRows() {
        byte[][] test128 = {
                {(byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0x3d, (byte) 0xf4, (byte) 0xc6, (byte) 0xf8},
                {(byte) 0xe3, (byte) 0xe2, (byte) 0x8d, (byte) 0x48},
                {(byte) 0xbe, (byte) 0x2b, (byte) 0x2a, (byte) 0x08}
        };
        byte[][] test192 = {
                {(byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0x3d, (byte) 0xf4, (byte) 0xc6, (byte) 0xf8, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0xe3, (byte) 0xe2, (byte) 0x8d, (byte) 0x48, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0xbe, (byte) 0x2b, (byte) 0x2a, (byte) 0x08, (byte) 0x9a, (byte) 0xe9}
        };
        byte[][] test256 = {
                {(byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9, (byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0x3d, (byte) 0xf4, (byte) 0xc6, (byte) 0xf8, (byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0xe3, (byte) 0xe2, (byte) 0x8d, (byte) 0x48, (byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0xbe, (byte) 0x2b, (byte) 0x2a, (byte) 0x08, (byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9}
        };

        assertArrayEquals(test128, RIJNDAEL128.invShiftRows(RIJNDAEL128.shiftRows(test128)));
        assertArrayEquals(test192, RIJNDAEL192.invShiftRows(RIJNDAEL192.shiftRows(test192)));
        assertArrayEquals(test256, RIJNDAEL256.invShiftRows(RIJNDAEL256.shiftRows(test256)));
    }

    @Test
    public void testMixColumns() {
        byte[][] testMixColumns = {
                {(byte) 0xd4, (byte) 0xe0, (byte) 0xb8, (byte) 0x1e},
                {(byte) 0xbf, (byte) 0xb4, (byte) 0x41, (byte) 0x27},
                {(byte) 0x5d, (byte) 0x52, (byte) 0x11, (byte) 0x98},
                {(byte) 0x30, (byte) 0xae, (byte) 0xf1, (byte) 0xe5}
        };
        byte[][] expected = {
                {(byte) 0x04, (byte) 0xe0, (byte) 0x48, (byte) 0x28},
                {(byte) 0x66, (byte) 0xcb, (byte) 0xf8, (byte) 0x06},
                {(byte) 0x81, (byte) 0x19, (byte) 0xd3, (byte) 0x26},
                {(byte) 0xe5, (byte) 0x9a, (byte) 0x7a, (byte) 0x4c}
        };

        assertArrayEquals(expected, RIJNDAEL128.mixColumns(testMixColumns));
    }

    @Test
    public void testInvMixColumns() {
        byte[][] test128 = {
                {(byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0x3d, (byte) 0xf4, (byte) 0xc6, (byte) 0xf8},
                {(byte) 0xe3, (byte) 0xe2, (byte) 0x8d, (byte) 0x48},
                {(byte) 0xbe, (byte) 0x2b, (byte) 0x2a, (byte) 0x08}
        };
        byte[][] test192 = {
                {(byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0x3d, (byte) 0xf4, (byte) 0xc6, (byte) 0xf8, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0xe3, (byte) 0xe2, (byte) 0x8d, (byte) 0x48, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0xbe, (byte) 0x2b, (byte) 0x2a, (byte) 0x08, (byte) 0x9a, (byte) 0xe9}
        };
        byte[][] test256 = {
                {(byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9, (byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0x3d, (byte) 0xf4, (byte) 0xc6, (byte) 0xf8, (byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0xe3, (byte) 0xe2, (byte) 0x8d, (byte) 0x48, (byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9},
                {(byte) 0xbe, (byte) 0x2b, (byte) 0x2a, (byte) 0x08, (byte) 0x19, (byte) 0xa0, (byte) 0x9a, (byte) 0xe9}
        };

        assertArrayEquals(test128, RIJNDAEL128.invMixColumns(RIJNDAEL128.mixColumns(test128)));
        assertArrayEquals(test192, RIJNDAEL192.invMixColumns(RIJNDAEL192.mixColumns(test192)));
        assertArrayEquals(test256, RIJNDAEL256.invMixColumns(RIJNDAEL256.mixColumns(test256)));
    }
}