package ru.mai;

import lombok.extern.slf4j.Slf4j;
import ru.mai.utils.BitsUtil;
import ru.mai.utils.BytesUtil;

import java.util.Arrays;

@Slf4j
public class Main {
    public static void main(String[] args) {
        byte[] arrayBits = new byte[] {1};
        int[] permutationValues = new int[] {8, 8, 8};

        try {
            byte[] resultPermutation = BytesUtil.permutation(arrayBits, permutationValues);
            System.out.println(Arrays.toString(resultPermutation));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
    }
}