package com.revature.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revature.delegate.HomeDelegate;
import com.revature.delegate.LoginDelegate;
import com.revature.model.Employee;

public class RequestHelper {
	private HomeDelegate hd = new HomeDelegate();
	private LoginDelegate ld = new LoginDelegate();
	public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		Employee employeeDetails = (Employee) session.getAttribute("currentUser");
		//System.out.println("Process in request helper triggered!");//test
		String switchString = req.getRequestURI().substring(req.getContextPath().length()+1);
		while(switchString.indexOf("/")>0) {
			switchString = switchString.substring(0, switchString.indexOf("/"));
		}
		switch(switchString) {
		case "home": 
			hd.goHome(req, resp); 
			break;
		case "login": 
			System.out.println("Login switch case triggered!");
			if("POST".equals(req.getMethod())) {
				ld.login(req, resp);
			} else {
				ld.getPage(req, resp);
			} 
			break;
		case "logout": ld.logout(req, resp); break;
		case "managerhome":
			System.out.println("Manager home switch case triggered!");
			hd.goHome(req, resp);
			break;
		/*
		 * 
		 * EMPLOYEE CASES
		 * 
		 * */
		case "addexpense":
			System.out.println("RequestHelper => addexpense switch case triggered");
			hd.addExpense(req, resp);
			break;
		case "getemployeeresolved":
			System.out.println("getemployeeresolved switch case triggered!");
			hd.getResolvedRequests(req, resp);
			break;
		case "getemployeeunresolved":
			System.out.println("getemployeeunresolved switch case triggered!");
			hd.getPendingRequests(req, resp);
			break;
		case "editprofile":
			System.out.println("editprofile switch case triggered!");
			hd.updateProfile(req, resp);
			break;
		/*
		 * 
		 * MANAGER CASES
		 * 
		 * */
		case "viewallemployees":
			System.out.println("viewallemployees switch case triggered!");
			hd.viewAllEmployees(req, resp);
			break;
		case "viewallpendingrequests":
			System.out.println("viewallpendingrequests switch case triggered!");
			hd.viewAllPendingRequests(req, resp);
			break;
		case "viewallresolvedrequests":
			System.out.println("viewallresolvedrequests switch case triggered!");
			hd.viewAllResolvedRequests(req, resp);
			break;
		case "createemployee":
			System.out.println("viewallresolvedrequests switch case triggered!");
			hd.createEmployee(req, resp);
			break;
		case "viewallrequests":
			System.out.println("viewallresolvedrequests switch case triggered!");
			hd.viewAllRequests(req, resp);
			break;
		default: 
			System.out.println("Default switch case triggered!");
			break;
		}
	}
}
