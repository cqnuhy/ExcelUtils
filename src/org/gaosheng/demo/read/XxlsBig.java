package org.gaosheng.demo.read;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.gaosheng.util.xls.reader.XxlsAbstract;


public class XxlsBig extends XxlsAbstract {
	public static void main(String[] args) throws Exception {
		XxlsBig howto = new XxlsBig("temp_major");
		howto.processOneSheet("E:/stand_major.xlsx",1);
//		howto.process("F:/new.xlsx");
		File file = new File("F:/new.xlsx");
		file.delete();
	}
	
	public XxlsBig(String tableName) throws SQLException{
		this.conn = getNew_Conn();
		this.statement = conn.createStatement();
		this.tableName = tableName;
	}

	private Connection conn = null;
	private Statement statement = null;
	private PreparedStatement newStatement = null;

	private String tableName = "temp_table";
	private boolean create = true;
	
	public void optRows(int sheetIndex,int curRow, List<String> rowlist) throws SQLException {
		if (sheetIndex == 0 && curRow == 0) {
			StringBuffer preSql = new StringBuffer("insert into " + tableName
					+ " values(");
			StringBuffer table = new StringBuffer("create table " + tableName
					+ "(");
			int c = rowlist.size();
			for (int i = 0; i < c; i++) {
				preSql.append("?,");
				table.append(rowlist.get(i));
				table.append("  varchar2(100) ,");
			}

			table.deleteCharAt(table.length() - 1);
			preSql.deleteCharAt(preSql.length() - 1);
			table.append(")");
			preSql.append(")");
			if (create) {
				statement = conn.createStatement();
				try{
					statement.execute("drop table "+tableName);
				}catch(Exception e){
					
				}finally{
					System.out.println("表 "+tableName+" 删除成功");
				}
				if (!statement.execute(table.toString())) {
					System.out.println("创建表 "+tableName+" 成功");
					// return;
				} else {
					System.out.println("创建表 "+tableName+" 失败");
					return;
				}
			}
			conn.setAutoCommit(false);
			newStatement = conn.prepareStatement(preSql.toString());

		} else if(curRow>0) {
			// 一般行
			int col = rowlist.size();
			for (int i = 0; i < col; i++) {
				newStatement.setString(i + 1, rowlist.get(i).toString());
			}
			newStatement.addBatch();
			if (curRow % 50 == 0) {
				System.out.println("已导入 "+curRow+" 条数据");
				newStatement.executeBatch();
				conn.commit();
			}
		}
	}
	
    private static Connection getNew_Conn() {
        Connection conn = null;
        Properties props = new Properties();
        FileInputStream fis = null;

        try {
            fis = new FileInputStream("database.properties");
            props.load(fis);
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            // String jdbcURLString =
            // "jdbc:oracle:thin:@192.168.0.28:1521:orcl";
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
        return conn;
    }
    
	public int close() {
		try {
			newStatement.executeBatch();
			conn.commit();
			System.out.println("数据写入完毕");
			this.newStatement.close();
			this.statement.close();
			this.conn.close();
			return 1;
		} catch (SQLException e) {
			return 0;
		}
	}

	public boolean isCreate() {
		return create;
	}

	public void setCreate(boolean create) {
		this.create = create;
	}
}
