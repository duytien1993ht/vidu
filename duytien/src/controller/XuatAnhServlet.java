package controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class XuatAnhServlet
 */
public class XuatAnhServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public XuatAnhServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 String connectionURL = "jdbc:mysql://localhost:3306/test";
		    Connection con=null;
		    try{
		        Class.forName("com.mysql.jdbc.Driver").newInstance();
		        /*
		        Database connection, database id is root and password is blank
		        */
		        con=DriverManager.getConnection(connectionURL,"root","");
		        Statement st1=con.createStatement();
		        /*
		        select the image from the picture table    .
		        */
		        ResultSet rs1 = st1.executeQuery("select image from picture where id=1");
		        if(rs1.next()){
		            int len = rs1.getString(1).length();
		            byte [] rb = new byte[len];

		            //retrieving image in binary format
		            InputStream readImg = rs1.getBinaryStream(1);
		            int index=readImg.read(rb, 0, len);
		            System.out.println("index: "+index+" rb = "+rb+ " readImg= "+readImg);
		            st1.close();

		            response.reset();
		            response.setContentType("image/jpg");
		            response.getOutputStream().write(rb,0,len);
		            response.getOutputStream().flush();
		        }
		    }
		    catch (Exception e){
		      e.printStackTrace();
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
