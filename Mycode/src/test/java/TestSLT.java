import Result.EncResult;
import algorithm.*;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;

import static algorithm.StaticDtPkc.list;

public class TestSLT {
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
        EncResult s1 = secureProtocols.SLT(x,y,list.get("pk"),list.get("g"),list.get("n"),list.get("SK11"),list.get("SK22"));
        System.out.println("[delta]= (" + s1.c0 + "," + s1.c1 +")");
        Decrypt decrypt =new Decrypt();
        BigInteger Y1 = decrypt.SKDecrypt(s1,list.get("SK"),list.get("n"));
        System.out.println("m1<m2?" + Y1);
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
    /**
     * 掷色子
     * @return
     */
    private static int flipCoin() {
        Random random = new Random();
        // 生成一个随机的布尔值，true表示正面，false表示反面
        boolean isHeads = random.nextBoolean();

        // 将布尔值映射为 -1 或 1
        return isHeads ? 1 : -1;
    }
}
