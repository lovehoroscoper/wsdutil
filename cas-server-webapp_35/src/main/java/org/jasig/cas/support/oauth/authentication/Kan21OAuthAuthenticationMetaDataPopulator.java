package org.jasig.cas.support.oauth.authentication;

import java.util.HashMap;
import java.util.Map;

import org.gonetbar.ssa.constant.UserLoginAttr;
import org.gonetbar.ssa.constant.UserLoginType;
import org.gonetbar.ssa.entity.ThirdProvider;
import org.gonetbar.ssa.service.SsaUserService;
import org.gonetbar.ssa.util.CheckUserLoginType;
import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.AuthenticationMetaDataPopulator;
import org.jasig.cas.authentication.MutableAuthentication;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.jasig.cas.support.oauth.OAuthConstants;
import org.jasig.cas.support.oauth.authentication.principal.OAuthCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.godtips.common.UtilString;

/**
 * 默认的类没有包含我方的基础信息
 * 
 * 我自己从数据库查询出来的属性
 * 
 * OAuthAuthenticationMetaDataPopulator
 * 
 */
public final class Kan21OAuthAuthenticationMetaDataPopulator implements AuthenticationMetaDataPopulator {

	private static final Logger logger = LoggerFactory.getLogger(Kan21OAuthAuthenticationMetaDataPopulator.class);

	public Authentication populateAttributes(final Authentication authentication, final Credentials credentials) {
		final Principal pri = authentication.getPrincipal();
		String user_local_uniquekey = (String) pri.getAttributes().get(UserLoginAttr.USER_LOCAL_UNIQUEKEY);
		String p_uid = pri.getId();
		String loginType = CheckUserLoginType.getLoginTypeByUid(p_uid);
		if (UtilString.notEmptyOrNullByTrim(user_local_uniquekey) && (UserLoginType.LOGIN_TYPE_LOCAL.equals(loginType) || UserLoginType.LOGIN_TYPE_OAUTH.equals(loginType))) {
			// 是本地用户 // 是第三方用户
		} else {
			logger.error("异常的登录方式[" + user_local_uniquekey + "][" + authentication + "]");
			return null;
		}
		if (credentials instanceof OAuthCredentials && UserLoginType.LOGIN_TYPE_OAUTH.equals(loginType)) {
			// GitHubProfile#xxxxxx
			OAuthCredentials oauthCredentials = (OAuthCredentials) credentials;
			String third_login_type = UtilString.getStringFromEmpty(oauthCredentials.getUserProfile().getTypedId());
			String user_third_uniquekey = UtilString.getStringFromEmpty(oauthCredentials.getUserProfile().getId());
			String profileType = CheckUserLoginType.getProviderTypeByUid(p_uid);
			ThirdProvider provider = ssaUserService.findProviderIdByType(profileType);
			if(null == provider){
				return null;
			}
			Map<String, Object> temp_map = new HashMap<String, Object>();
			temp_map.putAll(pri.getAttributes());
			//TODO weisd 暂时注释第三方属性
			//temp_map.putAll(oauthCredentials.getUserProfile().getAttributes());
			temp_map.put(UserLoginAttr.USER_LOGIN_TYPE, loginType);//登录类型
			temp_map.put(UserLoginAttr.USER_THIRD_UNIQUEKEY, user_third_uniquekey);
			temp_map.put(UserLoginAttr.THIRD_LOGIN_TYPE, third_login_type);
			temp_map.put(UserLoginAttr.THIRD_LOGIN_PROVIDERID, provider.getProviderId());
			
			// final Principal simplePrincipal = new
			// SimplePrincipal(authentication.getPrincipal().getId(),
			// oauthCredentials.getUserProfile().getAttributes());
			final Principal simplePrincipal = new SimplePrincipal(user_local_uniquekey, temp_map);

			final MutableAuthentication mutableAuthentication = new MutableAuthentication(simplePrincipal, authentication.getAuthenticatedDate());

			mutableAuthentication.getAttributes().putAll(authentication.getAttributes());

			mutableAuthentication.getAttributes().put(OAuthConstants.PROVIDER_TYPE, oauthCredentials.getCredential().getProviderType());

			return mutableAuthentication;
		}
		authentication.getAttributes().put(UserLoginAttr.USER_LOGIN_TYPE, loginType);
		return authentication;
	}

	private SsaUserService ssaUserService;

	public final void setSsaUserService(SsaUserService ssaUserService) {
		this.ssaUserService = ssaUserService;
	}

}
