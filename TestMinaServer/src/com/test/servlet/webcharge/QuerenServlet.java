package com.test.servlet.webcharge;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 提交
 * 
 * Servlet implementation class BaseServlet
 */
public class QuerenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String tempstr = "";
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().write(tempstr);
		return;
	}

	@Override
	public void init() throws ServletException {
		String line = null;
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new FileReader("D:\\http\\ceshi\\querenHtml.txt"));
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		tempstr = sb.toString();
		System.out.println(tempstr);
	}

	
	
}
