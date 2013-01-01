package org.gonetbar.ssa.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gonetbar.ssa.base.entity.ModelRecordStrUtil;
import org.gonetbar.ssa.constant.DBResultCode;
import org.gonetbar.ssa.constant.RegisterMd5;
import org.gonetbar.ssa.entity.ThirdRegVo;
import org.gonetbar.ssa.entity.UserInfoVo;
import org.gonetbar.ssa.service.RegisterUserService;
import org.jasig.cas.support.oauth.OAuthConstants;
import org.scribe.up.profile.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.godtips.common.RequestUtil;
import com.godtips.common.UtilString;

/**
 * 登录成功后，如果没有跳转到第3方平台这在我方平台显示登录成功 如果用户刷新则又需要重新登录， 用这个action跳转到成功界面
 * 
 * @author Administrator
 * 
 */
@Controller
public final class RegisterAction {

	@RequestMapping(value = "/register/index.do")
	public String registerBind(HttpServletRequest request, ModelMap model) {
		logger.info("进入到registerView");
		String nextUrl = "redirect:/login";
		HttpSession session = request.getSession(false);
		final ThirdRegVo thirdRegVo = (ThirdRegVo) session.getAttribute(ModelRecordStrUtil.THIRD_LOGIN_INFO);
		if (null != thirdRegVo) {
			// remove
			session.removeAttribute(ModelRecordStrUtil.THIRD_LOGIN_INFO);
			UserProfile userProfile = thirdRegVo.getUserProfile();
			String code = thirdRegVo.getCode();
			String providerId = thirdRegVo.getProviderId();
			if (null != userProfile) {
				String typedId = userProfile.getTypedId();
				if (!UtilString.isEmptyOrNullByTrim(providerId) && !UtilString.isEmptyOrNullByTrim(code) && !UtilString.isEmptyOrNullByTrim(typedId)) {
					String md5_valid = RegisterMd5.getRegisterMd5(providerId, code, typedId);
					model.put(ModelRecordStrUtil.THIRD_REG_MD5VALID, md5_valid);// md5code
					session.setAttribute(ModelRecordStrUtil.THIRD_LOGIN_INFO, thirdRegVo);
					nextUrl = "registerView";
				}
			}
		}
		logger.warn("测试添加---------1-------------");
		session.setAttribute("weisd", "RegisterAction /register/index.do ");
		return nextUrl;
	}

	@RequestMapping(value = "/register/add.do")
	public String registerAdd(HttpServletRequest request, ModelMap model) {
		logger.info("进入到registerView add");
		String nextUrl = "redirect:/login";
		HttpSession session = request.getSession(false);
		String my_md5_valid = RequestUtil.getParam(request, "my_code", "");
		if (null != session) {
			final ThirdRegVo thirdRegVo = (ThirdRegVo) session.getAttribute(ModelRecordStrUtil.THIRD_LOGIN_INFO);
			if (null != thirdRegVo) {
				session.removeAttribute(ModelRecordStrUtil.THIRD_LOGIN_INFO);
				UserProfile userProfile = thirdRegVo.getUserProfile();
				if (null != userProfile && !UtilString.isEmptyOrNullByTrim(my_md5_valid)) {
					String providerId = thirdRegVo.getProviderId();
					String code = thirdRegVo.getCode();
					String typedId = userProfile.getTypedId();
					if (!UtilString.isEmptyOrNullByTrim(providerId) && !UtilString.isEmptyOrNullByTrim(code) && !UtilString.isEmptyOrNullByTrim(typedId)) {
						String md5_valid = RegisterMd5.getRegisterMd5(providerId, code, typedId);
						if (my_md5_valid.equals(md5_valid)) {
							// 验证通过
							String[] third_info = typedId.split(UserProfile.SEPARATOR);
							String email = RequestUtil.getParam(request, "email", "");
							String password = RequestUtil.getParam(request, "password", "");
							String thirduserid = third_info[1];
							// valid user info
							if (null != third_info && third_info.length == 2) {
								// 注册用户
								UserInfoVo user = new UserInfoVo();
								user.setUsername(email);
								user.setPassword(password);
								user.setProviderid(providerId);
								user.setThirduserid(thirduserid);

								Map<String, String> param = new HashMap<String, String>();
								param.put("username", email);
								param.put("password", password);
								param.put("providerid", providerId);
								param.put("thirduserid", thirduserid);
								param.put("dbreturn", "");
								param.put("userid", "");
								registerUserService.addUser(param);
								String dbreturn = param.get("dbreturn");
								if (DBResultCode.SUCC.equals(dbreturn)) {
									
									session.setAttribute(ModelRecordStrUtil.THIRD_LOGIN_INFO, thirdRegVo);
									nextUrl += "?code=&" + OAuthConstants.OAUTH_PROVIDER + "=" + thirdRegVo.getProviderType();
									// 跳转登录界面带上参数
									// code
									logger.info("注册成功完成跳转nextUrl[" + nextUrl + "]");
								} else {
									logger.error("注册用户失败[" + dbreturn + "]");
								}

							}
						}
					}
				}
			}
		}
		return nextUrl;
	}

	private static final Logger logger = LoggerFactory.getLogger(ShowErrorController.class);

	private RegisterUserService registerUserService;

	@Resource(name = "registerUserService")
	public final void setRegisterUserService(RegisterUserService registerUserService) {
		this.registerUserService = registerUserService;
	}

}
