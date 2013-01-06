package org.gonetbar.ssa.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.godtips.common.RequestUtil;
import com.godtips.common.UtilRegex;
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
		String nextUrl = "redirect:/login";
		HttpSession session = request.getSession(false);
		final ThirdRegVo thirdRegVo = (ThirdRegVo) session.getAttribute(ModelRecordStrUtil.THIRD_LOGIN_INFO);
		if (null != thirdRegVo) {
			session.removeAttribute(ModelRecordStrUtil.THIRD_LOGIN_INFO);
			UserProfile userProfile = thirdRegVo.getUserProfile();
			String code = thirdRegVo.getAccessToken();
			String providerId = thirdRegVo.getProviderId();
			String keyStr = UUID.randomUUID().toString();
			thirdRegVo.setKeyStr(keyStr);
			if (null != userProfile) {
				String typedId = userProfile.getTypedId();
				if (!UtilString.isEmptyOrNullByTrim(providerId) && !UtilString.isEmptyOrNullByTrim(code) && !UtilString.isEmptyOrNullByTrim(typedId)) {
					String md5_valid = RegisterMd5.getRegisterMd5(providerId, code, typedId, keyStr);
					model.put(ModelRecordStrUtil.THIRD_REG_MD5VALID, md5_valid);// md5code
					session.setAttribute(ModelRecordStrUtil.THIRD_LOGIN_INFO, thirdRegVo);
					nextUrl = "registerView";
				}
			}
		}
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
				String keyStr = thirdRegVo.getKeyStr();
				thirdRegVo.setKeyStr("");// 删除避免重复提交
				UserProfile userProfile = thirdRegVo.getUserProfile();
				// 是否需要验证是否已经存在
				if (null != userProfile && !UtilString.isEmptyOrNullByTrim(my_md5_valid)) {
					String providerId = thirdRegVo.getProviderId();
					String code = thirdRegVo.getAccessToken();
					String typedId = userProfile.getTypedId();
					if (!UtilString.isEmptyOrNullByTrim(providerId) && !UtilString.isEmptyOrNullByTrim(code) && !UtilString.isEmptyOrNullByTrim(typedId) && !UtilString.isEmptyOrNullByTrim(keyStr)) {
						String md5_valid = RegisterMd5.getRegisterMd5(providerId, code, typedId, keyStr);
						if (my_md5_valid.equals(md5_valid)) {
							// 验证通过
							String[] third_info = typedId.split(UserProfile.SEPARATOR);
							String email = RequestUtil.getParam(request, "email", "");
							String password_t = RequestUtil.getParam(request, "password", "");
							String nikename = RequestUtil.getParam(request, "nikename", "");
							String thirduserid = third_info[1];
							// valid user info
							if (null != third_info && third_info.length == 2) {
								// 注册用户
								String password = passwordEncoder.encode(password_t);
								UserInfoVo user = new UserInfoVo();
								user.setUsername(email);
								user.setPassword(password);
								user.setProviderid(providerId);
								user.setThirduserid(thirduserid);
								user.setNikename(nikename);
								Map<String, String> param = new HashMap<String, String>();
								param.put("username", email);
								param.put("password", password);
								param.put("providerid", providerId);
								param.put("thirduserid", thirduserid);
								param.put("nikename", nikename);
								param.put("dbreturn", "");
								param.put("dbcode", "");
								param.put("userid", "");
								String checkRes = checkUserUnique(param, password_t);
								if ("".equals(checkRes)) {
									param.put("dbreturn", "");
									param.put("dbcode", "");
									param.put("userid", "");
									registerUserService.addUser(param);
									String dbreturn = param.get("dbreturn");
									if (DBResultCode.SUCC.equals(dbreturn)) {
										String after_my_md5_valid = RegisterMd5.getRegisterAfterMd5(userProfile.getAccessToken(), typedId, my_md5_valid);
										thirdRegVo.setMd5Valid(after_my_md5_valid);
										session.setAttribute(ModelRecordStrUtil.THIRD_LOGIN_INFO, thirdRegVo);
										nextUrl += "?code=" + my_md5_valid + "&" + OAuthConstants.OAUTH_PROVIDER + "=" + thirdRegVo.getProviderType();
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
		}
		return nextUrl;
	}

	private String checkUserUnique(Map<String, String> param, String password_t) {
		String username = param.get("username");
		String providerid = param.get("providerid");
		String thirduserid = param.get("thirduserid");
		String nikename = param.get("nikename");
		if (UtilString.isEmptyOrNullByTrim(username) || !UtilRegex.checkEmail(username)) {
			return "邮箱不合法";
		}
		if (UtilString.isEmptyOrNullByTrim(password_t) || password_t.length() < 6 || password_t.length() > 30) {
			return "密码不合法";
		}
		if (UtilString.isEmptyOrNullByTrim(providerid)) {
			return "第三方登录类型为空";
		}
		if (UtilString.isEmptyOrNullByTrim(thirduserid)) {
			return "第三方登录用户ID为空";
		}
		if (UtilString.isEmptyOrNullByTrim(nikename) || nikename.length() > 50) {
			return "昵称不合法";
		}
		param.put("type", "0");
		registerUserService.queryCheckUserUnique(param);
		param.remove("type");
		String dbreturn = param.get("dbreturn");
		String dbcode = param.get("dbcode");
		param.put("dbreturn", "");
		param.put("dbcode", "");
		param.put("userid", "");
		if (DBResultCode.SUCC.equals(dbreturn)) {
			return "";
		} else if (DBResultCode.A_USER_FAIL.equals(dbreturn)) {
			if (DBResultCode.FAIL_USERNAME.equals(dbcode)) {
				return "已经存在的用户标识[" + username + "]";
			} else if (DBResultCode.FAIL_THIRD.equals(dbcode)) {
				return "第三方登录帐户已经在我方平台绑定过";
			} else {
				return "其他错误[" + dbcode + "]";
			}
		} else if (DBResultCode.PARAM_VALID.equals(dbreturn)) {
			return "DB参数错误[" + dbcode + "]";
		} else {
			return dbreturn;
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(ShowErrorController.class);

	private RegisterUserService registerUserService;

	private PasswordEncoder passwordEncoder;

	@Resource(name = "passwordEncoder")
	public final void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Resource(name = "registerUserService")
	public final void setRegisterUserService(RegisterUserService registerUserService) {
		this.registerUserService = registerUserService;
	}

}
