package client.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;

/**
 * Servlet implementation class TestClient1
 */
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final Log logger = LogFactory.getLog(this.getClass());

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String t = getFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss");

		final HttpSession session = request.getSession(false);
		final Assertion assertion = session != null ? (Assertion) session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION) : null;

		logger.info("退出时间[" + t + "]");
		logger.info("退出用户信息------------开始-----------------");
		if (null != assertion) {
			AttributePrincipal attp = assertion.getPrincipal();
			if (null != attp) {
				logger.info("用户名称[" + attp.getName() + "]");
				Map map = attp.getAttributes();
				if (null != map) {
					// 效率高
					Set<Map.Entry<String, Integer>> set = map.entrySet();
					Iterator<Map.Entry<String, Integer>> it = set.iterator();
					while (it.hasNext()) {
						Map.Entry<String, Integer> me = it.next();
						logger.info("assertion.getPrincipal.getAttributes中map[" + (me.getKey() + " " + me.getValue()) + "]");// 直接获取键和值
					}
				}
			}
			
			
			Map ma = assertion.getAttributes();
			if (null != ma) {
				if (null != ma) {
					// 效率高
					Set<Map.Entry<String, Integer>> set = ma.entrySet();
					Iterator<Map.Entry<String, Integer>> it = set.iterator();
					while (it.hasNext()) {
						Map.Entry<String, Integer> me = it.next();
						logger.info("assertion.getAttributes中map[" + (me.getKey() + " " + me.getValue()) + "]");// 直接获取键和值
					}
				}
			}

		}else{
			logger.error("用户[assertion]--空---------------");
		}
		logger.info("退出用户信息------------开始-----------------");
		session.removeAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);

		response.sendRedirect("index.html");
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
