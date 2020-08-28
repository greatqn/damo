/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.damo.api.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 把xlsx里的内容导入mysql
 */
public class ReadExcel {

    public static void main(String[] args)
    {  
        String fname = "F:\\workspace\\test\\src\\test\\java\\testcase\\demo.xlsx";
        ArrayList<ArrayList<String>> list = readXls(fname,null);
        System.out.println(list);
    }  
     
    /***
     * excel 中的插入
     * @param fileName   excel文件名
     * @param caseName   对应每个case所需要的数据，一般都默认
     * @param otherMap   除excel以外要插入的数据
     * @param sheetName  excel中要读取的sheetName
     */
    public static void  insertForXls(String fileName,String caseName,HashMap<String,Object> otherMap,String sheetName) {
        ArrayList<ArrayList<String>> list  = readXls(fileName,sheetName);
        String tbname = "dw_"+fileName.split("dw_")[1].replace(".xlsx", "").replace(".xls", "");
        String sqlx = "insert into "+tbname + "(";
        boolean is_id = false;
        boolean iscaseName = false;
        
        ArrayList<String> cliNameList = list.get(0);
        if(cliNameList.get(0).equals("casename"))
        {
            iscaseName = true;
            cliNameList.remove(0);
        }
        int index = 0;
        int tmpIndex = 0;
        for(String em : list.get(0))
        {
            if(em.equals("id")){
                is_id = true;
                tmpIndex = index;
            }
            sqlx = sqlx + em+",";
            index ++;
        }
        String otherValue = "";
        if(otherMap != null) 
        {
            for(String em : otherMap.keySet()) 
            {
                sqlx = sqlx + em+",";
                otherValue = otherValue + "\'"+ otherMap.get(em) + "\',";
            }
        }
        sqlx = sqlx.substring(0,sqlx.length()-1)+") VALUES(";
        list.remove(0);
        for(ArrayList<String> em_list : list) 
        {
            
            
            String sql = sqlx + "";
            if(iscaseName)
            {
                if(em_list.get(0).indexOf(caseName)>-1)
                    em_list.remove(0);
                else
                    continue;
            }
            
            int index1 = 0;
            String id = "";
            for (int i = 0; i < cliNameList.size(); i++) {//循环标题，避免内容空值
                String em = "null";
                if(i<em_list.size()){
                    em = em_list.get(i);    
                }
                
                if(em.equals("null"))
                    sql =  sql + "NULL,";
                else
                    sql =  sql + "\'"+ em + "\',";
                if(index1 == tmpIndex)
                    id = em;
                index1++;
            }

            sql = sql + otherValue ;
            sql = sql.substring(0,sql.length()-1)+")";
//          System.out.println(sql);
            if(sql.length()>0){
                if(is_id == true){
                    DataPprocess.delete("delete from "+tbname+" where id="+id);
                }
                DataPprocess.insert(sql);
            }
            
        }
    }
    
    /***
     * 修改记录  updateMap为空第一列是修改条件，后面是修改内容
     * @param fileName    excel文件名
     * @param updateMap   修改条件
     * @param sheetName   excel中要读取的sheetName 
     */
    
    public static void updateForXls(String fileName,HashMap<String,Object> updateMap,String sheetName){
        ArrayList<ArrayList<String>> list  = readXls(fileName,sheetName);
        String tbname = "dw_"+fileName.split("dw_")[1].replace(".xlsx", "").replace(".xls", "");
        
        String sql2 = "update  "+tbname +" set ";
        ArrayList<String> cloNameList = list.get(0);
        
        if(updateMap == null || updateMap.keySet().size() == 0) {
            for(int index=1;index<list.size();index++) {
                String sql1 = "";
                ArrayList<String> cloDataList = list.get(index);
                for(int i=1;i<cloDataList.size();i++) {
                    sql1 = sql1 + cloNameList.get(i) + "=\'"+ cloDataList.get(i).trim() +"\' ,";
                }
                sql1 = sql1.substring(0,sql1.length()-1);
                sql1 = sql1 + "where " +cloNameList.get(0) + "=\'"+ cloDataList.get(0).trim() +"\'"  ;
                String sql = sql2 + sql1 ;
                System.out.println(sql);
                DataPprocess.update(sql);
            }
        } else {
            for(int index=1;index<list.size();index++) {
                String sql1 = "";
                ArrayList<String> cloDataList = list.get(index);
                for(String key : updateMap.keySet()) {
                    sql1 = sql1 + " " + key +  "=\'"+ updateMap.get(key).toString().trim() +"\',"  ;
                }
                sql1 = sql1.substring(0,sql1.length()-1);
                sql1 = sql1 + "where 1=1 and " ;
                
                for(int i=0;i<cloDataList.size();i++) {
                    sql1 = sql1 + cloNameList.get(i) + "=\'"+ cloDataList.get(i).trim() +"\' ,";
                }
                sql1 = sql1.substring(0,sql1.length()-1);
                
                
                String sql = sql2 + sql1 ;
            //  System.out.println(sql);
                DataPprocess.insert(sql);
            }
        }
    }
    
