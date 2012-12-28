package org.scribe.up.provider.impl;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.SinaWeiboApi20;
import org.scribe.up.profile.AttributesDefinitions_sub;
import org.scribe.up.profile.JsonHelper;
import org.scribe.up.profile.UserProfile;
import org.scribe.up.profile.weibo.WeiboProfile;
import org.scribe.up.provider.BaseOAuth20Provider;

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
		return "https://api.weibo.com/2/statuses/user_timeline.json";
	}

	@Override
	protected UserProfile extractUserProfile(String body) {
		WeiboProfile profile = new WeiboProfile();
		JsonNode json_all = JsonHelper.getFirstNode(body);
		if (json_all != null) {
			ArrayNode statuses = (ArrayNode) json_all.get("statuses");
			// TODO statuses.get(0) 会不会有空值返回
			JsonNode userJson = statuses.get(0).get("user");
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
