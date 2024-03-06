package ru.mai;

import lombok.extern.slf4j.Slf4j;
import ru.mai.utils.BitsUtil;

@Slf4j
public class Main {
    public static void main(String[] args) {
        byte[] arrayBits = new byte[] {1};
        int[] permutationValues = new int[] {8, 8, 8};

        try {
            byte[] resultPermutation = BitsUtil.permutation(arrayBits, permutationValues);
            BitsUtil.outputBits(resultPermutation);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
    }
}