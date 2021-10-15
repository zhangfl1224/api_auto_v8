package com.lemon.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @Project: api_auto_v5
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: 2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: luojie
 * @Create: 2020-09-12 10:47
 * @Desc： jdbc工具类
 **/
public class JDBCUtils {

    public static Connection getConnection() {
        //定义数据库连接
        //jdbc:oracle:thin:@localhost:1521:DataBaseName
        String url = Constants.JDBC_URL;
        String user = Constants.JDBC_USER;
        String password = Constants.JDBC_PASSWORD;
        //定义数据库连接对象
        Connection conn = null;
        try {
            //你导入的数据库驱动包， mysql。
            conn = DriverManager.getConnection(url, user,password);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void close(Connection conn) {
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
