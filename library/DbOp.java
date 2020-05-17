package library;

import java.sql.*;
import javax.swing.JOptionPane;


public class DbOp{
    // MySQL 8.0 以上版本 - JDbOpC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/library?useSSL=false&serverTimezone=UTC";

    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
	static final String PASS = "asdddf2";

	private static Connection conn=null;
	private static Statement stmt = null;

	public DbOp(){
		try{
			if(conn==null){
				// 注册 JDBC 驱动
				Class.forName(JDBC_DRIVER);

				// 打开连接
				System.out.println("connecting database...");
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				if(conn != null){
					System.out.println("database connected");
				}
			}
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,"database connection error");
			System.out.println(e.getMessage());
		}
	}

	public static ResultSet query(String sql){
		ResultSet rs=null;

		try{
			stmt = conn.createStatement();
			if(conn==null){
				new DbOp();
			}
			rs = stmt.executeQuery(sql);
		}catch(SQLException e){
			JOptionPane.showMessageDialog(null,"query execution error");
			System.out.println(e);
			rs=null;
		}
		return rs;
	}

	public static int update(String sql){
			int a=0;
		try{
			if(conn==null){
				new DbOp();
			}
			a= conn.createStatement().executeUpdate(sql);
		}catch(SQLException e){
			JOptionPane.showMessageDialog(null,"update error"+e);			
			a= -1;
		}
		return a;
	}

	public static void close(){
		try{
			if(conn!=null){
				
				conn.close();
				conn=null;
				//JOptionPane.showMessageDialog(null,"database connection closed");
			}
		}catch(SQLException e){
			JOptionPane.showMessageDialog(null,"connection close error");
		}
	}
	public static void main(String[] args){
		new DbOp();
		String sql = "select * from book";
		try{
		ResultSet rs = DbOp.query(sql);
		while (rs.next()) {
			// 通过字段检索
			int id = rs.getInt("id");
			String bookname = rs.getString("bookname");
			String author = rs.getString("author");

			// 输出数据
			System.out.print("ID: " + id);
			System.out.print(", 名称: " + bookname);
			System.out.print(", author: " + author);
			System.out.print("\n");
		}

		sql = "insert into book(id, bookname, bookType, author, translator, publisher, publish_time, price, page)";
		sql = sql + "values(2, 'Ale', 'Science', 'qingtai', 'qingtai', 'Xinhua', '2020-05', 213, 998)";
		System.out.println("update return: " +DbOp.update(sql));

		rs = DbOp.query("select * from book");
		while (rs.next()) {
			// 通过字段检索
			int id = rs.getInt("id");
			String bookname = rs.getString("bookname");
			String author = rs.getString("author");

			// 输出数据
			System.out.print("ID: " + id);
			System.out.print(", 名称: " + bookname);
			System.out.print(", author: " + author);
			System.out.print("\n");
		}

	} catch (SQLException se) {
		// 处理 JDbOpC 错误
		se.printStackTrace();
	} catch (Exception e) {
		// 处理 Class.forName 错误
		e.printStackTrace();
	} 
	}
}