    /** 
     * 得到Excel，并解析内容  对2007及以上版本 使用XSSF解析 
     * @param file 
     * @throws FileNotFoundException 
     * @throws IOException 
     * @throws InvalidFormatException  
     */  
    public static ArrayList<ArrayList<String>>  readXls(String fileName,String sheetName){  
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        boolean isE2007 = false;    //判断是否是excel2007格式  
        if(fileName.endsWith("xlsx"))  
            isE2007 = true;  
        try {  
            InputStream input = new FileInputStream(fileName);  //建立输入流  
            Workbook wb  = null;  
            //根据文件格式(2003或者2007)来初始化  
            if(isE2007)  
                wb = new XSSFWorkbook(input);  
            else  
                wb = new HSSFWorkbook(input);    
           
            FormulaEvaluator eval=null;        
            if(wb instanceof HSSFWorkbook)  
                eval=new HSSFFormulaEvaluator((HSSFWorkbook) wb);  
            else if(wb instanceof XSSFWorkbook)  
                eval=new XSSFFormulaEvaluator((XSSFWorkbook) wb);  
            
            Sheet sheet = null;
            if(sheetName == null || sheetName.length() == 0 )
                sheet = wb.getSheetAt(0);     //获得第一个表单  
            else
            {
                int sheetcount = wb.getNumberOfSheets();
                for(int index=0;index < sheetcount ;index++)
                {
                    if(wb.getSheetAt(index).getSheetName().equals(sheetName))
                    {
                        sheet = wb.getSheetAt(index);
                        break;
                    }
                }
                    
            }
       //     System.out.println(sheet.getSheetName());
            
            
            Iterator<Row> rows = sheet.rowIterator(); //获得第一个表单的迭代器  
            DecimalFormat df = new DecimalFormat("0");  
            while (rows.hasNext()) {  
                ArrayList<String> strDate = new ArrayList<String>();
                Row row = rows.next();  //获得行数据  
           //     System.out.println("Row #" + row.getRowNum());  //获得行号从0开始  
                Iterator<Cell> cells = row.cellIterator();    //获得第一行的迭代器  
                while (cells.hasNext()) {  
                    Cell cell = cells.next();  
                    String value = "";
                    switch (cell.getCellType()) {   //根据cell中的类型来输出数据  
                    case HSSFCell.CELL_TYPE_NUMERIC:    
                        value =  String.valueOf(df.format(cell.getNumericCellValue()));
                        break;  
                    case HSSFCell.CELL_TYPE_STRING:  
                        value = cell.getStringCellValue();
                        break;  
                    case HSSFCell.CELL_TYPE_BOOLEAN:  
                        value = String.valueOf(cell.getBooleanCellValue()); 
                        break;  
                    case HSSFCell.CELL_TYPE_FORMULA:  
                        eval.evaluateFormulaCell(cell);      
                        value = String.valueOf(df.format(cell.getNumericCellValue()));
                        break;  
                    default:  
                        System.out.println("unsuported sell type");  
                        break;  
                    }  
                    value = StringUtils.subZeroAndDot(value);
                    if(value.length() > 0)
                        strDate.add(value); 
                }  
                if(strDate != null && strDate.size() >0)
                {
                /*  System.out.println("-----------------------"+strDate);
                    System.out.println("-----------------------"+strDate.size());*/
                    list.add(strDate);
                }
            }  
        } catch (IOException ex) {  
            ex.printStackTrace();  
        } 
        return list;
    }
    
    
    /***
     * 得到Excel，并解析内容  对2007及以上版本 使用XSSF解析 
     * @param fileName   文件名
     * @param sheetName  sheetName
     * @return
     */
    public static HashMap<String,HashMap<String,String>>  readXlsForMap(String fileName,String sheetName){  
        HashMap<String,HashMap<String,String>> map = new HashMap<String,HashMap<String,String>>();
        ArrayList<ArrayList<String>> list  = readXls(fileName,sheetName);
        
        ArrayList<String> colNameList = list.get(0);
        list.remove(0);
        for(ArrayList<String> dataList : list)
        {
            HashMap<String,String> dataMap = new HashMap<String,String>();
            for(int index=1;index<dataList.size();index++)
            {
                dataMap.put(colNameList.get(index), dataList.get(index));
            }
            map.put(dataList.get(0), dataMap);
        }
        return map;
    }
   
}