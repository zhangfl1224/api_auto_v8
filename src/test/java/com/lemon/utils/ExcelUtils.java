package com.lemon.utils;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lemon.pojo.CaseInfo;
import com.lemon.pojo.MappingDemo;
import com.lemon.pojo.WriteBackData;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Project: api_auto_v2
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: 2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: luojie
 * @Create: 2020-09-05 10:18
 * @Desc： excel工具类
 **/
public class ExcelUtils {

    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream(Constants.EXCEL_PATH);
        //2、创建easypoi导入参数对象
        ImportParams params = new ImportParams();
        //从第几个sheet开始
        params.setStartSheetIndex(0);
        //从sheetIndex开始每次读取几个sheet
        params.setSheetNum(1);
//        importExcel(excel文件流对象，excel映射实体类.class，easypoi导入参数)
        List<CaseInfo> caseInfoList = ExcelImportUtil.importExcel(fis, CaseInfo.class, params);
        for (CaseInfo caseInfo : caseInfoList) {
            System.out.println(caseInfo);
        }
    }

    //回写数据集合
    public static List<WriteBackData> wbdList = new ArrayList<>();

    /**
     * 数据驱动
     * @param startSheetIndex           从第几个sheet开始
     * @param sheetNum                  从sheetIndex开始每次读取几个sheet
     * @return
     * @throws Exception
     */
    public static Object[] read(int startSheetIndex,int sheetNum) throws Exception {
        //1、加载excel文件
        FileInputStream fis = new FileInputStream(Constants.EXCEL_PATH);
        //2、创建easypoi导入参数对象
        ImportParams params = new ImportParams();
        //从第几个sheet开始
        params.setStartSheetIndex(startSheetIndex);
        //从sheetIndex开始每次读取几个sheet
        params.setSheetNum(sheetNum);
//        importExcel(excel文件流对象，excel映射实体类.class，easypoi导入参数)
        List<CaseInfo> caseInfoList = ExcelImportUtil.importExcel(fis, CaseInfo.class, params);
        fis.close();
        return caseInfoList.toArray();
    }

    /**
     * 批量回写
     */
    public static void batchWrite() throws Exception {
        //excel批量回写操作
        //1、读取execl中内容
        FileInputStream fis = new FileInputStream(Constants.EXCEL_PATH);
        Workbook sheets = WorkbookFactory.create(fis);
        //2、循环遍历wbdList集合
        for (WriteBackData writeBackData : wbdList) {
            int sheetIndex = writeBackData.getSheetIndex();
            int rowNum = writeBackData.getRowNum();
            int cellNum = writeBackData.getCellNum();
            String content = writeBackData.getContent();
            //获取sheet
            Sheet sheet = sheets.getSheetAt(sheetIndex);
            //获取row
            Row row = sheet.getRow(rowNum);
            //获取cell
            Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellType(CellType.STRING);
            //回写内容
            cell.setCellValue(content);
        }
        //3、回写excel
        FileOutputStream fos = new FileOutputStream(Constants.EXCEL_PATH);
        sheets.write(fos);
        fis.close();
        fos.close();
    }
}
