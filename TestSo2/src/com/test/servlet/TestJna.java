package com.test.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.test.SoJna;
import com.test.TextStart;

/**
 * Servlet implementation class TestSo
 */
public class TestJna extends HttpServlet {
	@Override
	public void init() throws ServletException {

		// logger.info("init  ----------start------");
		// SoJna so = new SoJna();
		// so.initial();
		// logger.info("init  ----------end------");
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
		String code = request.getParameter("code");
		String testInfo = request.getParameter("testInfo");
		String res = "start";
		if ("weisd007".equals(code)) {
			String str = SoJna.getFormatDate(new Date(), "yyyyMMddHHmmss");
			if (null == testInfo || "".equals(testInfo.trim())) {
				testInfo = "weisd的测试" + str;
			}
			logger.info("----合成开始[" + testInfo + "]------[" + str + "]------");
			SoJna so = new SoJna();
			so.initial(testInfo.trim());
			logger.info("----合成结束------end------");
			res = "ok";

		} else {
			logger.error("------code[" + code + "]---error");
			res = "error";
		}
		response.getWriter().write(res);
		response.getWriter().flush();
	}

}
