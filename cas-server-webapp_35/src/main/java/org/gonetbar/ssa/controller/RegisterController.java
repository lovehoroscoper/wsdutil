package org.gonetbar.ssa.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * 登录成功后，如果没有跳转到第3方平台这在我方平台显示登录成功 如果用户刷新则又需要重新登录， 用这个action跳转到成功界面
 * 
 * @author Administrator
 * 
 */
public final class RegisterController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

	@NotNull
	private String viewName;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView(this.viewName);
	}

	public void setViewName(final String viewName) {
		this.viewName = viewName;
	}

}
