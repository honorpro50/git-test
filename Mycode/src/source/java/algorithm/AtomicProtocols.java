package algorithm;
import Result.EncResult;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import static algorithm.StaticDtPkc.list;

public class AtomicProtocols {

    /**
     * 安全加法协议
     * @param a
     * @param b
     * @param n
     * @param h_a
     * @param h_b
     * @param h
     * @param g
     * @param SK1
     * @param SK2
     * @return
     */
    public EncResult SAD(EncResult a, EncResult b, BigInteger n, BigInteger h_a, BigInteger h_b, BigInteger h, BigInteger g, BigInteger SK1, BigInteger SK2){
        BigInteger n2 = n.multiply(n);
        BigInteger nSubOne = n.subtract(BigInteger.ONE);
        //CSP do something:
        long CSPtime1 = System.currentTimeMillis();
        BigInteger ra = getRandoma(n);
        BigInteger rb = getRandoma(n);
//        BigInteger k = getRandoma(n);
        Encrypt encrypt = new Encrypt();
        EncResult result_ra = encrypt.Encrypt(ra,g,h_a,n);
        EncResult result_rb = encrypt.Encrypt(rb,g,h_b,n);
        EncResult X = encrypt.ADD(a,result_ra,n);
        EncResult Y = encrypt.ADD(b,result_rb,n);
        long CSPtime2 = System.currentTimeMillis();
        long CSPtime3 = CSPtime2 - CSPtime1;
        long CPtime1 = System.currentTimeMillis();
        //CP1 do something:
        Decrypt decrypt = new Decrypt();
        EncResult X1 = decrypt.PSDO(X,SK1,n);
        EncResult Y1 = decrypt.PSDO(Y,SK1,n);
        //CP2 do something:
        BigInteger X2 = decrypt.PSDT(X,SK2,n);
        BigInteger Y2 = decrypt.PSDT(Y,SK2,n);
        BigInteger S = X2.add(Y2);
        long CPtime2 = System.currentTimeMillis();
        long CPtime = CPtime2 - CPtime1;
        System.out.println(CPtime);
        long CSPtime4 = System.currentTimeMillis();
        EncResult R = encrypt.Encrypt(ra.add(rb),g,h,n);
        EncResult reS = encrypt.Encrypt(S,g,h,n);
        reS.c0 = reS.c0.multiply(R.c0.modPow(nSubOne,n2));
        reS.c1 = reS.c1.multiply(R.c1.modPow(nSubOne,n2));
        long CSPtime5 = System.currentTimeMillis();
        long CSPtime6 = CSPtime5 - CSPtime4;
        long CSPtime = CSPtime6 + CSPtime3;
        System.out.println(CSPtime);
        return reS;
    }

    /**
     * 安全乘法协议
     * @param a
     * @param b
     * @param n
     * @param h_a
     * @param h_b
     * @param h
     * @param g
     * @param SK1
     * @param SK2
     * @return
     */
    public EncResult SM(EncResult a, EncResult b, BigInteger n, BigInteger h_a, BigInteger h_b, BigInteger h, BigInteger g, BigInteger SK1, BigInteger SK2){
        BigInteger n2 = n.multiply(n);
        //CSP do something:
        BigInteger ra = getRandoma(n);
        BigInteger rb = getRandoma(n);
//        BigInteger ra = BigInteger.valueOf(100);
//        BigInteger rb = BigInteger.valueOf(100);
        BigInteger rab = ra.multiply(rb);  //rab=ra*rb
        BigInteger Ra = getRandoma(n);
        BigInteger Rb = getRandoma(n);
//        BigInteger Ra = BigInteger.valueOf(100);
//        BigInteger Rb = BigInteger.valueOf(100);
        BigInteger n_Sub_ra = n.subtract(ra); //n-ra
        BigInteger n_Sub_rb = n.subtract(rb); //n-rb
        BigInteger nSubOne = n.subtract(BigInteger.ONE);  //n-1
        Encrypt encrypt = new Encrypt();
        EncResult result_ra = encrypt.Encrypt(ra,g,h_a,n);  //[ra]_{pk_a}
        EncResult result_rb = encrypt.Encrypt(rb,g,h_b,n);  //[rb]_{pk_b}
        EncResult result_Ra = encrypt.Encrypt(Ra,g,h_a,n);  //[Ra]_{pk_a}
        EncResult result_Rb = encrypt.Encrypt(Rb,g,h_b,n);  //[Rb]_{pk_b}
        EncResult X = encrypt.ADD(a,result_ra,n);    //[a+ra]_{pk_a}
        EncResult Y = encrypt.ADD(b,result_rb,n);    //[b+rb]_{pk_b}
        //S=[Ra-a*rb]_{pk_a};  T=[Rb-b*ra]_{pk_b}
        EncResult S = encrypt.Mod_R(a,n_Sub_rb, n2);
        EncResult T = encrypt.Mod_R(b,n_Sub_ra, n2);
        S.c0 = result_Ra.c0.multiply(S.c0); S.c1 = result_Ra.c1.multiply(S.c1);
        T.c0 = result_Rb.c0.multiply(T.c0); T.c1 = result_Rb.c1.multiply(T.c1);
        //CP1 do something:
        Decrypt decrypt = new Decrypt();
        EncResult X1 = decrypt.PSDO(X,SK1,n);
        EncResult Y1 = decrypt.PSDO(Y,SK1,n);
        //CP2 do something:
        BigInteger X2 = decrypt.PSDT(X,SK2,n);
        BigInteger Y2 = decrypt.PSDT(Y,SK2,n);
        BigInteger H = X2.multiply(Y2);
        EncResult reH = encrypt.Encrypt(H,g,h,n);
        EncResult re_rab = encrypt.Encrypt(rab,g,h,n);
        EncResult re_Ra = encrypt.Encrypt(Ra,g,h,n);
        EncResult re_Rb = encrypt.Encrypt(Rb,g,h,n);

//        BigInteger s4 = re_rab.c0.modPow(nSubOne,n2); BigInteger t4 = re_rab.c1.modPow(nSubOne,n2);
//        BigInteger s5 = re_Ra.c0.modPow(nSubOne,n2);  BigInteger t5 = re_Ra.c0.modPow(nSubOne,n2);
//        BigInteger s6 = re_Rb.c0.modPow(nSubOne,n2);  BigInteger t6 = re_Rb.c0.modPow(nSubOne,n2);
        EncResult S4 = encrypt.Mod_R(re_rab,nSubOne,n2);
        EncResult S5 = encrypt.Mod_R(re_Ra,nSubOne,n2);
        EncResult S6 = encrypt.Mod_R(re_Rb,nSubOne,n2);
        EncResult res = new EncResult();
        res.c0 = reH.c0.multiply(T.c0).multiply(S.c0).multiply(S4.c0).multiply(S5.c0).multiply(S6.c0);
        res.c1 = reH.c0.multiply(T.c1).multiply(S.c1).multiply(S4.c1).multiply(S5.c1).multiply(S6.c1);

        return res;

    }

