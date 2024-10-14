package algorithm;
import Result.EncResult;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Vector;

import static algorithm.StaticDtPkc.list;

public class SimilarityCalculation {
    public EncResult SEDC(Vector<EncResult> x, Vector<EncResult> y, BigInteger pk_x, BigInteger pk_y,BigInteger pk, BigInteger n, BigInteger g, BigInteger SK1, BigInteger SK2){
        BigInteger n2 = n.multiply(n);
        Encrypt encrypt = new Encrypt();
        Vector<EncResult> res = new Vector<>();
        for (int i = 0; i < x.size(); i++) {
            //CSP do something:
            BigInteger ra = getRandoma(n);
            BigInteger rb = getRandoma(n);
            BigInteger R = ra.subtract(rb);
            BigInteger R2 = R.pow(2);
            BigInteger nSub2R = n.subtract(BigInteger.valueOf(2).multiply(R));
            EncResult res_ra = encrypt.Encrypt(ra,g,pk_x,n);
            EncResult res_rb = encrypt.Encrypt(rb,g,pk_y,n);
            EncResult res_R2 = encrypt.Encrypt(R2,g,pk,n);
            EncResult X = encrypt.ADD(x.get(i),res_ra,n);
            EncResult Y = encrypt.ADD(y.get(i),res_rb,n);
            //CP1 do something:
            Decrypt decrypt = new Decrypt();
            EncResult X1 = decrypt.PSDO(X,SK1,n);
            EncResult Y1 = decrypt.PSDO(Y,SK1,n);
            //CP2 do something:
            BigInteger X2 = decrypt.PSDT(X,SK2,n);
            BigInteger Y2 = decrypt.PSDT(Y,SK2,n);
            BigInteger S1 = X2.subtract(Y2);
            BigInteger S2 = S1.pow(2);
            EncResult S = encrypt.Encrypt(S1,g,pk,n);
            EncResult T = encrypt.Encrypt(S2,g,pk,n);
            EncResult T1 = encrypt.Mod_R(S,nSub2R,n);
            EncResult Z = new EncResult();
            Z.c0 = T.c0.multiply(T1.c0).multiply(res_R2.c0).mod(n2);
            Z.c1 = T.c1.multiply(T1.c1).multiply(res_R2.c1).mod(n2);
            res.add(Z);
        }
        EncResult Z1 = res.get(0);
        for (int i = 1; i < res.size(); i++) {
            Z1 = encrypt.ADD(Z1,res.get(i),n);
        }
        return Z1;

    }
    public EncResult SDPC(Vector<EncResult> x, Vector<EncResult> y, BigInteger pk_x, BigInteger pk_y,BigInteger pk, BigInteger n, BigInteger g, BigInteger SK1, BigInteger SK2){
        BigInteger n2 = n.multiply(n);
        Encrypt encrypt = new Encrypt();
        AtomicProtocols atomicProtocols = new AtomicProtocols();
        Vector<EncResult> res = new Vector<>();
        for (int i = 0; i < x.size(); i++) {
            EncResult Z = atomicProtocols.SM(x.get(i),y.get(i),n,pk_x,pk_y,pk,g,SK1,SK2);
            res.add(Z);
        }
        EncResult Z1 = res.get(0);
        for (int i = 1; i < res.size(); i++) {
            Z1 = encrypt.ADD(Z1,res.get(i),n);
        }
        return Z1;
    }
    public  EncResult SJC(Vector<EncResult> x, Vector<EncResult> y, BigInteger pk_x, BigInteger pk_y,BigInteger pk, BigInteger n, BigInteger g, BigInteger SK1, BigInteger SK2){
        BigInteger n2 = n.multiply(n);
        BigInteger nSubOne = n.subtract(BigInteger.ONE);
        Encrypt encrypt = new Encrypt();
        SimilarityCalculation similarityCalculation = new SimilarityCalculation();
        AtomicProtocols atomicProtocols = new AtomicProtocols();
        EncResult X = similarityCalculation.SDPC(x,y,pk_x,pk_y,pk,n,g,SK1,SK2);
        EncResult Y = encrypt.Mod_R(X,nSubOne,n);
        EncResult S = similarityCalculation.SDPC(x,x,pk_x,pk_y,pk,n,g,SK1,SK2);
        EncResult T = similarityCalculation.SDPC(y,y,pk_x,pk_y,pk,n,g,SK1,SK2);
        EncResult H = new EncResult();
        H.c0 = S.c0.multiply(T.c0).multiply(Y.c0).mod(n2);
        H.c1 = S.c1.multiply(T.c1).multiply(Y.c1).mod(n2);
        EncResult Z = atomicProtocols.SDIV(H,X,n,pk,g,SK1,SK2);
        //最终的结果是乘以1000后的结果
        return Z;
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
