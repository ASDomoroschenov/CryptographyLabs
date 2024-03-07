package ru.mai;

import lombok.extern.slf4j.Slf4j;
import ru.mai.encryption.encryption_impl.DES.DES;

import java.util.Arrays;

@Slf4j
public class Main {
    public static void main(String[] args) {
        DES des = new DES();

        byte[] key = {2, 3, 4, 5, 6, 7, 8};
        byte[] bytes = {1, 3, 3, 4, 5, 6, 7, 8};
        byte[] encBytes = des.encrypt(bytes, key);
        byte[] result = des.decrypt(encBytes, key);

        log.info(Arrays.toString(result));
    }
}

//1 2 3 4 5 6 7 8
//  2 3 4 5 6 7 8