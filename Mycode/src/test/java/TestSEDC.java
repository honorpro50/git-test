import Result.EncResult;
import algorithm.Decrypt;
import algorithm.Encrypt;
import algorithm.SimilarityCalculation;
import algorithm.StaticDtPkc;

import java.math.BigInteger;
import java.util.Vector;

import static algorithm.StaticDtPkc.list;

public class TestSEDC {
    public static void main(String[] args) {
        new StaticDtPkc();
        Encrypt encrypt = new Encrypt();
        Decrypt decrypt = new Decrypt();
        SimilarityCalculation similarityCalculation = new SimilarityCalculation();
        Vector<BigInteger> vector1 = new Vector<>();
        Vector<BigInteger> vector2 = new Vector<>();
        Vector<EncResult> x = new Vector<>();
        Vector<EncResult> y = new Vector<>();
        vector1.add(BigInteger.valueOf(3));
        vector1.add(BigInteger.valueOf(5));
        for (BigInteger element : vector1) {
            x.add(encrypt.Encrypt(element,list.get("g"),list.get("pk"),list.get("n")));
        }
        vector2.add(BigInteger.valueOf(5));
        vector2.add(BigInteger.valueOf(10));
        for (BigInteger element : vector2) {
            y.add(encrypt.Encrypt(element,list.get("g"),list.get("pk"),list.get("n")));
        }
        EncResult S = similarityCalculation.SEDC(x,y,list.get("pk_o"),list.get("pk_u"),list.get("pk"),list.get("n"),list.get("g"),list.get("SK11"),list.get("SK22"));
        BigInteger res = decrypt.SKDecrypt(S,list.get("SK"),list.get("n"));
        System.out.println("vector1 and vector2 ED^2" + "=" + res);

//        EncResultVector1 encResultVector1 = new EncResultVector1();
    }
}
