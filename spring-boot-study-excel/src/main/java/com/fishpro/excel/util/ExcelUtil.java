package com.fishpro.excel.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

public class ExcelUtil {

    public static void main(String [] args){

    }
    /**
     * ## 3.1 创建 Workbook
     * - `HSSFWorkbook` 是操作 Excel2003 以前（包括2003）的版本，扩展名是.xls；
     * - `XSSFWorkbook` 是操作 Excel2007 后的版本，扩展名是.xlsx；
     * - `SXSSFWorkbook` 是操作 Excel2007 后的版本，扩展名是.xlsx；
     *
     * 返回空 输出 workbook.xls workbook.xlsx 注意此时 excel元素不全，还不能打开
     */
    public static void CreateNewWorkbook() {
        Workbook wb = new HSSFWorkbook();
        try {
            OutputStream fileOut = new FileOutputStream("workbook.xls");
            wb.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Workbook wb2 = new XSSFWorkbook();
        try (OutputStream fileOut = new FileOutputStream("workbook.xlsx")) {
            wb2.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *## 3.2 创建工作表 Sheet
     * - 工作表名称不要超过 31 个字符
     * - 名称不能含有特殊字符
     * - 可以使用 WorkbookUtil.createSafeSheetName 来创建安全的工作表名称
     * 返回空 输出 workbook.xls  注意此时 excel元素不全，还不能打开
     * */
    public static void CreateNewSheet() {
        Workbook wb = new HSSFWorkbook();  // or new XSSFWorkbook();
        Sheet sheet1 = wb.createSheet("new sheet");
        Sheet sheet2 = wb.createSheet("new second sheet");
        String safeName = WorkbookUtil.createSafeSheetName("[O'Brien's sales*?]"); // returns " O'Brien's sales   "
        Sheet sheet3 = wb.createSheet(safeName);
        try {
            OutputStream fileOut = new FileOutputStream("workbook.xls");
            wb.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *## 3.3 创建单元格 Cells
     * - 先有行在有列，先要创建 Row 在创建 Cell
     * - 创建一个样式
     * - 创建一个日期类型的值
     * - 创建日期、小数、字符 、布尔等类型
     * - 创建一个边框类型单元格
     * - 数据格式化单元格
     * -
     * */
    public static void CreateNewCell() {
        int rowNum=0;
        int colNum=0;
        Workbook wb = new HSSFWorkbook();  // or new XSSFWorkbook();
        Sheet sheet = wb.createSheet("new sheet");
        //先行后列
        Row row = sheet.createRow(rowNum++);

        //创建列
        Cell cell = row.createCell(colNum++);
        cell.setCellValue(new Date());
        //创建一个列的样式
        CellStyle cellStyle = wb.createCellStyle();
        //获取一个帮助类设置样式
        CreationHelper createHelper = wb.getCreationHelper();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));

        cell = row.createCell(colNum++);
        cell.setCellValue(new Date());
        cell.setCellStyle(cellStyle);
        //使用 Calendar
        cell = row.createCell(colNum++);
        cell.setCellValue(Calendar.getInstance());
        cell.setCellStyle(cellStyle);
        //创建不同的类型的单元格
        row.createCell(colNum++).setCellValue(1.1);
        row.createCell(colNum++).setCellValue(new Date());
        row.createCell(colNum++).setCellValue(Calendar.getInstance());
        row.createCell(colNum++).setCellValue("a string");
        row.createCell(colNum++).setCellValue(true);
        row.createCell(colNum++).setCellType(CellType.ERROR);

        // Create a cell and put a value in it.
        cell = row.createCell(colNum++);
        cell.setCellValue(4);

        // 边框
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);//底部边框
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);//左边
        style.setLeftBorderColor(IndexedColors.GREEN.getIndex());
        style.setBorderRight(BorderStyle.THIN);//右边
        style.setRightBorderColor(IndexedColors.BLUE.getIndex());
        style.setBorderTop(BorderStyle.MEDIUM_DASHED);//上边
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());//颜色
        cell.setCellStyle(style);

        //数据格式化
        DataFormat format = wb.createDataFormat();
        row = sheet.createRow(rowNum++);
        cell = row.createCell(colNum);
        cell.setCellValue(11111.25);
        style = wb.createCellStyle();
        style.setDataFormat(format.getFormat("0.0"));
        cell.setCellStyle(style);

        row = sheet.createRow(rowNum++);
        cell = row.createCell(colNum);
        cell.setCellValue(11111.25);
        style = wb.createCellStyle();
        style.setDataFormat(format.getFormat("#,##0.0000"));
        cell.setCellStyle(style);


        try {
            OutputStream fileOut = new FileOutputStream("workbook.xls");
            wb.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 使用 File 的方式读取 Excel
     * 读取单元格
     * */
    public static void OpenExcelByFile(){
        try {
            Workbook wb = WorkbookFactory.create(new File("workbook.xls"));
            //读取
            Sheet sheet=wb.getSheetAt(0);//第一个
            Sheet sheet1=wb.getSheet("sheet1");//根据名称读取
            Row row=sheet.getRow(0);//获取行
            Cell cell=row.getCell(0);//获取第一行

        }catch (Exception ex){

        }

    }

    /**
     * 使用 FileInputStream
     * */
    public static void OpenExcelByFileInputStream(){
        try {
            Workbook wb = WorkbookFactory.create(new FileInputStream("workbook.xls"));
            //遍历
        }catch (Exception ex){

        }
    }


}
