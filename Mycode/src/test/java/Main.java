import Result.EncResult;
import Result.EncResultPair;
import Result.EncResultStorage;
import algorithm.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static algorithm.StaticDtPkc.list;

public class Main {
    public static void main(String[] args) throws IOException {
        new StaticDtPkc();
        AtomicProtocols atomicProtocols = new AtomicProtocols();
        SecureProtocols secureProtocols = new SecureProtocols();
        SimilarityCalculation similarityCalculation = new SimilarityCalculation();
        Encrypt encrypt = new Encrypt();
        Decrypt decrypt = new Decrypt();
        EncResult alpha = encrypt.Encrypt(BigInteger.valueOf(6),list.get("g"),list.get("pk_o"),list.get("n"));
        EncResult beta = encrypt.Encrypt(BigInteger.valueOf(4),list.get("g"),list.get("pk_o"),list.get("n"));
        //给定查询请求Q
        Vector<BigInteger> vector1 = new Vector<>();
        Vector<EncResult> qx = new Vector<>();
        vector1.add(BigInteger.valueOf(14));
        vector1.add(BigInteger.valueOf(25));
        long start1 = System.currentTimeMillis();
        for (BigInteger element : vector1) {
            qx.add(encrypt.Encrypt(element, list.get("g"), list.get("pk_u"), list.get("n")));
        }
        long end1 = System.currentTimeMillis();
        long time1 = end1 - start1;
        Vector<BigInteger> vector2 = new Vector<>();
        Vector<EncResult> qy = new Vector<>();
        vector2.add(BigInteger.valueOf(7));
        vector2.add(BigInteger.valueOf(10));
        vector2.add(BigInteger.valueOf(8));
        vector2.add(BigInteger.valueOf(9));
        vector2.add(BigInteger.valueOf(5));
        vector2.add(BigInteger.valueOf(9));
        //ToKen加密时间
        long start2 = System.currentTimeMillis();
        for (BigInteger element : vector2) {
            qy.add(encrypt.Encrypt(element, list.get("g"), list.get("pk_u"), list.get("n")));
        }
        long end2 = System.currentTimeMillis();
        long time2 = end2 - start2;
//        long time = time2 + time1;
//        System.out.println("ToKen encrypt time:" + time);
        // 指定Excel文件路径
        String filePath = "D:\\Desktop\\Mycode\\data\\Hello.xlsx/";
//        String filePath = "D:\\Desktop\\Mycode\\data\\data.xlsx/";
        // 创建文件输入流
        FileInputStream fileInputStream = new FileInputStream(filePath);
        // 创建工作簿对象
        Workbook workbook = WorkbookFactory.create(fileInputStream);
        // 获取第一个工作表
        Sheet sheet1 = workbook.getSheetAt(0);
        Sheet sheet2 = workbook.getSheetAt(1);
        int rowCount1 = sheet1.getLastRowNum() + 1;
        int rowCount2 = sheet2.getLastRowNum() + 1;
        EncResult[][] matrix = new EncResult[rowCount1][rowCount2];
        List<EncResult> resList1 = new ArrayList<>();
        List<EncResult> resList2 = new ArrayList<>();
        long a1 = System.currentTimeMillis();
        long t = 0;
        for (Row row1 : sheet1) {
            Vector<EncResult> dataVector1 = new Vector<>();
            Vector<BigInteger> p = new Vector<>();
            double cell1 = row1.getCell(1).getNumericCellValue();
            double cell2 = row1.getCell(2).getNumericCellValue();
            BigInteger value1 = BigInteger.valueOf((long) cell1);
            BigInteger value2 = BigInteger.valueOf((long) cell2);
            p.add(value1);
            p.add(value2);
            for (BigInteger element : p) {
                dataVector1.add(encrypt.Encrypt(element, list.get("g"), list.get("pk_o"), list.get("n")));
            }
            Vector<EncResult> dataVector2 = new Vector<>();
            Vector<BigInteger> j = new Vector<>();
            double cell3 = row1.getCell(3).getNumericCellValue();
            double cell4 = row1.getCell(4).getNumericCellValue();
            double cell5 = row1.getCell(5).getNumericCellValue();
            double cell6 = row1.getCell(6).getNumericCellValue();
            double cell7 = row1.getCell(7).getNumericCellValue();
            double cell8 = row1.getCell(8).getNumericCellValue();
            BigInteger value3 = BigInteger.valueOf((long) cell3);
            BigInteger value4 = BigInteger.valueOf((long) cell4);
            BigInteger value5 = BigInteger.valueOf((long) cell5);
            BigInteger value6 = BigInteger.valueOf((long) cell6);
            BigInteger value7 = BigInteger.valueOf((long) cell7);
            BigInteger value8 = BigInteger.valueOf((long) cell8);
            j.add(value3);
            j.add(value4);
            j.add(value5);
            j.add(value6);
            j.add(value7);
            j.add(value8);
            for (BigInteger element : j) {
                dataVector2.add(encrypt.Encrypt(element, list.get("g"), list.get("pk_o"), list.get("n")));
            }
            long t1 = System.currentTimeMillis();
            EncResult res_dist = similarityCalculation.SEDC(dataVector1, qx, list.get("pk_o"), list.get("pk_u"), list.get("pk"), list.get("n"), list.get("g"), list.get("SK11"), list.get("SK22"));
            long t2 = System.currentTimeMillis();
            t = t + t2 - t1;
            EncResult res_jaccard = similarityCalculation.SJC(dataVector2, qy, list.get("pk_o"), list.get("pk_u"), list.get("pk"), list.get("n"), list.get("g"), list.get("SK11"), list.get("SK22"));
            resList1.add(res_dist);
            resList2.add(res_jaccard);
        }
        long b1 = System.currentTimeMillis();
        long c = b1 - a1;
        System.out.println("Data outsourcing:"+ c);
        System.out.println("SEDC time:"+ t);
        for (Row row2 : sheet2) {
            long startTime = System.currentTimeMillis();
            int rowNum = row2.getRowNum();
            Vector<BigInteger> fp = new Vector<>();
            double fcell1 = row2.getCell(1).getNumericCellValue();
            double fcell2 = row2.getCell(2).getNumericCellValue();
            BigInteger fvalue1 = BigInteger.valueOf((long) fcell1);
            BigInteger fvalue2 = BigInteger.valueOf((long) fcell2);
            fp.add(fvalue1); fp.add(fvalue2);
            Vector<EncResult> FVector1 = new Vector<>();
            for (BigInteger element : fp) {
                FVector1.add(encrypt.Encrypt(element,list.get("g"),list.get("pk_u"),list.get("n")));
            }
            Vector<BigInteger> fj = new Vector<>();
            double fcell3 = row2.getCell(3).getNumericCellValue();
            double fcell4 = row2.getCell(4).getNumericCellValue();
            double fcell5 = row2.getCell(5).getNumericCellValue();
            double fcell6 = row2.getCell(6).getNumericCellValue();
            double fcell7 = row2.getCell(7).getNumericCellValue();
            double fcell8 = row2.getCell(8).getNumericCellValue();
            BigInteger fvalue3 = BigInteger.valueOf((long) fcell3);
            BigInteger fvalue4 = BigInteger.valueOf((long) fcell4);
            BigInteger fvalue5 = BigInteger.valueOf((long) fcell5);
            BigInteger fvalue6 = BigInteger.valueOf((long) fcell6);
            BigInteger fvalue7 = BigInteger.valueOf((long) fcell7);
            BigInteger fvalue8 = BigInteger.valueOf((long) fcell8);
            fj.add(fvalue3); fj.add(fvalue4);
            fj.add(fvalue5);
            fj.add(fvalue6);
            fj.add(fvalue7);
            fj.add(fvalue8);
            Vector<EncResult> FVector2 = new Vector<>();
            for (BigInteger element : fj) {
                FVector2.add(encrypt.Encrypt(element,list.get("g"),list.get("pk_u"),list.get("n")));
            }
            long endTime = System.currentTimeMillis();
            long time = time2 + time1 + endTime - startTime;
            System.out.println("ToKen encrypt time:" + time);
            List<EncResult> resultList1 = new ArrayList<>();
            List<EncResult> resultList2 = new ArrayList<>();
            for (Row row1 : sheet1) {
                Vector<EncResult> dataVector1 = new Vector<>();
                Vector<BigInteger> p = new Vector<>();
                double cell1 = row1.getCell(1).getNumericCellValue();
                double cell2 = row1.getCell(2).getNumericCellValue();
                BigInteger value1 = BigInteger.valueOf((long) cell1);
                BigInteger value2 = BigInteger.valueOf((long) cell2);
                p.add(value1);
                p.add(value2);
                for (BigInteger element : p) {
                    dataVector1.add(encrypt.Encrypt(element, list.get("g"), list.get("pk_o"), list.get("n")));
                }
                Vector<EncResult> dataVector2 = new Vector<>();
                Vector<BigInteger> j = new Vector<>();
                double cell3 = row1.getCell(3).getNumericCellValue();
                double cell4 = row1.getCell(4).getNumericCellValue();
                double cell5 = row1.getCell(5).getNumericCellValue();
                double cell6 = row1.getCell(6).getNumericCellValue();
                double cell7 = row1.getCell(7).getNumericCellValue();
                double cell8 = row1.getCell(8).getNumericCellValue();
                BigInteger value3 = BigInteger.valueOf((long) cell3);
                BigInteger value4 = BigInteger.valueOf((long) cell4);
                BigInteger value5 = BigInteger.valueOf((long) cell5);
                BigInteger value6 = BigInteger.valueOf((long) cell6);
                BigInteger value7 = BigInteger.valueOf((long) cell7);
                BigInteger value8 = BigInteger.valueOf((long) cell8);
                j.add(value3);
                j.add(value4);
                j.add(value5);
                j.add(value6);
                j.add(value7);
                j.add(value8);
                for (BigInteger element : j) {
                    dataVector2.add(encrypt.Encrypt(element, list.get("g"), list.get("pk_o"), list.get("n")));
                }
                EncResult res_dist = similarityCalculation.SEDC(dataVector1, FVector1, list.get("pk_o"), list.get("pk_u"), list.get("pk"), list.get("n"), list.get("g"), list.get("SK11"), list.get("SK22"));
                EncResult res_jaccard = similarityCalculation.SJC(dataVector2, FVector2, list.get("pk_o"), list.get("pk_u"), list.get("pk"), list.get("n"), list.get("g"), list.get("SK11"), list.get("SK22"));
                resultList1.add(res_dist);
                resultList2.add(res_jaccard);
            }
            long startTime3 = System.currentTimeMillis();
//            long startTime2 = System.currentTimeMillis();
            EncResultPair S1 = secureProtocols.SMMSn(resultList1, list.get("pk"), list.get("g"), list.get("n"), list.get("SK1"), list.get("SK2"));
//            long endTime2 = System.currentTimeMillis();
//            long duration2 = endTime2 - startTime2;
//            System.out.println("SMMSn Time:"+duration2);
            EncResultPair S2 = secureProtocols.SMMSn(resultList2, list.get("pk"), list.get("g"), list.get("n"), list.get("SK1"), list.get("SK2"));
            BigInteger nSubOne = list.get("n").subtract(BigInteger.ONE);
            EncResult T1 = encrypt.Mod_R(S1.result1,nSubOne,list.get("n"));  //min^{N-1}
            EncResult T2 = encrypt.Mod_R(S2.result1,nSubOne,list.get("n"));
            //距离和相似度的最大值减最小值
            EncResult m_m_dist = encrypt.ADD(S1.result2,T1,list.get("n"));  //max-min
            EncResult m_m_jaccard = encrypt.ADD(S2.result2,T2,list.get("n"));
            EncResult a = atomicProtocols.SM(alpha, m_m_jaccard, list.get("n"), list.get("pk_o"), list.get("pk"), list.get("pk"), list.get("g"), list.get("SK1"), list.get("SK2"));
            EncResult b = atomicProtocols.SM(beta, m_m_dist, list.get("n"), list.get("pk_o"), list.get("pk"), list.get("pk"), list.get("g"), list.get("SK1"), list.get("SK2"));
//            List<EncResult> deltaList = new ArrayList<>();
            for (int i = 0; i < resList1.size(); i++) {
                EncResult A = encrypt.Mod_R(resultList1.get(i),nSubOne,list.get("n"));
                EncResult B = encrypt.Mod_R(resultList2.get(i),nSubOne,list.get("n"));
                EncResult A1 = atomicProtocols.SM(a,encrypt.ADD(resList1.get(i),A,list.get("n")),list.get("n"), list.get("pk"), list.get("pk"), list.get("pk"), list.get("g"), list.get("SK1"), list.get("SK2"));
                EncResult B1 = atomicProtocols.SM(b,encrypt.ADD(resList2.get(i),B,list.get("n")),list.get("n"), list.get("pk"), list.get("pk"), list.get("pk"), list.get("g"), list.get("SK1"), list.get("SK2"));
                EncResult delta = secureProtocols.SLT(A1,B1, list.get("pk"), list.get("g"), list.get("n"), list.get("SK1"), list.get("SK2"));
                matrix[i][rowNum] = delta;
            }
            long endTime3 = System.currentTimeMillis();
            long duration = endTime - startTime + endTime3 - startTime3;
            System.out.println("Query Time:"+duration);
//            BigInteger X1 = decrypt.SKDecrypt(S1.result1, list.get("SK"), list.get("n"));
//            BigInteger X2 = decrypt.SKDecrypt(S1.result2, list.get("SK"), list.get("n"));
//            BigInteger Y1 = decrypt.SKDecrypt(S2.result1, list.get("SK"), list.get("n"));
//            BigInteger Y2 = decrypt.SKDecrypt(S2.result2, list.get("SK"), list.get("n"));
//            BigInteger H1 = decrypt.SKDecrypt(m_m_dist, list.get("SK"), list.get("n"));
//            BigInteger H2 = decrypt.SKDecrypt(m_m_jaccard, list.get("SK"), list.get("n"));
//            System.out.println("Dist_min=" + X1 + "," + "Dist_max=" + X2);
//            System.out.println("Dist_max-Dist_min=" + H1);
//            System.out.println("Jaccard_min=" + Y1 + "," + "Jaccard_max=" + Y2);
//            System.out.println("Jaccard_max-Jaccard_min=" + H2);
        }
        /*
        // 输出矩阵的内容
        for (EncResult[] encResults : matrix) {
            for (EncResult encResult : encResults) {
                System.out.print(decrypt.SKDecrypt(encResult,list.get("SK"),list.get("n")) + " "); // 输出矩阵中的元素
            }
            System.out.println(); // 在行末添加换行符
        }

         */
        // 关闭文件输入流

        fileInputStream.close();

    }
        /*
        new StaticDtPkc();
        System.out.println("请输入第一个明文:");
        Scanner input = new Scanner(System.in);
        String str = input.nextLine();
        BigInteger bigInteger = new BigInteger(str);
        //加密过程
        long startTime_enc = System.currentTimeMillis();
        Encrypt object = new Encrypt();
        EncResult result = object.Encrypt(bigInteger,list.get("g"),list.get("pk_u"),list.get("n"));
        long endTime_enc = System.currentTimeMillis();
        long duration_enc = endTime_enc - startTime_enc;
        System.out.println("加密时间为：" + duration_enc + "ms");
        System.out.println("c0=" + result.c0 + ", c1=" + result.c1);

        Decrypt decrypt =new Decrypt();
        long startTime_SKdecO = System.currentTimeMillis();
        EncResult SKdecOResult = decrypt.PSDO(result, list.get("SK11"), list.get("n"));
        long endTime_SKdecO = System.currentTimeMillis();
        long duration_SKdecO = endTime_SKdecO - startTime_SKdecO;
        System.out.println("SK1解密时间为：" + duration_SKdecO + "ms");
        System.out.println(" 强私钥是： " + list.get("SK11") + "； 解密的结果是： " + SKdecOResult.ct1);
        //部分解密步骤2
        long startTime_SKdecT = System.currentTimeMillis();
        BigInteger SKdecTResult = decrypt.PSDT(result, list.get("SK22"), list.get("n"));
        long endTime_SKdecT = System.currentTimeMillis();
        long duration_SKdecT = endTime_SKdecT - startTime_SKdecT;
        System.out.println("SK2解密时间为：" + duration_SKdecT + "ms");
        System.out.println(" 强私钥是： " + list.get("SK22") + "； 解密的结果是： " + SKdecTResult);
        long startTime_SKdecO = System.currentTimeMillis();
        EncResult SKdecOResult = decrypt.PSD1(result, list.get("SK1"),list.get("n"));
        long endTime_SKdecO = System.currentTimeMillis();
        long duration_SKdecO = endTime_SKdecO - startTime_SKdecO;
        System.out.println("SK1解密时间为：" + duration_SKdecO + "ms");
        System.out.println(" 强私钥是： " + list.get("SK1") + "； 解密的结果是： " + SKdecOResult.ct1);
        System.out.println("======================");
        long startTime_SKdecT = System.currentTimeMillis();
        BigInteger SKdecTResult = decrypt.PSD2(result, list.get("SK2"),list.get("n"));
        long endTime_SKdecT = System.currentTimeMillis();
        long duration_SKdecT = endTime_SKdecT - startTime_SKdecT;
        System.out.println("SK2解密时间为：" + duration_SKdecT + "ms");
        System.out.println(" 强私钥是： " + list.get("SK2") + "； 解密的结果是： " + SKdecTResult);*/
}
