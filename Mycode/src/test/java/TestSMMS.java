import Result.EncResult;
import Result.EncResultPair;
import algorithm.Decrypt;
import algorithm.Encrypt;
import algorithm.SecureProtocols;
import algorithm.StaticDtPkc;
import java.math.BigInteger;
import java.util.Scanner;

import static algorithm.StaticDtPkc.list;

public class TestSMMS {
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
        SecureProtocols secureProtocols = new SecureProtocols();
        EncResultPair s1 = secureProtocols.SMMS(x,y,list.get("pk"),list.get("g"),list.get("n"),list.get("SK11"),list.get("SK22"));
        Decrypt decrypt =new Decrypt();
        BigInteger Y1 = decrypt.SKDecrypt(s1.result1,list.get("SK"),list.get("n"));
        BigInteger Y2 = decrypt.SKDecrypt(s1.result2,list.get("SK"),list.get("n"));
        System.out.println("max=" + Y1);
        System.out.println("min=" + Y2);
    }
}
