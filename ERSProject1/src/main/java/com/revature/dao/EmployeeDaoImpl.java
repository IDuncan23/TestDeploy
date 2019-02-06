package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.model.Employee;
import com.revature.model.Request;
import com.revature.util.ConnectionUtil;

import oracle.jdbc.proxy.annotation.Pre;

public class EmployeeDaoImpl implements EmployeeDao {

	@Override
	public List<Request> getEmployeeResolved(Employee emp) {
		
		List<Request> employeeResolvedRequests = new ArrayList<>();
		//String sql = "SELECT * FROM requests WHERE"
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateEmployee(int employeeId, String username, String firstname, String lastname,
			String email) {
		
		System.out.println("updateProfile IN EmployeeDaoImpl =>\n emplpoyee id: " + employeeId + " username: " + username
				 + " firstname: " + firstname + " lastname: " + lastname + " email: " + email);
		
		String sql = "UPDATE employees SET username = ?, firstname  = ?, lastname = ?, email = ?" +
		" WHERE employee_id = ?";
		
		try (Connection conn = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username); ps.setString(2, firstname); 
			ps.setString(3, lastname); ps.setString(4, email); ps.setInt(5, employeeId);
			if(ps.executeUpdate() == 1) {
				return true;
			} else {
				//throw exception here
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean updateLogin(String currentUsername, String username, String password) {

		System.out.println("updateLogin IN EmployeeDaoImpl =>\n current username: " + currentUsername +" username: " + username + " password: " + password);
		
		System.out.println("updateLogin() method in EmployeeDaoImpl hashed password is " + password);

		String sql = "UPDATE login SET username = ?, password  = ? WHERE username = ?";
		
		try (Connection conn = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username); ps.setString(2, password); ps.setString(3, currentUsername);  
			if(ps.executeUpdate() == 1) {
				return true;
			} else {
				//throw exception here
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	//mangager function to get all employees
	@Override
	public List<Employee> getAllEmployees() {
		//create empty array list to store results
		List<Employee> allEmployees = new ArrayList<>();
		
		String sql = "SELECT * FROM employees ORDER BY employee_id ASC";
		
		try(Connection conn = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet results = ps.executeQuery();
			while(results.next()) {
				allEmployees.add(new Employee(results.getInt("employee_id"), results.getString("firstname"),
						results.getString("lastname"), results.getString("username"), results.getString("email"),
						results.getString("is_manager")));
			}
			return allEmployees;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	//manager function to get all pending requests
	@Override
	public List<Request> getAllPendingRequests() {

		//create empty array list to store results
		List<Request> allPendingRequests = new ArrayList<>();
		
		String sql = "SELECT * FROM requests WHERE status = 'PENDING' ORDER BY request_id ASC";
		
		try(Connection conn = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet results = ps.executeQuery();
			while(results.next()) {
				allPendingRequests.add(new Request(results.getInt("request_id"),results.getInt("employee_id"), results.getString("type"),
						results.getString("description"), results.getDouble("amount"), results.getString("status"),
						results.getString("date_submitted")));
			}
			return allPendingRequests;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<Request> getAllResolvedRequests() {

		//create empty array list to store results
		List<Request> allResolvedRequests = new ArrayList<>();
		
		String sql = "SELECT * FROM requests WHERE status IN('APPROVED', 'DECLINED') ORDER BY request_id DESC";
		
		try(Connection conn = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet results = ps.executeQuery();
			while(results.next()) {
				allResolvedRequests.add(new Request(results.getInt("request_id"),results.getInt("employee_id"), results.getString("type"),
						results.getString("description"), results.getDouble("amount"), results.getString("status"),
						results.getString("date_submitted"), results.getString("date_resolved"), results.getInt("resolved_by_id")));
			}
			return allResolvedRequests;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<Request> getAnEmployeesRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean resolveEmployeeRequest() {
		// TODO Auto-generated method stub
		return false;
	}

	//manager method to create a new employee record
	@Override
	public boolean createEmployee(Employee emp) {
		System.out.println("createEmployee method triggered!");
		boolean employeeExists = false;
		
		try (Connection conn = ConnectionUtil.getConnection()){
			System.out.println("createEmployee first try-catch triggered");
			String sql = "SELECT username FROM employees WHERE username = ?";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, emp.getUsername());
			ResultSet results = ps.executeQuery();
			
			if(results.next()) {
				employeeExists = true;
			} 
			System.out.println("employeeExists is " + employeeExists);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(!employeeExists) {
			
			try (Connection conn = ConnectionUtil.getConnection()){
				System.out.println("createEmployee second try-catch triggered!");
				String sql2 = "INSERT INTO employees(firstname, lastname, username, email, is_manager) " +
						"VALUES(?,?,?,?,?)";
				
				PreparedStatement ps = conn.prepareStatement(sql2);
				ps.setString(1, emp.getFirstname());
				ps.setString(2, emp.getLastname());
				ps.setString(3, emp.getUsername());
				ps.setString(4, emp.getEmail());
				ps.setString(5, emp.getIsManager());
				
				if(ps.executeUpdate() == 1) {
					return true;
				} else {
					//throw exception here!
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return false;
	}

	@Override
	public List<Request> getAllRequests() {

		//create empty array list to store results
		List<Request> allRequests = new ArrayList<>();
		
		String sql = "SELECT * FROM requests ORDER BY request_id DESC";
		
		try(Connection conn = ConnectionUtil.getConnection()){
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet results = ps.executeQuery();
			while(results.next()) {
				allRequests.add(new Request(results.getInt("request_id"),results.getInt("employee_id"), results.getString("type"),
						results.getString("description"), results.getDouble("amount"), results.getString("status"),
						results.getString("date_submitted"), results.getString("date_resolved"), results.getInt("resolved_by_id")));
			}
			return allRequests;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
