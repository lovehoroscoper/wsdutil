package org.scribe.up.provider.impl;

import org.codehaus.jackson.JsonNode;
import org.gonetbar.ssa.constant.Oauth20Attr;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.QQApi20;
import org.scribe.model.Token;
import org.scribe.up.profile.AttributesDefinitions_sub;
import org.scribe.up.profile.JsonHelper;
import org.scribe.up.profile.UserProfile;
import org.scribe.up.profile.qq.QQProfile;
import org.scribe.up.provider.BaseOAuth20Provider;
import org.scribe.up.session.UserSession;
import org.scribe.up.util.StringHelper;

import com.godtips.common.UtilString;

public class QQProvider extends BaseOAuth20Provider {

	public static final String STATE = "state";

	@Override
	protected void internalInit() {
		if (scope != null) {
			service = new ServiceBuilder().provider(QQApi20.class).apiKey(key).apiSecret(secret).callback(callbackUrl).scope(scope).build();
		} else {
			service = new ServiceBuilder().provider(QQApi20.class).apiKey(key).apiSecret(secret).callback(callbackUrl).build();
		}
	}

	@Override
	protected String getProfileUrl() {
		return "https://graph.qq.com/user/get_user_info";
	}

	@Override
	public UserProfile getUserProfile(Token accessToken) {
		String body = sendRequestForData(accessToken, "https://graph.qq.com/oauth2.0/me");
		if (UtilString.isEmptyOrNullByTrim(body)) {
			return null;
		}
		if (body.indexOf("callback") > -1) {
			int lpos = body.indexOf("(");
			int rpos = body.lastIndexOf(")");
			body = body.substring(lpos + 1, rpos);
		}
		JsonNode json = JsonHelper.getFirstNode(body);
		if (null != json) {
			String uid = (String) JsonHelper.get(json, "openid");
			String client_id = (String) JsonHelper.get(json, "client_id");
			logger.debug("client_id : {} , openid : {}", client_id, uid);
			if (StringHelper.isNotBlank(uid) && UtilString.notEmptyOrNullByTrim(client_id) && client_id.equals(key)) {
				body = sendRequestForData(accessToken, getProfileUrl() + "?oauth_consumer_key=" + client_id + "&openid=" + uid);
				if (UtilString.isEmptyOrNullByTrim(body)) {
					return null;
				}
			} else {
				return null;
			}
			UserProfile profile = extractUserProfile(body);
			if (null != profile) {
				profile.setId(UtilString.getStringFromEmpty(uid));
				addAccessTokenToProfile(profile, accessToken);
			}
			return profile;
		}
		return null;
	}

	@Override
	protected UserProfile extractUserProfile(String body) {
		QQProfile profile = new QQProfile();
		JsonNode userJson = JsonHelper.getFirstNode(body);
		if (userJson != null) {
			String ret = JsonHelper.get(userJson, "ret").toString();
			if ("0".equals(ret)) {
				for (String attribute : AttributesDefinitions_sub.qqDefinition.getAttributes()) {
					if (AttributesDefinitions_sub.qqDefinition.isPrimary(attribute)) {
						profile.addAttribute(attribute, JsonHelper.get(userJson, attribute));
					}
				}
			} else {
				return null;
			}
		}
		return profile;
	}

	@Override
	public String getAuthorizationUrl(UserSession session) {
		String authorizationUrl = super.getAuthorizationUrl(session) + "&" + STATE + "=" + session.getAttribute(Oauth20Attr.OAUTH_STATE);
		logger.debug("authorizationUrl : {}", authorizationUrl);
		return authorizationUrl;

	}

}
