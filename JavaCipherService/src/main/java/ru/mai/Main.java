package ru.mai;

import lombok.extern.slf4j.Slf4j;
import ru.mai.rijndael.GF;

import java.util.List;

@Slf4j
public class Main {
    public static void main(String[] args) {
        List<Character> irreducibleList = GF.getIrreducible();

        for (int i = 1; i < 255; i++) {
            for (char moduloChar : irreducibleList) {
                byte polynomial = (byte) i;
                byte invert = GF.invert(polynomial, moduloChar);

                if (GF.multiplicationModulo(invert, polynomial, moduloChar) != 1) {
                    log.error("error");
                }
            }
        }
    }
}
