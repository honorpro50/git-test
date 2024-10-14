package algorithm;
import Result.EncResult;
import Result.EncResultPair;
import Result.EncResultStorage;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.*;
import static algorithm.StaticDtPkc.list;

public class SSPQ {
    public static void main(String[] args) {
        new StaticDtPkc();
        SecureProtocols secureProtocols = new SecureProtocols();
        SimilarityCalculation similarityCalculation = new SimilarityCalculation();
        Encrypt encrypt = new Encrypt();
        Decrypt decrypt = new Decrypt();
        ExecutorService executor = null;
        try {
            //给定查询请求Q
            Vector<BigInteger> vector1 = new Vector<>();
            Vector<EncResult> qx = new Vector<>();
            vector1.add(BigInteger.valueOf(434523));
            vector1.add(BigInteger.valueOf(734525));
            for (BigInteger element : vector1) {
                qx.add(encrypt.Encrypt(element, list.get("g"), list.get("pk_u"), list.get("n")));
            }
            Vector<BigInteger> vector2 = new Vector<>();
            Vector<EncResult> qy = new Vector<>();
//        vector1.add(BigInteger.valueOf(4367584));
//        vector1.add(BigInteger.valueOf(7426373));
            vector2.add(BigInteger.valueOf(495));
            vector2.add(BigInteger.valueOf(486));
            vector2.add(BigInteger.valueOf(455));
            vector2.add(BigInteger.valueOf(450));
            vector2.add(BigInteger.valueOf(420));
            vector2.add(BigInteger.valueOf(220));
            for (BigInteger element : vector2) {
                qy.add(encrypt.Encrypt(element, list.get("g"), list.get("pk_u"), list.get("n")));
            }
            // 指定Excel文件路径
            String filePath = "D:\\Desktop\\Mycode\\data\\data.xlsx";
            // 创建文件输入流
            FileInputStream fileInputStream = new FileInputStream(filePath);
            // 创建工作簿对象
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            // 获取第一个工作表
            Sheet sheet1 = workbook.getSheetAt(0);
            Sheet sheet2 = workbook.getSheetAt(1);
            executor = Executors.newFixedThreadPool(4);
            // 遍历设施数据
            for (Row row2 : sheet2) {
                // 创建 Callable 任务
                Callable<Vector<EncResult>> task1 = () -> {
                    Vector<BigInteger> fp = new Vector<>();
                    double fcell1 = row2.getCell(1).getNumericCellValue();
                    double fcell2 = row2.getCell(2).getNumericCellValue();
                    BigInteger fvalue1 = BigInteger.valueOf((long) fcell1);
                    BigInteger fvalue2 = BigInteger.valueOf((long) fcell2);
                    fp.add(fvalue1);
                    fp.add(fvalue2);
                    Vector<EncResult> FVector = new Vector<>();
                    for (BigInteger element : fp) {
                        FVector.add(encrypt.Encrypt(element, list.get("g"), list.get("pk_u"), list.get("n")));
                    }
                    return FVector;
                };
                Callable<Vector<EncResult>> task2 = () -> {
                    // 第二个任务的代码
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
                    fj.add(fvalue3);
                    fj.add(fvalue4);
                    fj.add(fvalue5);
                    fj.add(fvalue6);
                    fj.add(fvalue7);
                    fj.add(fvalue8);
                    Vector<EncResult> FVector = new Vector<>();
                    for (BigInteger element : fj) {
                        FVector.add(encrypt.Encrypt(element, list.get("g"), list.get("pk_u"), list.get("n")));
                    }
                    return FVector;
                };
                // 提交任务给线程池
                Future<Vector<EncResult>> future1 = executor.submit(task1);
                Future<Vector<EncResult>> future2 = executor.submit(task2);
                Vector<EncResult> FVector1 = future1.get();
                Vector<EncResult> FVector2 = future2.get();
                List<EncResult> resultList1 = new ArrayList<>();
                List<EncResult> resultList2 = new ArrayList<>();
                for (Row row1 : sheet1) {
                    Callable<Vector<EncResult>> task3 = () -> {
                        Vector<EncResult> Vector = new Vector<>();
                        Vector<BigInteger> p = new Vector<>();
                        double cell1 = row1.getCell(1).getNumericCellValue();
                        double cell2 = row1.getCell(2).getNumericCellValue();
                        BigInteger value1 = BigInteger.valueOf((long) cell1);
                        BigInteger value2 = BigInteger.valueOf((long) cell2);
                        p.add(value1);
                        p.add(value2);
                        for (BigInteger element : p) {
                            Vector.add(encrypt.Encrypt(element, list.get("g"), list.get("pk_o"), list.get("n")));
                        }
                        return Vector;
                    };
                    Callable<Vector<EncResult>> task4 = () -> {
                        Vector<EncResult> Vector = new Vector<>();
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
                            Vector.add(encrypt.Encrypt(element, list.get("g"), list.get("pk_o"), list.get("n")));
                        }
                        return Vector;
                    };
                    Future<Vector<EncResult>> future3 = executor.submit(task3);
                    Future<Vector<EncResult>> future4 = executor.submit(task4);
                    Vector<EncResult> dataVector1 = future3.get();
                    Vector<EncResult> dataVector2 = future4.get();
                    EncResult res_dist = similarityCalculation.SEDC(dataVector1, FVector1, list.get("pk_o"), list.get("pk_u"), list.get("pk"), list.get("n"), list.get("g"), list.get("SK11"), list.get("SK22"));
                    EncResult res_jaccard = similarityCalculation.SJC(dataVector2, FVector2, list.get("pk_o"), list.get("pk_u"), list.get("pk"), list.get("n"), list.get("g"), list.get("SK11"), list.get("SK22"));
                    resultList1.add(res_dist);
                    resultList2.add(res_jaccard);
                }
                long startTime = System.currentTimeMillis();
                EncResultPair S1 = secureProtocols.SMMSn(resultList1, list.get("pk"), list.get("g"), list.get("n"), list.get("SK1"), list.get("SK2"));
                EncResultPair S2 = secureProtocols.SMMSn(resultList2, list.get("pk"), list.get("g"), list.get("n"), list.get("SK1"), list.get("SK2"));
                BigInteger nSubOne = list.get("n").subtract(BigInteger.ONE);
                //距离和相似度的最大值减最小值
//                EncResult m_m_dist = encrypt.ADD(S1.result1);
                BigInteger X1 = decrypt.SKDecrypt(S1.result1, list.get("SK"), list.get("n"));
                BigInteger X2 = decrypt.SKDecrypt(S1.result2, list.get("SK"), list.get("n"));
                BigInteger Y1 = decrypt.SKDecrypt(S2.result1, list.get("SK"), list.get("n"));
                BigInteger Y2 = decrypt.SKDecrypt(S2.result2, list.get("SK"), list.get("n"));
                System.out.println("Dist_min=" + X1 + "," + "Dist_max=" + X2);
                System.out.println("Jaccard_min=" + Y1 + "," + "Jaccard_max=" + Y2);
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                System.out.println(duration + "ms");
            }
            // 关闭文件输入流
            fileInputStream.close();
        } catch (IOException | EncryptedDocumentException ex) {
            ex.printStackTrace();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
