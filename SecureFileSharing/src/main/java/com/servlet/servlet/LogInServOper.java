package com.servlet.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import com.servlet.sql.*;

import javax.servlet.GenericServlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
@WebServlet("/login")
public class LogInServOper extends GenericServlet{

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException 
	{
		String userName = req.getParameter("uname");
		String password = req.getParameter("upass");
		
		Login log = new Login();
		log.setUserName(userName);
		log.setPassword(password);
		
		boolean value = Login.logIn();
		
		if(value == true)
		{
			PrintWriter pw = res.getWriter();
			pw.print("Login Successfull");
			RequestDispatcher dispatcher = req.getRequestDispatcher("/display.jsp");
			dispatcher.forward(req, res);
		}
		else
		{
			PrintWriter pw = res.getWriter();
			pw.print("OOPS!!");
		}
		
	}

}
