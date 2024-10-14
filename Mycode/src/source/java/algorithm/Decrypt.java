package algorithm;

import Result.EncResult;

import java.math.BigInteger;

public class Decrypt {

    public static BigInteger lcm(BigInteger a, BigInteger b) {
        return a.multiply(b).divide(a.gcd(b));
    }
    public BigInteger Function_l(BigInteger x,BigInteger n) {
        return x.subtract(BigInteger.ONE).divide(n);
    }
    public BigInteger WDecrypt(EncResult c,BigInteger sk,BigInteger n2,BigInteger n){
        //BigInteger u = c1.modPow(sk, n2).modInverse(n2);
        BigInteger c0 = c.c0;
        BigInteger c1 = c.c1;
        BigInteger m = Function_l(c0.multiply(c1.modPow(sk, n2).modInverse(n2)).mod(n2),n);
        //String str = new String(scy.toByteArray());
        return m;
    }

    public BigInteger SKDecrypt(EncResult c,BigInteger lambda,BigInteger n){
        BigInteger n2 = n.multiply(n);
        BigInteger c0 = c.c0;
        BigInteger u = c0.modPow(lambda, n2);
        BigInteger m = Function_l(u,n).multiply(lambda.modInverse(n)).mod(n);
        //String str = new String(scy.toByteArray());
        return m;
    }
    public EncResult PSD1(EncResult c, BigInteger SK1, BigInteger n){
        BigInteger n2 = n.multiply(n);
        BigInteger c0 = c.c0;
        BigInteger ct1 = c0.modPow(SK1, n2);
        c.ct1 = ct1;
        return c;

    }
    public BigInteger PSD2(EncResult c, BigInteger SK2, BigInteger n){
        BigInteger n2 = n.multiply(n);
        BigInteger c0 = c.c0;
        BigInteger ct1 = c.ct1;
        BigInteger ct2 = c0.modPow(SK2, n2);
        BigInteger m = Function_l(ct1.multiply(ct2).mod(n2),n);
        return m;
    }

    /**
     * PSDO与PSDT是有联系的，因为通过调用PSDO会更新c中的ct1的值
     * @param c
     * @param SK1
     * @param n
     * @return
     */
    public EncResult PSDO(EncResult c, BigInteger SK1, BigInteger n){
        BigInteger n2 = n.multiply(n);
        BigInteger c0 = c.c0;
        BigInteger ct1 = c0.modPow(SK1, n2);
        c.ct1 = ct1;
        return c;
    }
    public BigInteger PSDT(EncResult c, BigInteger SK2, BigInteger n){
        BigInteger n2 = n.multiply(n);
        BigInteger c0 = c.c0;
        BigInteger ct1 =c.ct1;
        BigInteger ct2 = c0.modPow(SK2, n2);
        BigInteger T = ct1.multiply(ct2).mod(n2);
        BigInteger m = Function_l(T.modInverse(n2).mod(n2),n);
        return m;
    }


}
