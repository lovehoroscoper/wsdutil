package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.TokenExtractor20Impl;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.scribe.oauth.QQServiceImpl;
import org.scribe.utils.OAuthEncoder;

/**
 * 
 * @author Administrator
 *
 */
public class QQApi20 extends DefaultApi20 {

	private static final String AUTHORIZE_URL = "https://graph.qq.com/oauth2.0/authorize?client_id=%s&redirect_uri=%s&response_type=code";

	private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";
	
	@Override
	public Verb getAccessTokenVerb() {
		return Verb.POST;
	}

	@Override
	public AccessTokenExtractor getAccessTokenExtractor() {
		return new TokenExtractor20Impl();
	}

	@Override
	public String getAccessTokenEndpoint() {
//		return "https://graph.qq.com/oauth2.0/token?grant_type=" + OAuthEncoder.encode("authorization_code");
		return "https://graph.qq.com/oauth2.0/token";
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		// Append scope if present
		if (config.hasScope()) {
			return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.getScope()));
		} else {
			return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
		}
	}

	@Override
	public OAuthService createService(OAuthConfig config) {
		 return new QQServiceImpl(this, config);
	}
	
	
}
