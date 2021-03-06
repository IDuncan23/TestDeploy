package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.model.Employee;
import com.revature.model.Login;
import com.revature.util.ConnectionUtil;
import com.revature.util.MD5;

public class LoginDaoImpl implements LoginDao{

	
	@Override
	public Login login(String username, String password) {
		Login login = null;
		
		password = MD5.getMd5(password);
		System.out.println("login() method in LoginDaoImpl hashed password is " + password);
		String sql = "SELECT username, password FROM login WHERE username = ? AND password =?";
		
	
		try (Connection conn = ConnectionUtil.getConnection()){
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,username);
			pstmt.setString(2,password);
			
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				login = new Login(rs.getString("username"),
						rs.getString("password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return login;
	}
	
	@Override 
	public Employee getEmpDetails(String username) {
		Employee empId = null;
		
		String sql = "SELECT * FROM employees WHERE username = ?";
	
		try (Connection conn = ConnectionUtil.getConnection()){
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,username);
			
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				empId = new Employee(rs.getInt("employee_id"),rs.getString("firstname"),rs.getString("lastname"),
						rs.getString("username"),rs.getString("email"),rs.getString("is_manager"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return empId;
	}

	@Override
	public boolean createLogin(Login login) {

		String sql = "INSERT INTO login(username, password) VALUES(?,?)";
		
		try (Connection conn = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, login.getUsername());
			ps.setString(2, login.getPassword());
			
			if(ps.executeUpdate() == 1) {
				return true;
			} else {
				//throw exception here!
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
