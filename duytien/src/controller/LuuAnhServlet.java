package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LuuAnhServlet
 */
public class LuuAnhServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LuuAnhServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter pw = response.getWriter();
        /*
        Database connection, database name is test
        */
        String connectionURL = "jdbc:mysql://localhost:3306/test";
        Connection con=null;
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            /*
            Database connection, database id is root and password is blank
            */
            con = DriverManager.getConnection(connectionURL, "root", "");
            PreparedStatement ps = con.prepareStatement("INSERT INTO picture(image) VALUES(?)");
            /*
            inserting image in  pictures table*/
            File file = new File("F:\\so3.jpg");

            FileInputStream fs = new FileInputStream(file);

            ps.setBinaryStream(1,fs,fs.available());
            int i = ps.executeUpdate();
             System.out.println(i);
            if(i!=0){
              pw.println("image inserted successfully");
            }
            else{
              pw.println("problem in image insertion");
            }
        }
        catch (Exception e){
        System.out.println(e);
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
