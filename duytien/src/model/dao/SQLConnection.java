package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.microsoft.sqlserver.jdbc.SQLServerException;

public class SQLConnection {
	private static Connection con = null;

	/**
	 * <h1>getConnection</h1>Mo ket noi den co so du lieu.
	 * 
	 * @return con Connection
	 * @throws ClassNotFoundException
	 *             Khong tim duoc driver
	 * @throws SQLException
	 *             Khong the mo duoc ket noi.
	 */
	public static Connection getConnection() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager
					.getConnection("jdbc:sqlserver://localhost; database=QLNhaXe;integratedSecurity=True;");
			System.out.println("Connect sucess");
		} catch (ClassNotFoundException e) {
			System.err.println("Can not get SQLServerDriver class !");
			e.printStackTrace();
		} catch (SQLServerException e) {
			// TODO Auto-generated catch block
			System.out.println("Khong ket noi voi database");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Khong the mo duoc ket noi");
			e.printStackTrace();
		}
		return con;
	}

	// Dong ket noi
	public static void closeConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			System.err.println("Can not close connection !");
			e.printStackTrace();
		}
	}

	public static void closeResultSet(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			System.err.println("Can not close ResultSet !");
			e.printStackTrace();
		}
	}

	public static void closePrepareStatement(PreparedStatement pstmt) {
		try {
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			System.err.println("Can not close PreparedStatement  !");
			e.printStackTrace();
		}
	}
}
