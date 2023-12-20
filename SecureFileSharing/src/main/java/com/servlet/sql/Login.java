package com.servlet.sql;
import java.sql.*;

public class Login 
{
	static String userName;
	static String password;
	
	static public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	static public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	static public boolean logIn()
	{
		Connection conn = null;
		
		conn = ConnectDB.dB();
		try 
		{
			
			String query = "select * from login where username=? and password=?";
			PreparedStatement pst = conn.prepareStatement(query);
			
			pst.setString(1, Login.getUserName());
			pst.setString(2, Login.getPassword());
			
			ResultSet rs = pst.executeQuery();
			
			if(rs.next())
			{
				return true;
			}
			else
			{
				return false;
			}
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return false;
		}
	}

}
