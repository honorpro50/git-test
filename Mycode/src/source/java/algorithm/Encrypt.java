package algorithm;

import Result.EncResult;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class Encrypt {
    /**
     *
     * @param m
     * @return
     */
    public EncResult Encrypt(BigInteger m, BigInteger g, BigInteger h, BigInteger n) {
        BigInteger n2 = n.multiply(n);
        BigInteger k = getRandoma(n);
        //k = new BigInteger(n.bitLength() - 2, r);
        k = k.abs();
        // 计算 c0
        BigInteger c0 = h.modPow(k, n2).multiply(BigInteger.ONE.add(m.multiply(n))).mod(n2);
        //c0 = message.multiply(h.modPow(k, n2)).mod(n2);
        // 计算 c1
        BigInteger c1 = g.modPow(k, n2);
        EncResult c = new EncResult(c0, c1);
        //c.set_c0(c0);
        //c.set_c1(c1);
        return c;
    }

    /**
     * 输出两个明文的和的加密结果
     * @param a
     * @param r
     * @param n2
     * @return
     */
    public EncResult ADD(EncResult a, EncResult r, BigInteger n){
        BigInteger n2 = n.multiply(n);
        BigInteger c0 = a.c0.multiply(r.c0).mod(n2);
        BigInteger c1 = a.c1.multiply(r.c1).mod(n2);
        EncResult c = new EncResult(c0,c1);
        return c;
    }

    /**
     * 输出两个
     * @param a
     * @param R
     * @param n2
     * @return
     */
    public EncResult Mod_R(EncResult a, BigInteger R, BigInteger n){
        BigInteger n2 = n.multiply(n);
        BigInteger c0 = a.c0.modPow(R,n2);
        BigInteger c1 = a.c1.modPow(R,n2);
        EncResult c = new EncResult(c0,c1);

        return c;
    }

    public static BigInteger getRandoma(BigInteger b) {
        BigInteger a;
        Random random = new SecureRandom();
//        a = new BigInteger(b.divide(BigInteger.valueOf(4)).bitLength(), random);
        do {
            a = new BigInteger(b.divide(BigInteger.valueOf(4)).bitLength(), random);
        } while (a.compareTo(BigInteger.ONE) < 0);
        // 确保生成的随机数在[1, n/4]范围内
        return a;
    }

}
