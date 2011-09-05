//package com.weisd.hfdealer;
//
//import java.io.IOException;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * @desc 描述：
// *
// * @author weisd E-mail:weisd@junbao.net
// * @version 创建时间：2011-7-27 下午1:14:51
// */
//public class TestFilter implements Filter {
//
//	@Override
//	public void destroy() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
//		// TODO Auto-generated method stub
//		HttpServletRequest request = (HttpServletRequest) arg0;
//		HttpServletResponse response = (HttpServletResponse) arg1;
//		
//		System.out.println("-----------------------这里只是测试 ，避免每次都要修改代码----------------------------");
//		request.getSession().setAttribute(com.speed.speedadmin.base.AccessFilter.CAS_FILTER_USER, "weisd");
//		request.getSession().setAttribute(com.speed.speedadmin.base.AccessFilter.CAS_FILTER_USER_NAME, "韦胜迪");
//		arg2.doFilter(request, response);
//
//	}
//
//	@Override
//	public void init(FilterConfig arg0) throws ServletException {
//		// TODO Auto-generated method stub
//
//	}
//
//}
