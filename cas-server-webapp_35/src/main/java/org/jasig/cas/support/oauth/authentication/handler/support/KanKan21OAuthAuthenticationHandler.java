package org.jasig.cas.support.oauth.authentication.handler.support;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.gonetbar.ssa.cas.exception.CheckNotRegisterException;
import org.gonetbar.ssa.entity.ThirdProvider;
import org.gonetbar.ssa.entity.ThirdRegVo;
import org.gonetbar.ssa.entity.UserProviderInfoVo;
import org.gonetbar.ssa.service.SsaUserService;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.support.oauth.OAuthConfiguration;
import org.jasig.cas.support.oauth.OAuthUtils;
import org.jasig.cas.support.oauth.authentication.principal.OAuthCredentials;
import org.scribe.model.Token;
import org.scribe.up.credential.OAuthCredential;
import org.scribe.up.profile.UserProfile;
import org.scribe.up.provider.BaseOAuthProvider;
import org.scribe.up.provider.OAuthProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.godtips.common.UtilString;

public final class KanKan21OAuthAuthenticationHandler extends AbstractPreAndPostProcessingAuthenticationHandler {

	private static final Logger logger = LoggerFactory.getLogger(OAuthAuthenticationHandler.class);

	@NotNull
	private OAuthConfiguration configuration;

	@NotNull
	private SsaUserService ssaUserService;

	public final void setSsaUserService(SsaUserService ssaUserService) {
		this.ssaUserService = ssaUserService;
	}

	@Override
	public boolean supports(final Credentials credentials) {
		return credentials != null && (OAuthCredentials.class.isAssignableFrom(credentials.getClass()));
	}

	@Override
	protected boolean doAuthentication(final Credentials credentials) throws AuthenticationException {
		final OAuthCredentials oauthCredentials = (OAuthCredentials) credentials;
		logger.debug("credential : {}", oauthCredentials);

		final String providerType = oauthCredentials.getCredential().getProviderType();
		logger.debug("providerType : {}", providerType);

		// get provider
		final OAuthProvider provider = OAuthUtils.getProviderByType(this.configuration.getProviders(), providerType);
		logger.debug("provider : {}", provider);

		// weisd 如果存在则是刚才注册的过来的
		UserProfile temp_userProfile = oauthCredentials.getUserProfile();
		oauthCredentials.setUserProfile(null);
		if (null != temp_userProfile) {
			String accessToken_str = temp_userProfile.getAccessToken();
			if (!UtilString.isEmptyOrNullByTrim(accessToken_str)) {
				temp_userProfile = ((BaseOAuthProvider) provider).getUserProfile(new Token(accessToken_str, ""));
			} else {
				temp_userProfile = null;
			}
		}
		if (null == temp_userProfile) {
			// get user profile
			OAuthCredential credential = oauthCredentials.getCredential();
			temp_userProfile = provider.getUserProfile(credential);
		}

		final UserProfile userProfile = temp_userProfile;
		logger.debug("userProfile : {}", userProfile);

		if (userProfile != null && StringUtils.isNotBlank(userProfile.getId()) && !UtilString.isEmptyOrNullByTrim(providerType)) {
			ThirdProvider thirdProvider = ssaUserService.findProviderIdByType(providerType);// 第三方登录类型ID
			if (thirdProvider != null) {
				String thirdUserId = userProfile.getId();// 第三方用户ID
				UserProviderInfoVo third_user = ssaUserService.findUserByProviderId(thirdProvider.getProviderId(), thirdUserId);
				if (null == third_user || UtilString.isEmptyOrNullByTrim(third_user.getUsername())) {
					logger.warn("第三方[" + providerType + "]登录用户[" + thirdUserId + "]未绑定我方平台信息");
					final ThirdRegVo thirdRegVo = new ThirdRegVo(thirdProvider.getProviderId(), providerType, userProfile.getAccessToken(), userProfile);
					throw new CheckNotRegisterException("NOT_BOUND_USER", thirdRegVo);
				} else {
					oauthCredentials.setUserProfile(userProfile);
					return true;
				}
			}
		}
		return false;
	}

	public void setConfiguration(final OAuthConfiguration configuration) {
		this.configuration = configuration;
	}
}
