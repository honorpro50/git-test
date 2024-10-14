package algorithm;
import Result.RSSResult;
import Result.SK_RSS_Result;
import Result.SK_Splitting;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

public class StaticDtPkc {

    public static BigInteger lcm(BigInteger a, BigInteger b) {
        return a.multiply(b).divide(a.gcd(b));
    }
//    public BigInteger Function_l(BigInteger x) {
//        return x.subtract(BigInteger.ONE).divide(n);
//    }
    static boolean isPrime(BigInteger x, int certainty){
        return x.isProbablePrime(certainty);
    }

//    static ArrayList<BigInteger> list = new ArrayList<>();
    public static Map<String,BigInteger> list = new HashMap<>();
    //静态代码,KMC生成所有的公私参数
    static {
        //密钥生成
        //p,q,g,n,lambda...
        int certainty = 10;
        boolean flag = false;
        BigInteger lambda,tha_u,tha_o,tha;
        BigInteger lambda1,lambda2;
        BigInteger n,n2,a,g,h_u,h_o,h;
        BigInteger p1;
        BigInteger q1;
//        BigInteger p = new BigInteger("236357755517910594500460880009049484227");//128位
//        BigInteger q = new BigInteger("300374856797195919319535147934810462599");
//        BigInteger p = new BigInteger("21849961644035895719");
//        BigInteger q = new BigInteger("25395196308809357519");
        BigInteger p = new BigInteger("16886210425221089603862418878155320505052876594725354455386739185348698720577326190485762469999167057443456312805064973277938365762445546753667510844030803");
        BigInteger q = new BigInteger("19276435177817885098699817953639045451877289974395155763914124162774541309748874093621848062048228941997045123451055943385765026196140327952833730273206263");
        do {
            flag = true;
//            p = new BigInteger("9862284938155742366871436857320438905671075791583179515326641270549325340833639489483718330036832659958951727481875323823101307170532092338837153187628443");
//            q = new BigInteger("7986901984020290211411487668763291338428895773814064308090191603940550262206907214650071489911031996382654873978355925810410599385919586702672115698022983");
            p1 = p.subtract(BigInteger.ONE).divide(new BigInteger("2"));
            q1 = q.subtract(BigInteger.ONE).divide(new BigInteger("2"));
            n = p.multiply(q);
            //nSubOne = n.subtract(BigInteger.ONE);
            lambda = lcm(p.subtract(BigInteger.ONE), q.subtract(BigInteger.ONE));
            if (!isPrime(p1, certainty) || !isPrime(q1, certainty)) {
                flag = false;
            }
        } while ((!lambda.gcd(n).equals(BigInteger.ONE)) || !flag);
        //validate p, q, p1, q1 is primes
        if (!isPrime(p1, certainty)) {
            System.out.println("p1 is not prime");
        }
        if (!isPrime(q1, certainty)) {
            System.out.println("q1 is not prime");
        }
        list.put("p",p);
        list.put("q",q);
        list.put("n",n);
        //compute N^2, p1, q1
        n2 = n.multiply(n);
        list.put("n2",n2);
        BigInteger lambdaInverse = lambda.modInverse(n);
        //generate g
        a = generateRandomNumber(n2);  //为了计算生成元g=a^n mod n^2或者g=a^(2n) mod n^2
//        a = new BigInteger("43740291838977966358412011346027645009024807429724953221257839631540716696605");
        list.put("a",a);
        g = a.modPow(BigInteger.valueOf(2).multiply(n), n2);
        list.put("g",g);
        tha_u = getRandoma(n);  //弱私钥theta_i \in [1,n/4]
        tha_o = getRandoma(n);  //弱私钥theta_o \in [1,n/4]
        tha = getRandoma(n);  //弱私钥theta \in [1,n/4]
//        tha = new BigInteger("61859245414837337432551845531881824149955236468526305350679797549893413478745");
        list.put("sk_u",tha_u);
        h_u = g.modPow(tha_u, n2);    //查询用户的公钥h=g^theta mod n^2
        h_o = g.modPow(tha_o, n2);    //数据拥有者的公钥h=g^theta mod n^2
        h = g.modPow(tha, n2);    //临时公钥h=g^theta mod n^2
        list.put("pk_u",h_u);
        list.put("pk_o",h_o);
        list.put("pk",h);
        list.put("SK",lambda);
        BigInteger s = lambda.multiply(lambda.modInverse(n.multiply(n))).mod(lambda.multiply(n).multiply(n));
        lambda1 = new BigInteger(lambda.bitLength(), new SecureRandom());
//        lambda1 = new BigInteger("7213717513743931863");
        lambda2 = s.subtract(lambda1);
        list.put("SK1",lambda1);
        list.put("SK2",lambda2);
        SK_RSS(lambda,lambda1,lambda2);

    }

