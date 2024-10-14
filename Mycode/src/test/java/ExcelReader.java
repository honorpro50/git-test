import Result.EncResult;
import Result.EncResultPair;
import Result.EncResultStorage;
import algorithm.Decrypt;
import algorithm.Encrypt;
import algorithm.SecureProtocols;
import algorithm.StaticDtPkc;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static algorithm.StaticDtPkc.list;

public class ExcelReader {

    public static void main(String[] args) {
        new StaticDtPkc();
        SecureProtocols secureProtocols =new SecureProtocols();
        EncResultStorage storage = new EncResultStorage();
        Vector<EncResult> Q = new Vector<>();
        Encrypt encrypt = new Encrypt();
        Decrypt decrypt = new Decrypt();
        try {
            // 指定Excel文件路径
            String filePath = "D:\\Desktop\\Mycode\\data\\data.xlsx";
            // 创建文件输入流
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            // 创建工作簿对象
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            // 获取第一个工作表
            Sheet sheet = workbook.getSheetAt(0);
            List<EncResult> resultList = new ArrayList<>();
            // 遍历每一行
            for (Row row : sheet) {
                // 遍历每个单元格
                for (Cell cell : row) {
                    if (cell.getColumnIndex() == 0){
                        // 获取单元格的数值
                        double cellValue = cell.getNumericCellValue();
                        // 转换为 BigInteger
                        BigInteger value = BigInteger.valueOf((long) cellValue);
                        EncResult cell1 = encrypt.Encrypt(value,list.get("g"),list.get("pk"),list.get("n"));
                        // 输出单元格的值
                        System.out.print(cell);
                        resultList.add(cell1);
                    }
                }
                System.out.println(); // 换行
            }
            long startTime = System.currentTimeMillis();
            EncResultPair S = secureProtocols.SMMSn(resultList,list.get("pk"),list.get("g"),list.get("n"),list.get("SK1"),list.get("SK2"));
            BigInteger X1 = decrypt.SKDecrypt(S.result1,list.get("SK"),list.get("n"));
            BigInteger Y1 = decrypt.SKDecrypt(S.result2,list.get("SK"),list.get("n"));
            System.out.println("min=" + X1);
            System.out.println("max=" + Y1);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println(duration + "ms");
            // 关闭文件输入流
            fileInputStream.close();
        } catch (IOException | EncryptedDocumentException ex) {
            ex.printStackTrace();
        }
    }
}
