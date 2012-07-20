package org.gonetbar.ssa.oauth.sina;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.scribe.builder.ServiceBuilder;
import org.scribe.up.profile.JsonHelper;
import org.scribe.up.profile.UserProfile;
import org.scribe.up.profile.UserProfileHelper;
import org.scribe.up.provider.BaseOAuth20Provider;

public class SinaWeiboProvider extends BaseOAuth20Provider {

	@Override
	protected void internalInit() {
		if (scope != null) {
			service = new ServiceBuilder().provider(SinaWeiboApi20.class).apiKey(key).apiSecret(secret).callback(callbackUrl).scope(scope).build();
		} else {
			service = new ServiceBuilder().provider(SinaWeiboApi20.class).apiKey(key).apiSecret(secret).callback(callbackUrl).build();
		}
		String[] names = new String[] { "uid", "username" };
		for (String name : names) {
			mainAttributes.put(name, null);
		}

	}

	@Override
	protected String getProfileUrl() {
		return "https://api.weibo.com/2/statuses/user_timeline.json";
	}

	@Override
	protected UserProfile extractUserProfile(String body) {
		UserProfile userProfile = new UserProfile();
		JsonNode json = JsonHelper.getFirstNode(body);
		ArrayNode statuses = (ArrayNode) json.get("statuses");
		JsonNode userJson = statuses.get(0).get("user");
		if (json != null) {
			UserProfileHelper.addIdentifier(userProfile, userJson, "id");
			for (String attribute : mainAttributes.keySet()) {
				UserProfileHelper.addAttribute(userProfile, json, attribute, mainAttributes.get(attribute));
			}
		}
		JsonNode subJson = userJson.get("id");
		if (subJson != null) {
			UserProfileHelper.addAttribute(userProfile, "uid", subJson.getIntValue());

		}
		subJson = userJson.get("domain");
		if (subJson != null) {
			UserProfileHelper.addAttribute(userProfile, "username", subJson.getTextValue());
		}

		return userProfile;
	}

}
