package algorithm;
import Result.EncResult;
import Result.EncResultPair;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static algorithm.StaticDtPkc.list;

public class SecureProtocols {

    /**
     * SLT安全小于协议，如果m1<m2输出1；否则输出0
     * @param x
     * @param y
     * @param h_x
     * @param h_y
     * @param h
     * @param g
     * @param n
     * @param SK1
     * @param SK2
     * @return
     */
    public EncResult SLT1(EncResult x, EncResult y, BigInteger h_x, BigInteger h_y, BigInteger h, BigInteger g, BigInteger n, BigInteger SK1, BigInteger SK2) {
        BigInteger n2 = n.multiply(n);
        BigInteger nSubOne = n.subtract(BigInteger.ONE);
        AtomicProtocols atomicProtocols = new AtomicProtocols();
        Encrypt encrypt = new Encrypt();
        Decrypt decrypt = new Decrypt();
        //CSP do something:
        y.c0 = y.c0.modPow(nSubOne, n2);
        y.c1 = y.c1.modPow(nSubOne, n2);
        EncResult L = atomicProtocols.SAD(x, y, n, h_x, h_y, h, g, SK1, SK2);
//        System.out.println(decrypt.SKDecrypt(L,list.get("SK"),list.get("n2"),list.get("n")));
        int result = flipCoin();
        BigInteger s = BigInteger.valueOf(result);  //flp a coin s \in {-1,1}.
        BigInteger r1,r2;
        do {
            r1 = getRandoma(n);
            r2 = getRandoma(n);
        } while (r1.compareTo(r2)<=0);//select r1>r2>0
        EncResult res_r2 = encrypt.Encrypt(s.multiply(r2), g, h, n);
        EncResult S = encrypt.Mod_R(L, s.multiply(r1), n2); //[x-y]^{s*r1}
//        EncResult X = atomicProtocols.SM(S,res_r2,n,h_x,h_y,h,g,SK1,SK2);
        EncResult X = new EncResult();
        X.c0 = S.c0.multiply(res_r2.c0).mod(n2);
        X.c1 = S.c1.multiply(res_r2.c1).mod(n2);
//        CP1 do something:
        EncResult X1 = decrypt.PSDO(X, SK1, n);
//        CP2 do something:
        BigInteger X2 = decrypt.PSDT(X, SK2, n);
        //Since encryption systems are not very well implemented for handling negative numbers
        EncResult delta = new EncResult();
        if (s.equals(BigInteger.ONE)) {
            if (X2.compareTo(BigInteger.ZERO) >= 0) {
                int a = 0;
                BigInteger theta = BigInteger.valueOf(a);
                EncResult T = encrypt.Encrypt(theta, g, h, n);
//                EncResult H = encrypt.Encrypt(BigInteger.valueOf(1).subtract(theta),g,h,n,n2);
                delta = T;
            } else {
                int a = 1;
                BigInteger theta = BigInteger.valueOf(a);
                EncResult T = encrypt.Encrypt(theta, g, h, n);
//                EncResult H = encrypt.Encrypt(BigInteger.valueOf(1).subtract(theta),g,h,n,n2);
                delta = T;
            }
        } else {
            if (X2.compareTo(BigInteger.ZERO) >= 0) {
                int a = 0;
                BigInteger theta = BigInteger.valueOf(a);
//                EncResult T = encrypt.Encrypt(theta, g, h, n);
                EncResult H = encrypt.Encrypt(BigInteger.valueOf(1).subtract(theta), g, h, n);
                delta = H;
            } else {
                int a = 1;
                BigInteger theta = BigInteger.valueOf(a);
//                EncResult T = encrypt.Encrypt(theta,g,h,n,n2);
                EncResult H = encrypt.Encrypt(BigInteger.valueOf(1).subtract(theta), g, h, n);
                delta = H;
            }
        }
        return delta;
    }
    public EncResult SLT(EncResult x, EncResult y, BigInteger h, BigInteger g, BigInteger n, BigInteger SK1, BigInteger SK2) {
        Encrypt encrypt = new Encrypt();
        Decrypt decrypt = new Decrypt();
//        CP1 do something:
        EncResult X1 = decrypt.PSDO(x, SK1, n);
        EncResult Y1 = decrypt.PSDO(y, SK1, n);
//        CP2 do something:
        BigInteger X2 = decrypt.PSDT(x, SK2, n);
        BigInteger Y2 = decrypt.PSDT(y, SK2, n);
        EncResult delta = new EncResult();
        if (X2.compareTo(Y2) < 0){
            delta = encrypt.Encrypt(BigInteger.valueOf(1),g,h,n);
        } else {
            delta = encrypt.Encrypt(BigInteger.valueOf(0),g,h,n);
        }
        return delta;
    }
    public EncResultPair SMMS(EncResult x, EncResult y, BigInteger h, BigInteger g, BigInteger n, BigInteger SK1, BigInteger SK2){
        BigInteger n2 = n.multiply(n);
        BigInteger nSubOne = n.subtract(BigInteger.ONE);
        AtomicProtocols atomicProtocols = new AtomicProtocols();
        Encrypt encrypt = new Encrypt();
//        Decrypt decrypt = new Decrypt();
        //CSP do something:
        EncResult S = encrypt.Mod_R(y,nSubOne,n2);
        EncResult X = encrypt.ADD(x,S,n); //[m1-m2]

        SecureProtocols secureProtocols = new SecureProtocols();
        EncResult delta = secureProtocols.SLT(x,y,h,g,n,SK1,SK2);//if m1<m2,delta=1;
//        System.out.println(decrypt.SKDecrypt(delta,list.get("SK"),n));
        EncResult Y = atomicProtocols.SM(delta,X,n,h,h,h,g,SK1,SK2); //[delta*(m1-m2)];
        EncResult T = encrypt.Mod_R(Y,nSubOne,n2); //[- delta*(m1-m2)]
        EncResult max = encrypt.ADD(x,T,n);  //[m1 - delta*(m1-m2)]
        EncResult min = encrypt.ADD(y,Y,n);  //[m2 + delta*(m1-m2)]
        EncResultPair res = new EncResultPair(max, min);
        return res;
    }
    public EncResultPair SMMSn(List<EncResult> resultList, BigInteger h, BigInteger g, BigInteger n, BigInteger SK1, BigInteger SK2){
        SecureProtocols secureProtocols = new SecureProtocols();
//        List<EncResult> resultList = storage.getEncResults();
        EncResultPair S = secureProtocols.SMMS(resultList.get(0),resultList.get(1),h,g,n,SK1,SK2);
        for (int i = 2; i < resultList.size(); i++) {
            EncResultPair H = secureProtocols.SMMS(S.result1,resultList.get(i),h,g,n,SK1,SK2);
            EncResultPair T = secureProtocols.SMMS(H.result2,S.result2,h,g,n,SK1,SK2);
            S.result1 = H.result1;
            S.result2 = T.result2;
        }
        return S;
    }
    public static BigInteger getRandoma(BigInteger N) {
        Random random = new SecureRandom();
        BigInteger a;
        do {
            // 随机生成比特长度为 N.bitLength() 的整数
            a = new BigInteger(N.bitLength(), random);
        } while (a.compareTo(N) > 0); // 重复直到生成小于 N 的数
        return a;
    }
    private static int flipCoin() {
        Random random = new Random();
        // 生成一个随机的布尔值，true表示正面，false表示反面
        boolean isHeads = random.nextBoolean();

        // 将布尔值映射为 -1 或 1
        return isHeads ? 1 : -1;
    }
    private static <T> T getRandomItem(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List must not be empty");
        }

        // 使用 Random 或 SecureRandom
//        Random random = new Random();
        // 如果需要更高的安全性，可以使用 SecureRandom
         SecureRandom random = new SecureRandom();

        int randomIndex = random.nextInt(list.size());
        return list.get(randomIndex);
    }
}
