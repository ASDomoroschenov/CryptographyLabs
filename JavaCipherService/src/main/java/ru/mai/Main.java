package ru.mai;

import lombok.extern.slf4j.Slf4j;
import ru.mai.service.ModuloService;

import java.math.BigInteger;

@Slf4j
public class Main {
    public static void main(String[] args) {
        System.out.println(ModuloService.fastPowMod(
                new BigInteger("2"),
                new BigInteger("5"),
                new BigInteger("100")
        ));
    }
}