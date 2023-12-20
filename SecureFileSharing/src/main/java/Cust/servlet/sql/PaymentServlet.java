package Cust.servlet.sql;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.servlet.sql.ConnectDB;

@WebServlet("/PaymentServlet")
public class PaymentServlet extends HttpServlet
{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String payerName = req.getParameter("payerName");
	    String cardNumber = req.getParameter("cardNumber");
	    String expirationDate = req.getParameter("expirationDate");
	    String cvc = req.getParameter("cvc");
	    double amount = Double.parseDouble(req.getParameter("amount"));
	    Date paymentDate = new Date();
	    
	    Connection con = ConnectDB.dB();
	    String query = "insert into payment values(?,?,?,?,?,?)";
	    try 
	    {
			PreparedStatement pst = con.prepareStatement(query);
			
			pst.setString(1, payerName);
			pst.setString(2, cardNumber);
			pst.setString(3, expirationDate);
			pst.setString(4, cvc);
			pst.setDouble(5, amount);
			pst.setTimestamp(6, new java.sql.Timestamp(paymentDate.getTime()));

			pst.executeUpdate();
			
			 resp.setContentType("text/html");
	         PrintWriter out = resp.getWriter();
	         out.println("<html><body>");
	         out.println("<h2>Payment Successful!</h2>");
	         out.println("<p>Amount: " + amount + "</p>");
	         out.println("<p>Name: " + payerName + "</p>");
	         out.println("<p>Date: " + paymentDate + "</p>");
	         out.println("</body></html>");
		} 
	    catch (SQLException e) 
	    {
			e.printStackTrace();
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing payment");
		}
	}
	 
	
}
