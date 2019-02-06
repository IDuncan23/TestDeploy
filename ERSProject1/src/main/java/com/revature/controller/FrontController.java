package com.revature.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.servlets.DefaultServlet;

public class FrontController extends DefaultServlet {
	
	private static final long serialVersionUID = 3479236907455377769L;
	
	private RequestHelper rh = new RequestHelper();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(req.getRequestURI().equals("/ERSProject1/") || req.getRequestURI().contains("/static")) {
			//System.out.println("doGet if block Triggered in Front Controller");//test
			super.doGet(req, resp);
		} else {
			//System.out.println("doGet else block Triggered in Front Controller");//test
			rh.process(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//System.out.println("doPost Triggered in Front Controller");//test
		doGet(req,resp);
	}
}
