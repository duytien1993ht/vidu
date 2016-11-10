package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bean.User;


public class UserDAO {
	private Connection con = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private User user = null;
	
	public void insertUser(String first,String last,String gender,boolean verified,byte[] userImage){
		con = SQLConnection.getConnection();
		String sql = "insert into bang1(first,last,gender) values(?,?,?)";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, first);
			pstmt.setString(2, last);
			pstmt.setString(3, gender);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
}
