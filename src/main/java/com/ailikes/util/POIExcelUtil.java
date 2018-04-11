package com.ailikes.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class POIExcelUtil {

    public static final String FILE_EXTENSION_XLS  = "xls";
    public static final String FILE_EXTENSION_XLSX = "xlsx";

    /**
     * 
     * @param Map <String,String> maps 属性表，成员属性age为KEY，中文名称为VALUE
     * @param List <T> list 需要导出的数据列表对象
     * @param String type 文件类型 xls 或者 xlsx
     * 
     * @return Workbook 导出成功 null 导出失败
     */
    public static <T> Workbook excelExport(Map<String, String> maps,
                                          List<T> list,
                                          String type) {

        Workbook wb = null;
        try {
            if (type.equals(FILE_EXTENSION_XLS)) {
                wb = new HSSFWorkbook();
            }
            if (type.equals(FILE_EXTENSION_XLSX)) {
                wb = new XSSFWorkbook();
            }
            CreationHelper createHelper = wb.getCreationHelper();
            Sheet sheet = wb.createSheet("sheet1");
            Set<String> sets = maps.keySet();
            Row row = sheet.createRow(0);
            int i = 0;
            // 定义表头
            for (Iterator<String> it = sets.iterator(); it.hasNext();) {
                String key = it.next();
                Cell cell = row.createCell(i++);
                cell.setCellValue(createHelper.createRichTextString(maps.get(key)));
            }
            // 填充表单内容
            System.out.println("--------------------100%");
            float avg = list.size() / 20f;
            int count = 1;
            for (int j = 0; j < list.size(); j++) {
                T p = list.get(j);
                Class classType = p.getClass();
                int index = 0;
                Row row1 = sheet.createRow(j + 1);
                for (Iterator<String> it = sets.iterator(); it.hasNext();) {
                    String key = it.next();
                    String firstLetter = key.substring(0, 1).toUpperCase();
                    // 获得和属性对应的getXXX()方法的名字
                    String getMethodName = "get" + firstLetter + key.substring(1);
                    // 获得和属性对应的getXXX()方法
                    Method getMethod = classType.getMethod(getMethodName, new Class[] {});
                    // 调用原对象的getXXX()方法
                    Object value = getMethod.invoke(p, new Object[] {});
                    Cell cell = row1.createCell(index++);
                    cell.setCellValue(value.toString());
                }
                if (j > avg * count) {
                    count++;
                    System.out.print("I");
                }
                if (count == 20) {
                    System.out.print("I100%");
                    count++;
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
        return wb;
    }
    /**
     * 
     * @param Map <String,String> maps 属性表，成员属性age为KEY，中文名称为VALUE
     * @param List <Map<String, Object>> list 需要导出的数据Map集合
     * @param String type 文件类型 xls 或者 xlsx
     * @param Map<String,Map<String,String>> 格式化
     * 
     * @return Workbook 导出成功 null 导出失败
     */
    public static Workbook excelExportByMap(Map<String, String> maps,
                                           List<Map<String, Object>> list,
                                           String type,Map<String,Map<String,String>> format) {

         Workbook wb = null;
         try {
             if (type.equals(FILE_EXTENSION_XLS)) {
                 wb = new HSSFWorkbook();
             }
             if (type.equals(FILE_EXTENSION_XLSX)) {
                 wb = new XSSFWorkbook();
             }
             CreationHelper createHelper = wb.getCreationHelper();
             Sheet sheet = wb.createSheet("sheet1");
             Set<String> sets = maps.keySet();
             Row row = sheet.createRow(0);
             int i = 0;
             // 定义表头
             for (Iterator<String> it = sets.iterator(); it.hasNext();) {
                 String key = it.next();
                 Cell cell = row.createCell(i++);
                 cell.setCellValue(createHelper.createRichTextString(maps.get(key)));
             }
             // 填充表单内容
             System.out.println("--------------------100%");
             float avg = list.size() / 20f;
             int count = 1;
             for (int j = 0; j < list.size(); j++) {
                 Map<String,Object> p = list.get(j);
                 int index = 0;
                 Row row1 = sheet.createRow(j + 1);
                 for (Iterator<String> it = sets.iterator(); it.hasNext();) {
                     String key = it.next();
                     Object value = p.get(key);
                     if(null!= format && null!=format.get(key)){
                         value = formatString(format.get(key),value.toString());
                     }
                     Cell cell = row1.createCell(index++);
                     cell.setCellValue(value.toString());
                 }
                 if (j > avg * count) {
                     count++;
                     System.out.print("I");
                 }
                 if (count == 20) {
                     System.out.print("I100%");
                     count++;
                 }
             }
         } catch (SecurityException e) {
             e.printStackTrace();
             return null;
         } catch (IllegalArgumentException e) {
             e.printStackTrace();
             return null;
         }
         return wb;
     }
    
    private static String formatString(Map<String,String> format,String value){
        return format.get(value)!=null?format.get(value):value;
    }
}
