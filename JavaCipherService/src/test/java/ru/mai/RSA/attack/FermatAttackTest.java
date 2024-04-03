package ru.mai.RSA.attack;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigInteger;

import static org.testng.Assert.*;

public class FermatAttackTest {
    @DataProvider(name = "testDataAttack")
    public Object[][] testDataText() {
        return new Object[][]{
                {new BigInteger("6792605526025"), new BigInteger("9449868410449"), new BigInteger("569")},
                {new BigInteger("1073780833"), new BigInteger("1220275921"), new BigInteger("25")},
                {new BigInteger("1779399043"), new BigInteger("2796304957"), new BigInteger("11")},
        };
    }

    @Test(dataProvider = "testDataAttack")
    public void smartTest(BigInteger[] publicKeyAndD) {
        FermatAttack fermatAttack = new FermatAttack();
        BigInteger e = publicKeyAndD[0];
        BigInteger N = publicKeyAndD[1];
        BigInteger d = fermatAttack.attack(e, N).getLeft();
        assertEquals(d, publicKeyAndD[2]);
    }
}