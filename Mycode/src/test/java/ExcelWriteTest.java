import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ExcelWriteTest {
    String PATH = "D:\\Desktop\\Mycode\\data/";
    @Test
    public void testWrite() throws Exception {

        //创建一个工作簿
        Workbook workbook = new XSSFWorkbook();
        //创建一个工作表
        Sheet sheet = workbook.createSheet("hello");
        //创建第一行
        Row row1 = sheet.createRow(0);
        //创建第一个单元格,并设置单元格的值
        Cell cell11 = row1.createCell(0);
        cell11.setCellValue("今日天气");
        Cell cell12 = row1.createCell(1);
        cell12.setCellValue("今日任务");
        //创建第二行
        Row row2 = sheet.createRow(1);
        //创建第一个单元格,并设置单元格的值
        Cell cell21 = row2.createCell(0);
        cell21.setCellValue("晴");
        Cell cell22 = row2.createCell(1);
        cell22.setCellValue("做实验");

        //生成一张表IO 流，以xlsx结尾
        FileOutputStream fileOutputStream = new FileOutputStream(PATH + "Hello.xlsx");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        System.out.println("文件生成完毕！");
    }

    @Test
    public void testWriteBigData() throws Exception {
        //创建一个工作簿
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        //创建一个工作表
        Sheet sheet = workbook.createSheet();
        long start = System.currentTimeMillis();
        //写入数据
        for (int rowNum = 0; rowNum < 10000; rowNum++) {
            Row row = sheet.createRow(rowNum);
            for (int cellNum = 0; cellNum < 10; cellNum++) {
                Cell cell = row.createCell(cellNum);
                cell.setCellValue(cellNum);
            }
        }
        System.out.println("over");
        //生成一张表IO 流，以xlsx结尾
        FileOutputStream fileOutputStream = new FileOutputStream(PATH + "Hello.xlsx");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        workbook.dispose();
        long end = System.currentTimeMillis();
        System.out.println((double) (end - start)/1000);
        System.out.println("文件生成完毕！");

    }
}
