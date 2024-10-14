import Result.EncResult;
import algorithm.AtomicProtocols;
import algorithm.Decrypt;
import algorithm.Encrypt;
import algorithm.StaticDtPkc;

import java.math.BigInteger;
import java.util.Scanner;

import static algorithm.StaticDtPkc.list;

public class TestSAD {
    public static void main(String[] args) {
        new StaticDtPkc();
        BigInteger nSubOne = list.get("n").subtract(BigInteger.ONE);
        System.out.println("请输入第一个明文m1:");
        Scanner input1 = new Scanner(System.in);
        String str1 = input1.nextLine();
        System.out.println("请输入第二个明文m2:");
        Scanner input2 = new Scanner(System.in);
        String str2 = input2.nextLine();
        BigInteger bigInteger1 = new BigInteger(str1);
        BigInteger bigInteger2 = new BigInteger(str2);
        //加密过程
        long startTime_enc = System.currentTimeMillis();
        Encrypt encrypt = new Encrypt();
        EncResult x = encrypt.Encrypt(bigInteger1,list.get("g"),list.get("pk_o"),list.get("n"));
        EncResult y = encrypt.Encrypt(bigInteger2,list.get("g"),list.get("pk_u"),list.get("n"));
        long endTime_enc = System.currentTimeMillis();
        long duration_enc = endTime_enc - startTime_enc;
        System.out.println("加密时间为：" + duration_enc + "ms");
        System.out.println("xc0=" + x.c0 + ", xc1=" + x.c1);
        System.out.println("yc0=" + y.c0 + ", yc1=" + y.c1);
        EncResult X = encrypt.ADD(x,y,list.get("n2"));
        AtomicProtocols atomicProtocols = new AtomicProtocols();
        EncResult s1 = atomicProtocols.SAD(x,y,list.get("n"),list.get("pk_o"),list.get("pk_u"),list.get("pk"),list.get("g"),list.get("SK11"),list.get("SK22"));
        System.out.println("[m1+m2]= (" + s1.c0 + "," + s1.c1 +")");
        Decrypt decrypt =new Decrypt();
        BigInteger X1 = decrypt.SKDecrypt(X,list.get("SK"),list.get("n"));
        BigInteger Y1 = decrypt.SKDecrypt(s1,list.get("SK"),list.get("n"));
        System.out.println("m1+m2=" + X1);
        System.out.println("m1+m2=" + Y1);
//        //强私钥解密
//        System.out.println("======================");
//        long startTime_SKdec = System.currentTimeMillis();
//        BigInteger SKdecResult1 = decrypt.SKDecrypt(s1,list.get("SK"),list.get("n2"),list.get("n"));
//        BigInteger SKdecResult2 = decrypt.SKDecrypt(s2,list.get("SK"),list.get("n2"),list.get("n"));
//        long endTime_SKdec = System.currentTimeMillis();
//        long duration_SKdec = endTime_SKdec - startTime_SKdec;
//        System.out.println("SK解密时间为：" + duration_SKdec + "ms");
//        System.out.println(" 强私钥是： " + list.get("SK") + "； 解密的结果是： " + SKdecResult1);
//        System.out.println(" 强私钥是： " + list.get("SK") + "； 解密的结果是： " + SKdecResult2);

    }
}
