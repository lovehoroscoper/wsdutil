package org.gonetbar.ssa.oauth.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.gonetbar.ssa.base.entity.ModelRecordStrUtil;
import org.gonetbar.ssa.entity.UserInfoVo;
import org.gonetbar.ssa.entity.UserProviderInfoVo;
import org.gonetbar.ssa.service.SsaUserService;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * 用于验证第三方登录后是否在我方平台有账户
 * 
 * @author Administrator
 * 
 */
public final class LoginSuccRegSetCheckAction extends AbstractAction {

	@Override
	protected Event doExecute(RequestContext context) throws Exception {
		HttpServletRequest request = WebUtils.getHttpServletRequest(context);
		HttpSession session = request.getSession();
		String returncode = (String)context.getRequestScope().get("returncode");
		// 检查是本地登录还是第三方接口
		// String providertype = request.getParameter(OAUTH_PROVIDER);
//		String providertype = "yahoo1";
//		String providerid = "yahoo1";
		String providertype = "";
		String providerid = "";
		
		
		
		String[] username_arr = request.getParameterValues("username");

		// 避免2个情况同时存在
		String username = null;
		if (null == username_arr && StringUtils.isNotBlank(providertype) && StringUtils.isNotBlank(providerid)) {
			UserProviderInfoVo third_user = ssaUserService.findUserByProviderType(providertype, providerid);
			if (null == third_user) {
				// 需要在我方注册
				return result("regset");
			}
			username = third_user.getUsername();
		} else if (null != username_arr && username_arr.length == 1 && StringUtils.isBlank(providertype) && StringUtils.isBlank(providerid)) {
			//我方db
			username = username_arr[0];
		} else {
			return result("regSetCheckError");
		}
		if (StringUtils.isBlank(username)) {
			return error();
		} else {
			UserInfoVo sess_user = (UserInfoVo) session.getAttribute(ModelRecordStrUtil.SESS_LOGIN_USER);
			if (null == sess_user) {
				UserInfoVo login_user = ssaUserService.findUserByName(username);
				if (null == login_user) {
					return error();
				} else {
					session.setAttribute(ModelRecordStrUtil.SESS_LOGIN_USER, login_user);
				}
			}
		}
		if(StringUtils.isBlank(returncode)){
			return result("regSetCheckError");
		}else{
			return result(returncode);
		}
	}

	public final void setSsaUserService(SsaUserService ssaUserService) {
		this.ssaUserService = ssaUserService;
	}

	@NotNull
	private SsaUserService ssaUserService;

	private static final String OAUTH_PROVIDER = "oauth_provider";

	private static final Logger logger = LoggerFactory.getLogger(LoginSuccRegSetCheckAction.class);

}
