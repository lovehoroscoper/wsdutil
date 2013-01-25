package client.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TestClient1
 */
public class TestClient1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("servlet 11111111");
		
		String t = getFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		
		response.getWriter().write("servlet 11111111-----------[" + t + "]");
	}
	
	/**
	 * 格式化指定日期为指定格式的字符串
	 * 
	 * @param date
	 *            要格式化的日期时间
	 * @param format
	 *            输出的日期格式 如"yyyy-MM-dd"
	 * @return
	 */
	public String getFormatDate(java.util.Date date, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		return f.format(date);
	}


}
