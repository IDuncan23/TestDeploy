package com.revature.delegate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dao.EmployeeDao;
import com.revature.dao.EmployeeDaoImpl;
import com.revature.dao.LoginDao;
import com.revature.dao.LoginDaoImpl;
import com.revature.dao.RequestDao;
import com.revature.dao.RequestDaoImpl;
import com.revature.model.Employee;
import com.revature.model.Login;
import com.revature.model.Request;
import com.revature.util.MD5;

public class HomeDelegate {
	
private final ObjectMapper mapper = new ObjectMapper();	
public RequestDao rd = new RequestDaoImpl();	
public EmployeeDao ed = new EmployeeDaoImpl();
public LoginDao ld = new LoginDaoImpl();

	//depending on whether you're a employee or manager, redirect to the right home page
	public void goHome(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// Get our session information
		HttpSession session = req.getSession();
		// Give a personalized response
		Login login = (Login) session.getAttribute("user");
		Employee employeeDetails = (Employee) session.getAttribute("currentUser");

		if (login == null) {
			resp.sendRedirect("login");
		} else {
			if(employeeDetails.getIsManager().equals("Y")) {
				try {
					req.getRequestDispatcher("static/managerhome.jsp").forward(req,resp);
				} catch (ServletException e) {
					e.printStackTrace();
				}
			} else {
				try {
					req.getRequestDispatcher("static/employeehome.jsp").forward(req,resp);
				} catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
		}
	}
	
/**********************************************************************
 * EMPLOYEE METHODS
 **********************************************************************/
	
	//employee can submit a reimbursement request
	public void addExpense(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// Get our session information
		HttpSession session = req.getSession();
		Employee employeeDetails = (Employee) session.getAttribute("currentUser");
		
		//get form parameters
		String expenseTypeParam = req.getParameter("type");
		String expenseDescParam = req.getParameter("description");
		String expenseAmountParam = req.getParameter("amount");
		Double expAmntParamToDbl = Double.parseDouble(expenseAmountParam);
		//get employee id from session
		int employeeId = employeeDetails.getEmpId();
		//System.out.println(employeeId + " addExpense() => employeeId");//test
		//insert request into database
		boolean expenseAdded = rd.addExpenseRequest(employeeId, expenseTypeParam, expenseDescParam, expAmntParamToDbl);
		if(expenseAdded) {
			//System.out.println("HomeDelegate => addExpense() => expenseAdded returns true!");//test
			try {
				req.getRequestDispatcher("static/employeehome.jsp").forward(req, resp);
			} catch (ServletException e) {
				e.printStackTrace();
			}
		} else {
			//System.out.println("HomeDelegate => addExpense() => expenseAdded returns false!");//test
			try {
				req.getRequestDispatcher("static/employeehome.jsp").forward(req, resp);
			} catch (ServletException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	//employee can get all of their resolved requests
	public void getResolvedRequests(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		System.out.println("Get pending requests called");
		// Get our session information
		HttpSession session = req.getSession();
		Employee employeeDetails = (Employee) session.getAttribute("currentUser");
		
		List<Request> resolvedRequests = new ArrayList<>();
		resolvedRequests = rd.getResolvedRequests(employeeDetails.getEmpId());
		resp.setContentType("application/json");
		resp.getWriter().append(mapper.writeValueAsString(resolvedRequests));
		//System.out.println(resolvedRequests);//test
	}
	
	//employee can get all of their pending requests
	public void getPendingRequests(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		System.out.println("Get pending requests called");
		// Get our session information
		HttpSession session = req.getSession();
		Employee employeeDetails = (Employee) session.getAttribute("currentUser");
		
		List<Request> unresolvedRequests = new ArrayList<>();
		unresolvedRequests = rd.getUnresolvedRequests(employeeDetails.getEmpId());
		resp.setContentType("application/json");
		resp.getWriter().append(mapper.writeValueAsString(unresolvedRequests));
		//System.out.println(unresolvedRequests);//test
	}
	
	//update employee profile
	public void updateProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//System.out.println("updateProfile method in HD called!");//test
		// Get our session information
		HttpSession session = req.getSession();
		Employee employeeDetails = (Employee) session.getAttribute("currentUser");
		Login login = (Login) session.getAttribute("user");
		
		//updated employee details objects
		Login updatedLogin = new Login();
		Employee updatedEmpDetails = new Employee();
		
		int employeeId = employeeDetails.getEmpId();
		String currentUsername = login.getUsername();
		String usernameParam = req.getParameter("username");
		String passwordParam = req.getParameter("password");
		//System.out.println("------------PASSWORD FROM FORM: " + passwordParam + " PASSWORD STORED IN SESSION: " + login.getPassword());//test
		if(!passwordParam.equals(login.getPassword())) {
			passwordParam = MD5.getMd5(passwordParam);
		}
		//System.out.println("------AFTER------PASSWORD FROM FORM: " + passwordParam + " PASSWORD STORED IN SESSION: " + login.getPassword());//test		
		String firstnameParam = req.getParameter("firstname");
		String lastnameParam = req.getParameter("lastname");
		String emailParam = req.getParameter("email");
		//System.out.println("username: " + usernameParam + " password: " + passwordParam + " firstname: " + firstnameParam + " lastname: " + lastnameParam + " email: " + emailParam); //test  
		boolean employeeUpdated = ed.updateEmployee(employeeId, usernameParam, firstnameParam, lastnameParam, emailParam);
		boolean loginUpdated = ed.updateLogin(currentUsername, usernameParam, passwordParam);
		
		if(employeeUpdated || loginUpdated) {
			updatedLogin.setUsername(usernameParam);
			updatedLogin.setPassword(passwordParam);
			updatedEmpDetails.setEmpId(employeeDetails.getEmpId());
			updatedEmpDetails.setUsername(usernameParam);
			updatedEmpDetails.setFirstname(firstnameParam);
			updatedEmpDetails.setLastname(lastnameParam);
			updatedEmpDetails.setEmail(emailParam);
			updatedEmpDetails.setIsManager(employeeDetails.getIsManager());
			session.setAttribute("user", updatedLogin);
			session.setAttribute("currentUser", updatedEmpDetails);
			try {
				req.getRequestDispatcher("static/employeehome.jsp").forward(req, resp);
			} catch (ServletException e) {
				e.printStackTrace();
			}
		} else {
			try {
				req.getRequestDispatcher("static/employeehome.jsp").forward(req, resp);
			} catch (ServletException e) {
				e.printStackTrace();
			}
		}
		
	}
	
/**********************************************************************
 * MANAGER METHODS
 **********************************************************************/
	
	//manager can view all employees
	public void viewAllEmployees(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		List<Employee> allEmployees = ed.getAllEmployees();
		resp.setContentType("application/json");
		resp.getWriter().append(mapper.writeValueAsString(allEmployees));
	}
	
	//manager can view all currently pending requests from all employees
	public void viewAllPendingRequests(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		List<Request> allPendingRequests = ed.getAllPendingRequests();
		resp.setContentType("application/json");
		resp.getWriter().append(mapper.writeValueAsString(allPendingRequests));
	}
	
	//manager can view all resolved requests from all employees
	public void viewAllResolvedRequests(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		List<Request> allResolvedRequests = ed.getAllResolvedRequests();
		resp.setContentType("application/json");
		resp.getWriter().append(mapper.writeValueAsString(allResolvedRequests));
	}
	
	//manager can create an employee account
	public void createEmployee(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		password = MD5.getMd5(password);
		String firstname = req.getParameter("firstname");
		String lastname = req.getParameter("lastname");
		String email = req.getParameter("email");
		String manager = req.getParameter("manager");
		
		boolean empWasCreated = ed.createEmployee(new Employee(firstname, lastname, username, email, manager));
		
		if(empWasCreated) {
			System.out.println("empWasCreated returned " + empWasCreated);
			boolean loginWasCreated = ld.createLogin(new Login(username, password));
			try {
				req.getRequestDispatcher("static/managerhome.jsp").forward(req, resp);
			} catch (ServletException e) {
				e.printStackTrace();
			}
		} else {
			try {
				req.getRequestDispatcher("static/managerhome.jsp").forward(req, resp);
			} catch (ServletException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//manager can create an employee account
	public void viewAllRequests(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		List<Request> allRequests = ed.getAllRequests();
		resp.setContentType("application/json");
		resp.getWriter().append(mapper.writeValueAsString(allRequests));
		
	}
	
	
}
