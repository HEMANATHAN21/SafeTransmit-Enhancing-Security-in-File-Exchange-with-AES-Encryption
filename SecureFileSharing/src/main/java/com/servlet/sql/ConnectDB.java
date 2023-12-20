package com.servlet.sql;
import java.sql.*;

public class ConnectDB 
{
	//Connection conn = null;
	public static Connection dB()
	{
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/practice?user=root&password=solohema");
			return conn;
		} 
		catch (ClassNotFoundException | SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
		
	}

}