package controllers;
import java.sql.*;


public class SQLConnection {
	
	public static String driver = "com.mysql.jdbc.Driver";
	public static String username = "root";
	public static String password = "";
	public static String dbName = "resume_builder";
	public static java.sql.Connection conn = null;
	
	public static java.sql.Connection GetConnection() throws ClassNotFoundException, SQLException{
		Class.forName(driver);
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbName,username,password);
		return conn;
	}
	
}
