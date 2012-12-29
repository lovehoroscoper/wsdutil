package org.scribe.up.provider.impl;

import org.codehaus.jackson.JsonNode;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.SinaWeiboApi20;
import org.scribe.model.Token;
import org.scribe.up.profile.AttributesDefinitions_sub;
import org.scribe.up.profile.JsonHelper;
import org.scribe.up.profile.UserProfile;
import org.scribe.up.profile.weibo.WeiboProfile;
import org.scribe.up.provider.BaseOAuth20Provider;
import org.scribe.up.util.StringHelper;

public class WeiboProvider extends BaseOAuth20Provider {

	@Override
	protected void internalInit() {
		if (scope != null) {
			service = new ServiceBuilder().provider(SinaWeiboApi20.class).apiKey(key).apiSecret(secret).callback(callbackUrl).scope(scope).build();
		} else {
			service = new ServiceBuilder().provider(SinaWeiboApi20.class).apiKey(key).apiSecret(secret).callback(callbackUrl).build();
		}
	}

	@Override
	protected String getProfileUrl() {
		// 不按照网上那个获取默认的,使用这个需要uid
		// https://api.weibo.com/2/account/get_uid.json
		return "https://api.weibo.com/2/users/show.json";
	}

	@Override
	public UserProfile getUserProfile(Token accessToken) {
		// get the guid :
		String body = sendRequestForData(accessToken, "https://api.weibo.com/2/account/get_uid.json");
		if (body == null) {
			return null;
		}
		JsonNode uidNode = JsonHelper.getFirstNode("uid");
		logger.debug("uid : {}", uidNode);
		// then the profile with the uid
		if (null != uidNode && StringHelper.isNotBlank(uidNode.getTextValue())) {
			body = sendRequestForData(accessToken, getProfileUrl() + "?uid=" + uidNode.getTextValue());
			if (body == null) {
				return null;
			}
		}
		UserProfile profile = extractUserProfile(body);
		addAccessTokenToProfile(profile, accessToken);
		return profile;
	}

	@Override
	protected UserProfile extractUserProfile(String body) {
		WeiboProfile profile = new WeiboProfile();
		JsonNode userJson = JsonHelper.getFirstNode(body);
		if (userJson != null) {
			Object id_obj = JsonHelper.get(userJson, "id");
			profile.setId(id_obj);
			for (String attribute : AttributesDefinitions_sub.weiboDefinition.getAttributes()) {
				if (AttributesDefinitions_sub.weiboDefinition.isPrimary(attribute)) {
					profile.addAttribute(attribute, JsonHelper.get(userJson, attribute));
				}
			}
			JsonNode subNode = null;
			if (null != id_obj) {
				subNode = (JsonNode) id_obj;
				profile.addAttribute("uid", subNode.getIntValue());
			}
			subNode = userJson.get("screen_name");// 用户昵称
			if (null != subNode) {
				profile.addAttribute("username", subNode.getTextValue());
			}
		}
		return profile;
	}
}
