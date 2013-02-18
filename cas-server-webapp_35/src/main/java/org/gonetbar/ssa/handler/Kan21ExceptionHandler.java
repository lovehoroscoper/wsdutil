package org.gonetbar.ssa.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class Kan21ExceptionHandler implements HandlerExceptionResolver {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		logger.error("异常无法捕获遗漏出来了", ex);
		return null;
	}

}
