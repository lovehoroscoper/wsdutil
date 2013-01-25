package org.gonetbar.ssa.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.gonetbar.ssa.oauth.InitOpenLoginConfig;
import org.gonetbar.ssa.util.OauthLoginMustParam;
import org.jasig.cas.support.oauth.OAuthConfiguration;
import org.jasig.cas.support.oauth.OAuthUtils;
import org.scribe.up.provider.OAuthProvider;
import org.scribe.up.session.HttpUserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.godtips.common.UtilString;

/**
 * 初始化第三方登录
 * 
 * @author Administrator
 * 
 */
@Controller
public final class LoginInitAction {

	@RequestMapping(value = "/oauth20/{providerKey}/initlogin.do")
	public String initlogin(@PathVariable("providerKey") String providerKey, HttpServletRequest request, ModelMap model) {
		String nextUrl = null;
		try {
			String providerType = initOpenLoginConfig.getProviderTypeByKey(providerKey);
			if (!UtilString.isEmptyOrNullByTrim(providerType)) {
				final OAuthProvider provider = OAuthUtils.getProviderByType(this.providersDefinition.getProviders(), providerType);
				logger.debug("provider : {}", provider);
				if (null != provider) {
					HttpUserSession userSession = new HttpUserSession(request);
					OauthLoginMustParam.getMd5State(request, provider.getType(),true);
					nextUrl = provider.getAuthorizationUrl(userSession);
				}
			} else {
				logger.error("无效的登录操作!!![" + providerKey + "]信息异常");
			}
		} catch (Exception e) {
			logger.error("初始化登录[" + providerKey + "]信息异常", e);
		}
		if (UtilString.isEmptyOrNullByTrim(nextUrl)) {
			return "/showerror/index.do";
		} else {
			return "redirect:" + nextUrl;
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(LoginInitAction.class);

	@NotNull
	private OAuthConfiguration providersDefinition;

	@NotNull
	private InitOpenLoginConfig initOpenLoginConfig;

	@Resource(name = "initOpenLoginConfig")
	public final void setInitOpenLoginConfig(InitOpenLoginConfig initOpenLoginConfig) {
		this.initOpenLoginConfig = initOpenLoginConfig;
	}

	@Resource(name = "providersDefinition")
	public final void setProvidersDefinition(OAuthConfiguration providersDefinition) {
		this.providersDefinition = providersDefinition;
	}

}
