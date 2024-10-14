import Result.EncResult;
import algorithm.AtomicProtocols;
import algorithm.Decrypt;
import algorithm.Encrypt;
import algorithm.StaticDtPkc;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

import static algorithm.StaticDtPkc.list;

public class TestDiv {
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
        EncResult x = encrypt.Encrypt(bigInteger1,list.get("g"),list.get("pk"),list.get("n"));
        EncResult y = encrypt.Encrypt(bigInteger2,list.get("g"),list.get("pk"),list.get("n"));
        long endTime_enc = System.currentTimeMillis();
        long duration_enc = endTime_enc - startTime_enc;
        System.out.println("加密时间为：" + duration_enc + "ms");
        System.out.println("xc0=" + x.c0 + ", xc1=" + x.c1);
        System.out.println("yc0=" + y.c0 + ", yc1=" + y.c1);
        AtomicProtocols atomicProtocols = new AtomicProtocols();
        EncResult s2 = atomicProtocols.SDIV(x,y,list.get("n"),list.get("pk"),list.get("g"),list.get("SK11"),list.get("SK22"));
//        System.out.println("[m1*m2]= (" + s2.c0 + "," + s2.c1 +")");
//        EncResult s2 = encrypt.Mod_R(x,bigInteger2,list.get("n2"));
        Decrypt decrypt =new Decrypt();
        BigInteger X1 = decrypt.SKDecrypt(s2,list.get("SK"),list.get("n"));
        System.out.println("m2/m1=" + X1);
    }
}
