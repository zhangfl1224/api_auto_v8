package com.lemon.utils;

/**
 * @Project: api_auto_v3
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: 2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: luojie
 * @Create: 2020-09-08 20:50
 * @Desc： 常量类
 **/
public class Constants {

    //用例文件地址
    public static final String EXCEL_PATH = "src/test/resources/cases_v3.xlsx";

    //柠檬班特殊头
    public static final String X_LEMONBAN_MEDIA_TYPE = "lemonban.v2";

    //响应体回写列号
    public static final int RESPONSE_CELLNUM = 8;

    //断言结果回写列号
    public static final int ASSERT_CELLNUM = 10;

    //jdbcurl
    public static final String JDBC_URL = "jdbc:mysql://api.lemonban.com:3306/futureloan?useUnicode=true&characterEncoding=utf-8";
    //jdbcuser
    public static final String JDBC_USER = "future";
    //jdbcpassword
    public static final String JDBC_PASSWORD = "123456";

}
