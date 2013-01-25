package org.gonetbar.ssa.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gonetbar.ssa.base.entity.ModelRecordStrUtil;
import org.gonetbar.ssa.constant.DBResultCode;
import org.gonetbar.ssa.constant.Oauth20Attr;
import org.gonetbar.ssa.constant.RegisterMd5;
import org.gonetbar.ssa.constant.user.UserCheckCode;
import org.gonetbar.ssa.entity.ThirdRegVo;
import org.gonetbar.ssa.entity.UserInfoVo;
import org.gonetbar.ssa.service.RegisterUserService;
import org.gonetbar.ssa.util.OauthLoginMustParam;
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

	/**
	 * 绑定逻辑还有问题,如果是多个第三方绑定同一个帐号
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/register/add.do")
	public String registerAdd(HttpServletRequest request, ModelMap model) {
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
								String encodedpassword = passwordEncoder.encode(password_t);
								UserInfoVo user = new UserInfoVo();
								user.setUsername(email);
								user.setPassword(encodedpassword);
								user.setProviderid(providerId);
								user.setThirduserid(thirduserid);
								user.setNikename(nikename);
								Map<String, String> param = new HashMap<String, String>();
								param.put("bindtype", "");
								param.put("username", email);
								param.put("password", encodedpassword);
								param.put("providerid", providerId);
								param.put("thirduserid", thirduserid);
								param.put("nikename", nikename);
								param.put("dbreturn", "");
								param.put("userid", "");
								param.put("info1", "");
								String checkRes = checkUserUnique(param, email, password_t, providerId, thirduserid, nikename);
								String bindtype = param.get("bindtype");
								if ("".equals(checkRes) && UtilString.notEmptyOrNullByTrim(bindtype)) {
									registerUserService.addUser(param);
									String dbreturn = param.get("dbreturn");
									if (DBResultCode.SUCC.equals(dbreturn)) {
										String after_my_md5_valid = RegisterMd5.getRegisterAfterMd5(userProfile.getAccessToken(), typedId, my_md5_valid);
										thirdRegVo.setMd5Valid(after_my_md5_valid);
										session.setAttribute(ModelRecordStrUtil.THIRD_LOGIN_INFO, thirdRegVo);
										String reg_state = OauthLoginMustParam.getMd5State(request, thirdRegVo.getProviderType(), false);
										StringBuffer sb = new StringBuffer();
										sb.append("?").append("code=").append(my_md5_valid).append("&").append(OAuthConstants.OAUTH_PROVIDER).append("=").append(thirdRegVo.getProviderType())
												.append("&").append(Oauth20Attr.OAUTH_STATE).append("=").append(reg_state);
										nextUrl += sb.toString();
										logger.info("用户绑定成功完成跳转nextUrl[" + nextUrl + "]");
									} else {
										String info1 = param.get("info1");
										logger.error("用户绑定失败[" + dbreturn + "][" + info1 + "]");
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

	private String checkUserUnique(Map<String, String> param, String username, String password, String providerid, String thirduserid, String nikename) {
		if (UtilString.isEmptyOrNullByTrim(username) || !UtilRegex.checkEmail(username)) {
			return "邮箱不合法";
		}
		if (UtilString.isEmptyOrNullByTrim(password) || password.length() < 6 || password.length() > 30) {
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
		Map<String, String> param_check = new HashMap<String, String>();
		param_check.put("checktype", "1");
		param_check.put("username", username);
		param_check.put("password", password);
		param_check.put("providerid", providerid);
		param_check.put("thirduserid", thirduserid);
		param_check.put("nikename", nikename);
		param_check.put("dbreturn", "");
		param_check.put("localexist", "");// 我方是否存在
		param_check.put("thirdexist", "");// 对方是否存在
		param_check.put("info1", "");// 冗余信息1
		param_check.put("info2", "");// 冗余信息2
		registerUserService.queryCheckUserUnique(param_check);
		String dbreturn = param_check.get("dbreturn");
		if (DBResultCode.SUCC.equals(dbreturn)) {
			// 查询数据库正常
			String localexist = param_check.get("localexist");
			String thirdexist = param_check.get("thirdexist");
			if (UserCheckCode.CUN_0000.equals(thirdexist)) {
				/* 第三方不存在 */
				if (UserCheckCode.CUN_0000.equals(localexist)) {
					// 我方不存在
					// 双方都添加
					param.put("bindtype", "1");
					return "";
				} else if (UserCheckCode.CUN_0001.equals(localexist)) {
					// 我方用户存在
					// 验证密码是否正确
					String dbEncodedPassword = param_check.get("info1");
					if (UtilString.notEmptyOrNullByTrim(dbEncodedPassword) && passwordEncoder.matches(password, dbEncodedPassword)) {
						// 添加第三方
						param.put("bindtype", "2");
						return "";
					} else {
						return "密码验证失败[" + localexist + "]";
					}
				} else if (UserCheckCode.CUN_0003.equals(localexist)) {
					return "本平台账户已经绑定过该第三方登录的账户[" + localexist + "]";
				} else {
					return "登录ID验证异常[" + localexist + "]";
				}
			} else {
				return "第三方登录ID已经在我方平台绑定过";
			}
		} else if (DBResultCode.PARAM_VALID.equals(dbreturn)) {
			return "操作DB参数验证失败[" + dbreturn + "]";
		} else {
			return "操作DB错误[" + dbreturn + "]";
		}
		// return "其他错误[OTHER]";
	}

	private static final Logger logger = LoggerFactory.getLogger(RegisterAction.class);

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
