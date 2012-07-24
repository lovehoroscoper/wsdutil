package org.gonetbar.ssa.oauth.sina;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;


public class SinaWeiboApi2022 extends DefaultApi20 {
	private static final String AUTHORIZE_URL = "https://api.weibo.com/oauth2/authorize?client_id=%s&redirect_uri=%s&response_type=code";
	private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";

	@Override
	public String getAccessTokenEndpoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		// TODO Auto-generated method stub
		return null;
	}

	// @Override
	// public Verb getAccessTokenVerb() {
	// return Verb.POST;
	// }
	//
	// @Override
	// public AccessTokenExtractor getAccessTokenExtractor() {
	// return new JsonTokenExtractor();
	// }
	//
	// @Override
	// public String getAccessTokenEndpoint() {
	// return
	// "https://api.weibo.com/oauth2/access_token?grant_type=authorization_code";
	// }
	//
	// @Override
	// public String getAuthorizationUrl(OAuthConfig config) {
	// // Append scope if present
	// if (config.hasScope()) {
	// return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(),
	// OAuthEncoder.encode(config.getCallback()),
	// OAuthEncoder.encode(config.getScope()));
	// } else {
	// return String.format(AUTHORIZE_URL, config.getApiKey(),
	// OAuthEncoder.encode(config.getCallback()));
	// }
	// }
}