    private static BigInteger generateRandomNumber(BigInteger N2) {
        // 使用 SecureRandom 生成随机数
        SecureRandom secureRandom = new SecureRandom();

        // 生成随机数
        BigInteger randomValue;
        do {
            // 随机生成比特长度为 N2.bitLength() 的整数
            randomValue = new BigInteger(N2.bitLength(), secureRandom);
        } while (randomValue.compareTo(N2) >= 0 || !randomValue.gcd(N2).equals(BigInteger.ONE));
        // 重复直到生成小于 N2 且与 N2 互质的数

        return randomValue;
    }
    /**
     * 在[1,N/4]中选取随机数
     * @param b
     * @return
     */
    public static BigInteger getRandoma(BigInteger N) {
        BigInteger a;
        Random random = new SecureRandom();
//        a = new BigInteger(b.divide(BigInteger.valueOf(4)).bitLength(), random);
        do {
            a = new BigInteger(N.divide(BigInteger.valueOf(4)).bitLength(), random);
        } while (a.compareTo(BigInteger.ONE) < 0);
        // 确保生成的随机数在[1, n/4]范围内
        return a;
    }
    static void SK_RSS(BigInteger lambda, BigInteger lambda1, BigInteger lambda2){
        //StaticDtPkc object = new StaticDtPkc();
        //BigInteger alpha = StaticDtPkc.lcm(object.p.subtract(BigInteger.ONE), q.subtract(BigInteger.ONE));
        //public int alpha = 128;
        RSS Rss = new RSS();
        BigInteger pho11  =new BigInteger(lambda.bitLength(), new SecureRandom());
        BigInteger pho12  =new BigInteger(lambda.bitLength(), new SecureRandom());
        BigInteger pho13  =new BigInteger(lambda.bitLength(), new SecureRandom());
        RSSResult A = Rss.RSS_numa(pho11, pho12, pho13);
        BigInteger pho21  =new BigInteger(lambda.bitLength(), new SecureRandom());
        BigInteger pho22  =new BigInteger(lambda.bitLength(), new SecureRandom());
        BigInteger pho23  =new BigInteger(lambda.bitLength(), new SecureRandom());
        RSSResult B = Rss.RSS_numb(pho21, pho22, pho23);
        BigInteger x1 = A.r3.subtract(lambda1);
        BigInteger x2 = A.r1.subtract(lambda1);
        BigInteger x3 = A.r2.subtract(lambda1);
        BigInteger y1 = B.r3.subtract(lambda2);
        BigInteger y2 = B.r1.subtract(lambda2);
        BigInteger y3 = B.r2.subtract(lambda2);
        BigInteger alpha1 = x1.add(y1); BigInteger beta1 = A.r1.add(B.r1);
        BigInteger alpha2 = x2.add(y2); BigInteger beta2 = A.r2.add(B.r2);
        BigInteger alpha3 = x3.add(y3); BigInteger beta3 = A.r3.add(B.r3);
//        SK_RSS_Result sk_rss_result = new SK_RSS_Result(alpha1.add(beta1),beta1,alpha2.add(beta2),beta2,alpha3.add(beta3),beta3);
        list.put("SK11",alpha1.add(beta1));list.put("SK12",beta1);
        list.put("SK21",alpha2.add(beta2));list.put("SK22",beta2);
        list.put("SK31",alpha3.add(beta3));list.put("SK32",beta3);
    }
    public StaticDtPkc(){
        //密钥生成
    }

    public void sent_key(){

        //密钥发放，需要6个实体，QU,DO,CSP,CP1,CP2,CP3
        Map<String,BigInteger> key_u = new HashMap<>();
        Map<String,BigInteger> key_o = new HashMap<>();
        Map<String,BigInteger> key_csp = new HashMap<>();
        Map<String,BigInteger> key_cp1 = new HashMap<>();
        Map<String,BigInteger> key_cp2 = new HashMap<>();
        Map<String,BigInteger> key_cp3 = new HashMap<>();
        key_u.put("sk_u",list.get("sk_u"));
        key_u.put("pk_u",list.get("pk_u"));
        key_u.put("n",list.get("n"));
        key_u.put("n2",list.get("n2"));
        key_u.put("g",list.get("g"));
        key_o.put("pk_o",list.get("pk_o"));
        key_o.put("n",list.get("n"));
        key_o.put("n2",list.get("n2"));
        key_o.put("g",list.get("g"));
        key_csp.put("pk_o",list.get("pk_o"));
        key_csp.put("pk_u",list.get("pk_u"));
        key_csp.put("n",list.get("n"));
        key_csp.put("n2",list.get("n2"));
        key_csp.put("g",list.get("g"));
        look("QU",key_u);
        look("DO",key_o);
        look("CSP",key_csp);
    }
    public void look(String name, Map<String,BigInteger> list){
        System.out.println(name+": ");
        System.out.print(list);
        System.out.println();
    }
}
