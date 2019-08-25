Excel 在日常操作中经常使用到，Spring Boot 中使用 POI 操作 Excel 


[本项目源码 github 下载 ](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-excel)



# 1 新建 Spring Boot Maven 示例工程项目

注意：本示例是用 IDEA 开发工具
1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=excel
3. 选择依赖 `Spring Web Starter` 前面打钩。
4. 项目名设置为 `spring-boot-study-excel`.

文件上传不需要引入第三方组件。

# 2 依赖引入 Pom.xml

```xml
 <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml -->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>4.0.1</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```


# 3 操作 Excel
不同的 Excel 版本具有不同的类来操作本示例中使用 xls 后缀版本。详细请参见 [官方文档](http://poi.apache.org/components/spreadsheet/quick-guide.html)

## 3.1 创建 Workbook
- `HSSFWorkbook` 是操作 Excel2003 以前（包括2003）的版本，扩展名是.xls；
- `XSSFWorkbook` 是操作 Excel2007 后的版本，扩展名是.xlsx；
- `SXSSFWorkbook` 是操作 Excel2007 后的版本，扩展名是.xlsx；

```java
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
```

## 3.2 创建工作表 Sheet
- 工作表名称不要超过 31 个字符
- 名称不能含有特殊字符
- 可以使用 WorkbookUtil.createSafeSheetName 来创建安全的工作表名称
```java
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

``` 

## 3.3 创建单元格 Cells
- 先有行在有列，先要创建 Row 在创建 Cell
- 创建一个样式
- 创建一个日期类型的值
- 创建日期、小数、字符 、布尔等类型
- 创建一个边框类型单元格
- 数据格式化单元格
```java
 /**
     *## 3.3 创建单元格 Cells
     * - 先有行在有列，先要创建 Row 在创建 Cell
     * */
    public void CreateNewCell() {
        Workbook wb = new HSSFWorkbook();  // or new XSSFWorkbook();
        Sheet sheet1 = wb.createSheet("new sheet");
        //先行后列
        Row row = sheet1.createRow(0);

        //创建列
        Cell cell = row.createCell(0);
        cell.setCellValue(new Date());
        //创建一个列的样式
        CellStyle cellStyle = wb.createCellStyle();
        //获取一个帮助类设置样式
        CreationHelper createHelper = wb.getCreationHelper();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
        cell = row.createCell(1);
        cell.setCellValue(new Date());
        cell.setCellStyle(cellStyle);
        //使用 Calendar
        cell = row.createCell(2);
        cell.setCellValue(Calendar.getInstance());
        cell.setCellStyle(cellStyle);
        //创建不同的类型的单元格
        row.createCell(3).setCellValue(1.1);
        row.createCell(4).setCellValue(new Date());
        row.createCell(5).setCellValue(Calendar.getInstance());
        row.createCell(6).setCellValue("a string");
        row.createCell(7).setCellValue(true);
        row.createCell(8).setCellType(CellType.ERROR);

        try {
            OutputStream fileOut = new FileOutputStream("workbook.xls");
            wb.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```


## 3.4 读取与获取Excel
 使用 File 的方式读取 Excel
 ```java
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
 ```

 使用 FileInputStream 需要内存支持
 
 ```java
 public static void OpenExcelByFileInputStream(){
        try {
            Workbook wb = WorkbookFactory.create(new FileInputStream("workbook.xls"));
            //遍历
        }catch (Exception ex){

        }
    }
 ```



---
参考：

[https://poi.apache.org/](http://poi.apache.org/components/spreadsheet/quick-guide.html)

[https://www.iteye.com/blog/chenhailong-1498528](https://www.iteye.com/blog/chenhailong-1498528)