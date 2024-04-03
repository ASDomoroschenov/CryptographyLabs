package ru.mai;

import lombok.extern.slf4j.Slf4j;
import ru.mai.RSA.attack.WienerAttack;

import java.math.BigInteger;

@Slf4j
public class Main {
    public static void main(String[] args) {
        BigInteger e = new BigInteger("6792605526025");
        BigInteger N = new BigInteger("9449868410449");
        WienerAttack wienerAttack = new WienerAttack();
        System.out.println(wienerAttack.attack(e, N));
    }
}