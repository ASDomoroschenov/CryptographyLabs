package ru.mai;

import lombok.extern.slf4j.Slf4j;
import ru.mai.RSA.attack.FermatAttack;

import java.math.BigInteger;

@Slf4j
public class Main {
    public static void main(String[] args) {
        FermatAttack fermatAttack = new FermatAttack();
        System.out.println(fermatAttack.attack(new BigInteger("7"), new BigInteger("21")));
    }
}