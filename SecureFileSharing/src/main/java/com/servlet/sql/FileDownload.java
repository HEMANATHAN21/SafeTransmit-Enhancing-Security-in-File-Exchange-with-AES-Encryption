package com.servlet.sql;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.cj.xdevapi.Result;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/seckeyinp")
@MultipartConfig
public class FileDownload extends HttpServlet 
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String encryptionKey = request.getParameter("secretKey");
        try {
            byte[] encryptedPdfBytes = retrieveFromDatabase(encryptionKey);
            byte[] decryptedPdfBytes = decryptPdf(encryptedPdfBytes,encryptionKey);

            response.setContentType("application/pdf");
            //for download
            //response.setHeader("Content-Disposition", "attachment; filename=decrypted.pdf");
            response.setHeader("Content-Disposition", "inline; filename=decrypted.pdf");
            //response.getOutputStream().write(decryptedPdfBytes);
            //response.getWriter().print("<iframe  width="+100+" height="+800+"></iframe>");
            //request.setAttribute("pdffileatr", decryptedPdfBytes);
            //request.getRequestDispatcher("/display.jsp").forward(request, response);
            //response.setContentType("text/html"); // Set content type
            //request.setAttribute("pdffileatr", decryptedPdfBytes);
           // request.getRequestDispatcher("/display.jsp").forward(request, response);
            /*response.setContentType("text/html"); // Set content type to HTML
            request.setAttribute("pdffileatr", decryptedPdfBytes);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/display.jsp");
            dispatcher.forward(request, response);*/
            try (OutputStream out = response.getOutputStream()) {
                out.write(decryptedPdfBytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error downloading the file.");
        }
    }

    private byte[] retrieveFromDatabase(String encryptionKey) throws SQLException {
        byte[] encryptedPdfBytes = null;
        Connection con = ConnectDB.dB();
        String query = "select document from document where securitykey = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, encryptionKey);
        
        ResultSet rs = pst.executeQuery();
        if(rs.next())
        {
        	encryptedPdfBytes = rs.getBytes("document");
        }	

        return encryptedPdfBytes;
    }

    private byte[] decryptPdf(byte[] encryptedPdfBytes,String encryptionKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    	// Initialize cipher for encryption
    	 Cipher cipher = Cipher.getInstance("AES");
         SecretKeySpec secretKey = new SecretKeySpec(encryptionKey.getBytes(), "AES");
         cipher.init(Cipher.DECRYPT_MODE, secretKey);

         // Decrypt encrypted PDF bytes
         byte[] decryptedPdfBytes = cipher.doFinal(encryptedPdfBytes);
        return decryptedPdfBytes;
    }
}

	    