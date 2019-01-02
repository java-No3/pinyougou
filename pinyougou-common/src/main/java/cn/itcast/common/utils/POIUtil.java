package cn.itcast.common.utils;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


/**
 * excel读写工具类 */  
public class POIUtil {  
    private static Logger logger  = Logger.getLogger(POIUtil.class);  
    private final static String xls = "xls";  
    private final static String xlsx = "xlsx";  
      
    /** 
     * 读入excel文件，解析后返回
     */  
    public static List<String[]> readExcel(String url , HttpServletRequest request) throws Exception{

        URL u = new URL(url);
        URLConnection c = u.openConnection();
        InputStream is = c.getInputStream();

        String realPath = request.getSession().getServletContext().getRealPath("");
        // File file = new File(realPath);
         File file = new File("E:\\log\\test.xlsx");
        if(!file.exists()){ //先得到文件的上级目录，并创建上级目录，在创建文件
            file.getParentFile().mkdir();
            try {
                //创建文件
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream fos = null;

        //定义一个缓存区
        byte[] buf = new byte[1024];


        int len = 0;
        try{

            //获取到输出对象
            fos = new FileOutputStream(file);
            //进行读取
            while ((len = is.read(buf)) != -1){
                //写入到文件中
                fos.write(buf, 0 , len);
            }
            //刷新，将缓冲区数据写入文件
            fos.flush();
        }catch (IOException e){
            e.printStackTrace();
        }


        //检查文件  
        checkFile(file);  
        //获得Workbook工作薄对象  
        Workbook workbook = getWorkBook(file);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回  
        List<String[]> list = new ArrayList<String[]>();  
        if(workbook != null){  
            for(int sheetNum = 0;sheetNum < workbook.getNumberOfSheets();sheetNum++){  
                //获得当前sheet工作表  
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if(sheet == null){  
                    continue;  
                }  
                //获得当前sheet的开始行  
                int firstRowNum  = sheet.getFirstRowNum();  
                //获得当前sheet的结束行  
                int lastRowNum = sheet.getLastRowNum();  
                //循环除了第一行的所有行  
                for(int rowNum = firstRowNum+1;rowNum <= lastRowNum;rowNum++){  
                    //获得当前行  
                    Row row = sheet.getRow(rowNum);
                    if(row == null){  
                        continue;  
                    }  
                    //获得当前行的开始列  
                    int firstCellNum = row.getFirstCellNum();  
                    //获得当前行的列数  
                    int lastCellNum = row.getPhysicalNumberOfCells();  
                    String[] cells = new String[row.getPhysicalNumberOfCells()];  
                    //循环当前行  
                    for(int cellNum = firstCellNum; cellNum < lastCellNum;cellNum++){  
                        Cell cell = row.getCell(cellNum);
                        cells[cellNum] = getCellValue(cell);  
                    }  
                    list.add(cells);  
                }  
            }  
            workbook.close();  
        }  
        return list;  
    }  
    public static void checkFile(File file) throws Exception{
        String message = "该文件是EXCEL文件！";
        if(!file.exists()){
            message = "文件不存在！";
            throw new Exception(message);
        }
        if(!file.isFile()||((!file.getName().endsWith(xls)&&!file.getName().endsWith(xlsx)))){
            System.out.println(file.isFile()+"==="+file.getName().endsWith(xls)+"==="+file.getName().endsWith(xlsx));
            System.out.println(file.getName());
            message = "文件不是Excel";
            throw new Exception(message);
        }
    }  
    public static Workbook getWorkBook(File file) {
        //获得文件名
       // String fileName = file.getOriginalFilename();
        String fileName = file.getName();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {  
            //获取excel文件的io流  
            // InputStream is = file.getInputStream();
            InputStream is = new FileInputStream(file);

            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if(fileName.endsWith(xls)){  
                //2003  
                workbook = new HSSFWorkbook(is);
            }else if(fileName.endsWith(xlsx)){  
                //2007  
                workbook = new XSSFWorkbook(is);
            }  
        } catch (IOException e) {  
            logger.info(e.getMessage());  
        }  
        return workbook;  
    }  
    public static String getCellValue(Cell cell){
        String cellValue = "";  
        if(cell == null){  
            return cellValue;  
        }  
        //把数字当成String来读，避免出现1读成1.0的情况  
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }  
        //判断数据的类型  
        switch (cell.getCellType()){  
            case Cell.CELL_TYPE_NUMERIC: //数字
                cellValue = String.valueOf(cell.getNumericCellValue());  
                break;  
            case Cell.CELL_TYPE_STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());  
                break;  
            case Cell.CELL_TYPE_BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());  
                break;  
            case Cell.CELL_TYPE_FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());  
                break;  
            case Cell.CELL_TYPE_BLANK: //空值
                cellValue = "";  
                break;  
            case Cell.CELL_TYPE_ERROR: //故障
                cellValue = "非法字符";  
                break;  
            default:  
                cellValue = "未知类型";  
                break;  
        }  
        return cellValue;  
    }  
}