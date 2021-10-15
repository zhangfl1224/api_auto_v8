package com.lemon.pojo;

/**
 * @Project: api_auto_v4
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: 2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: luojie
 * @Create: 2020-09-10 21:38
 * @Desc： excel回写实体类
 **/
public class WriteBackData {

    //回写行号
    private int rowNum;
    //回写列号
    private int cellNum;
    //回写sheetIndex
    private int sheetIndex;
    //回写内容
    private String content;

    public WriteBackData() {
    }

    public WriteBackData(int rowNum, int cellNum, int sheetIndex, String content) {
        this.rowNum = rowNum;
        this.cellNum = cellNum;
        this.sheetIndex = sheetIndex;
        this.content = content;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getCellNum() {
        return cellNum;
    }

    public void setCellNum(int cellNum) {
        this.cellNum = cellNum;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "WriteBackData{" +
                "rowNum=" + rowNum +
                ", cellNum=" + cellNum +
                ", sheetIndex=" + sheetIndex +
                ", content='" + content + '\'' +
                '}';
    }
}
