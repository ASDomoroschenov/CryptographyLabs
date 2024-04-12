package ru.mai;

import lombok.extern.slf4j.Slf4j;
import ru.mai.rijndael.GF;

@Slf4j
public class Main {
    public static void main(String[] args) {
        char moduloChar = 0b111111111;
        byte modulo = 0b00011011;

        System.out.println(GF.isIrreducible(moduloChar));

        for (int i = 1; i < 255; i++) {
            byte polynomial = (byte) i;
            byte invert = GF.invert(polynomial, modulo);

            if (GF.multiplicationModulo(invert, polynomial, modulo) != 1) {
                System.out.println("YES");
            }
        }
    }
}
