package com.test.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.test.So;
import com.test.TextStart;

/**
 * Servlet implementation class TestSo
 */
public class TestSo extends HttpServlet {
	@Override
	public void init() throws ServletException {

		logger.info("init  ----------start------");
		So so = new So();
		so.initial();
		logger.info("init  ----------end------");
	}

	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(TextStart.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().write("ok");
		response.getWriter().flush();
	}

}
