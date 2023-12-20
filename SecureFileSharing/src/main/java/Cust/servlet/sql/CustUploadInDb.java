package Cust.servlet.sql;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.servlet.sql.ConnectDB;

@WebServlet("/CustFileUpload")
@MultipartConfig
public class CustUploadInDb extends HttpServlet 
{
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
        	String encryptionKey =request.getParameter("secretKey");
            Part filePart = request.getPart("pdfFile");
            InputStream fileContent = filePart.getInputStream();

            byte[] pdfBytes = fileContent.readAllBytes();
            byte[] encryptedPdfBytes = encryptPdf(pdfBytes,encryptionKey);

            saveToDatabase(encryptionKey,encryptedPdfBytes);

            response.getWriter().println("File uploaded and encrypted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error uploading and encrypting the file.");
        }
    }

    private byte[] encryptPdf(byte[] pdfBytes,String encryptionKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    	// Initialize cipher for encryption
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKey = new SecretKeySpec(encryptionKey.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        
     // Encrypt PDF bytes
        byte[] encryptedPdfBytes = cipher.doFinal(pdfBytes);
        
        return encryptedPdfBytes;
    }

    private void saveToDatabase(String encryptionKey,byte[] encryptedPdfBytes) throws SQLException {
        Connection con = ConnectDB.dB();
        String query ="insert into document values(?,?)";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, encryptionKey);
        pst.setBytes(2, encryptedPdfBytes);
        pst.execute();
    }
}
