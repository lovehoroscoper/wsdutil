package org.jasig.cas.support.oauth.authentication.handler.support;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.support.oauth.OAuthConfiguration;
import org.jasig.cas.support.oauth.OAuthUtils;
import org.jasig.cas.support.oauth.authentication.principal.OAuthCredentials;
import org.scribe.up.profile.UserProfile;
import org.scribe.up.provider.OAuthProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于检测oauth登录后的id是否在我方平台注册过
 * @author Administrator
 *
 */
public final class CheckRegisterOAuthHandler extends AbstractPreAndPostProcessingAuthenticationHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(CheckRegisterOAuthHandler.class);
    
    @NotNull
    private OAuthConfiguration configuration;
    
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
        
        // get user profile
        final UserProfile userProfile = provider.getUserProfile(oauthCredentials.getCredential());
        logger.debug("userProfile : {}", userProfile);
        
        if (userProfile != null && StringUtils.isNotBlank(userProfile.getId())) {
            oauthCredentials.setUserProfile(userProfile);
            return true;
        } else {
            return false;
        }
    }
    
    public void setConfiguration(final OAuthConfiguration configuration) {
        this.configuration = configuration;
    }
}
