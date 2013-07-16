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
public class TijiaoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String tempstrERR = "";
	private static String tempstrSucc = "";
       

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
		
		String hf = request.getParameter("hf");
		response.setContentType("text/html;charset=utf-8");
		int a = Integer.valueOf(hf.substring(hf.length()-1, hf.length()));
		
		System.out.println("成功与否参数:" + a);
		if(a>4){
			response.getWriter().write(tempstrERR);
		}else{
			response.getWriter().write(tempstrSucc);
		}
		return;
	}

	@Override
	public void init() throws ServletException {
		String line = null;
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new FileReader("D:\\http\\ceshi\\tijiaoError.txt"));
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		tempstrERR = sb.toString();
		System.out.println(tempstrERR);
		
		
		String line2 = null;
		StringBuffer sb2 = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new FileReader("D:\\http\\ceshi\\tijiaoHtmlSucc.txt"));
			while ((line2 = in.readLine()) != null) {
				sb2.append(line2);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		tempstrSucc = sb2.toString();
		System.out.println(tempstrSucc);
	}

	
	
}