    /**
     * 安全除法协议，输出y/x
     * @param a=[x]
     * @param b=[y]
     * @param n
     * @param h
     * @param g
     * @param SK1
     * @param SK2
     * @return
     */
    public EncResult SDIV1(EncResult a, EncResult b, BigInteger n, BigInteger h, BigInteger g, BigInteger SK1, BigInteger SK2){
        BigInteger n2 = n.multiply(n);
        BigInteger nSubOne = n.subtract(BigInteger.ONE);
        Encrypt encrypt = new Encrypt();
        Decrypt decrypt = new Decrypt();
        //CSP do something:
        BigInteger ra = getRandoma(n);
        BigInteger rb = getRandoma(n);
        EncResult res_rb = encrypt.Encrypt(rb,g,h,n);
        BigInteger s1 = encrypt.Mod_R(b,ra,n2).c0.multiply(encrypt.Mod_R(a,ra.multiply(rb),n2).c0).mod(n2);
        BigInteger s2 = encrypt.Mod_R(b,ra,n2).c1.multiply(encrypt.Mod_R(a,ra.multiply(rb),n2).c1).mod(n2);
        BigInteger t1 = encrypt.Mod_R(a,ra,n2).c0;
        BigInteger t2 = encrypt.Mod_R(a,ra,n2).c1;
        EncResult Y = new EncResult(s1,s2);
        EncResult X = new EncResult(t1,t2);
        //CP1 do something:
        EncResult X1 = decrypt.PSDO(X,SK1,n);
        EncResult Y1 = decrypt.PSDO(Y,SK1,n);
        //CP2 do something:
        BigInteger X2 = decrypt.PSDT(X,SK2,n);
        BigInteger Y2 = decrypt.PSDT(Y,SK2,n);
        BigInteger H = Y2.divide(X2);
        EncResult reH = encrypt.Encrypt(H,g,h,n);
        //CSP do something:
        EncResult res = new EncResult();
        res.c0 = reH.c0.multiply(encrypt.Mod_R(res_rb,nSubOne,n2).c0);
        res.c1 = reH.c1.multiply(encrypt.Mod_R(res_rb,nSubOne,n2).c1);
        return res;
    }

    /**
     * 注意，这里的除法保留了3位小数之后又乘以了1000
     * @param a
     * @param b
     * @param n
     * @param h
     * @param g
     * @param SK1
     * @param SK2
     * @return
     */
    public EncResult SDIV(EncResult a, EncResult b, BigInteger n, BigInteger h, BigInteger g, BigInteger SK1, BigInteger SK2){
        BigInteger n2 = n.multiply(n);
        BigInteger nSubOne = n.subtract(BigInteger.ONE);
        Encrypt encrypt = new Encrypt();
        Decrypt decrypt = new Decrypt();
        //CP1 do something:
        EncResult X1 = decrypt.PSDO(a,SK1,n);
        EncResult Y1 = decrypt.PSDO(b,SK1,n);
        //CP2 do something:
        BigInteger X2 = decrypt.PSDT(a,SK2,n);
        BigInteger Y2 = decrypt.PSDT(b,SK2,n);
        BigDecimal res1 = new BigDecimal(Y2).divide(new BigDecimal(X2), 3, RoundingMode.HALF_UP);
        BigInteger H = res1.multiply(new BigDecimal("1000")).toBigInteger();
        EncResult res = encrypt.Encrypt(H,g,h,n);
        return res;
    }
    public static BigInteger getRandoma(BigInteger N) {
        Random random = new SecureRandom();
        BigInteger a;
        do {
            // 随机生成比特长度为 N.bitLength() 的整数
            a = new BigInteger(N.bitLength(), random);
        } while (a.compareTo(N) >= 0); // 重复直到生成小于 N 的数

        return a;
    }
}
