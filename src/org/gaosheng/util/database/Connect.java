package org.gaosheng.util.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connect {
	private static Connection conn = null;
	
	@SuppressWarnings("unused")
	public static Connection getNew_Conn() {
		if (conn == null){
	        Properties props = new Properties();
	        FileInputStream fis = null;
	
	        try {
	            fis = new FileInputStream("database.properties");
	            props.load(fis);
	            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
	            StringBuffer jdbcURLString = new StringBuffer();
	            jdbcURLString.append("jdbc:oracle:thin:@");
	            jdbcURLString.append(props.getProperty("host"));
	            jdbcURLString.append(":");
	            jdbcURLString.append(props.getProperty("port"));
	            jdbcURLString.append(":");
	            jdbcURLString.append(props.getProperty("database"));
	            conn = DriverManager.getConnection(jdbcURLString.toString(), props
	                    .getProperty("user"), props.getProperty("password"));
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                fis.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
		}
        return conn;
    }
    
	public static int close() {
		try {
			conn.commit();
			System.out.println("数据写入完毕");
			conn.close();
			return 1;
		} catch (SQLException e) {
			return 0;
		}
	}
}
