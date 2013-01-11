package org.jasig.cas.support.oauth.authentication;

import java.util.HashMap;
import java.util.Map;

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

/**
 * 默认的类没有包含我方的基础信息
 * 
 * OAuthAuthenticationMetaDataPopulator
 * 
 */
public final class Kan21OAuthAuthenticationMetaDataPopulator implements AuthenticationMetaDataPopulator {
	
	private static final Logger logger = LoggerFactory.getLogger(Kan21OAuthAuthenticationMetaDataPopulator.class);

	public Authentication populateAttributes(final Authentication authentication, final Credentials credentials) {
		
		//TODO 还有一个不明确的是否需要把ID修改为我这边的ID
		if (credentials instanceof OAuthCredentials) {
			OAuthCredentials oauthCredentials = (OAuthCredentials) credentials;
			
			//TODO weisd 这里遗漏了我自己从数据库查询出来的属性
			final Principal pri = authentication.getPrincipal();
			String id = pri.getId();
			Map<String, Object> temp_map = new HashMap<String, Object>();
			String new_id = (String)pri.getAttributes().get("username");
			temp_map.putAll(pri.getAttributes());
			temp_map.putAll(oauthCredentials.getUserProfile().getAttributes());
			
			//final Principal simplePrincipal = new SimplePrincipal(authentication.getPrincipal().getId(), oauthCredentials.getUserProfile().getAttributes());
			
			//final Principal simplePrincipal = new SimplePrincipal(id, temp_map);
			final Principal simplePrincipal = new SimplePrincipal(new_id, temp_map);
			
			
			
			final MutableAuthentication mutableAuthentication = new MutableAuthentication(simplePrincipal, authentication.getAuthenticatedDate());
			
			
			mutableAuthentication.getAttributes().putAll(authentication.getAttributes());
			
			
			
			mutableAuthentication.getAttributes().put(OAuthConstants.PROVIDER_TYPE, oauthCredentials.getCredential().getProviderType());
			
			
			
			return mutableAuthentication;
		}
		return authentication;
	}
}
