package com.lottery.db;

import java.sql.Connection;
import java.sql.DriverManager;


/**
 * Created By:Anuruddhika Mahanama
 * Date :2019/06/01
 * For: To Define the database connection details
 */

public class DBConnection {

    /**
     * To Create Db connection
     *
     * @return
     * @throws Exception
     */
    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "dlb_numbers";//"consignment_data";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "";

        Class.forName(driver).newInstance();
        Connection conn = DriverManager.getConnection(url + dbName, userName, password);
        return conn;
    }

    /**
     * To Close Db connection     *
     */
    public static void closeConnection(Connection conn) {
        try {
            if(conn != null)
                conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
