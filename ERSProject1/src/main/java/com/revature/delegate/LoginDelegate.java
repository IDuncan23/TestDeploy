package com.revature.delegate;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revature.dao.EmployeeDao;
import com.revature.dao.LoginDao;
import com.revature.dao.LoginDaoImpl;
import com.revature.model.Employee;
import com.revature.model.Login;
import com.revature.util.MD5;

public class LoginDelegate {
public LoginDao ld = new LoginDaoImpl();
	
	public void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		System.out.println("login method in login delegate triggered!");
		String username = req.getParameter("user");
		String password = req.getParameter("pass");
		Login login = ld.login(username, password);
		if(login == null) {
			resp.sendRedirect("login");
		} else {
			Employee empDetails = ld.getEmpDetails(login.getUsername());
			HttpSession session = req.getSession();
			session.setAttribute("user", login);
			session.setAttribute("currentUser", empDetails);
			Employee employeeDetails = (Employee) session.getAttribute("currentUser");
			if(employeeDetails.getIsManager().equals("Y")) {
				resp.sendRedirect("managerhome");
			} else {
				resp.sendRedirect("home");
			}
			
		}
	}
	
	public void getPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		if(session.getAttribute("user")==null) {
			req.getRequestDispatcher("static/login.html").forward(req,resp);
		} else {
			Employee employeeDetails = (Employee) session.getAttribute("currentUser");
			if(employeeDetails.getIsManager().equals("Y")) {
				resp.sendRedirect("managerhome");
			} else {
				resp.sendRedirect("home");
			}
		}
	}
	
	public void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		req.getSession().invalidate();
		resp.sendRedirect("login");
	}
}
