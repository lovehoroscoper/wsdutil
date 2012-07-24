package org.gonetbar.ssa.oauth.sina;

import org.scribe.up.profile.UserProfile;
import org.scribe.up.provider.BaseOAuth20Provider;


public class SinaWeiboProvider2 extends BaseOAuth20Provider {

	@Override
	protected void internalInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getProfileUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected UserProfile extractUserProfile(String body) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	protected void internalInit() {
//		if (scope != null) {
//			service = new ServiceBuilder().provider(SinaWeiboApi2022.class).apiKey(key).apiSecret(secret).callback(callbackUrl).scope(scope).build();
//		} else {
//			service = new ServiceBuilder().provider(SinaWeiboApi2022.class).apiKey(key).apiSecret(secret).callback(callbackUrl).build();
//		}
//		String[] names = new String[] { "uid", "username" };
//		for (String name : names) {
//			mainAttributes.put(name, null);
//		}
//
//	}
//
//	@Override
//	protected String getProfileUrl() {
//		return "https://api.weibo.com/2/statuses/user_timeline.json";
//	}
//
//	@Override
//	protected UserProfile extractUserProfile(String body) {
//		UserProfile userProfile = new UserProfile();
//		JsonNode json = JsonHelper.getFirstNode(body);
//		ArrayNode statuses = (ArrayNode) json.get("statuses");
//		JsonNode userJson = statuses.get(0).get("user");
//		if (json != null) {
//			UserProfileHelper.addIdentifier(userProfile, userJson, "id");
//			for (String attribute : mainAttributes.keySet()) {
//				UserProfileHelper.addAttribute(userProfile, json, attribute, mainAttributes.get(attribute));
//			}
//		}
//		JsonNode subJson = userJson.get("id");
//		if (subJson != null) {
//			UserProfileHelper.addAttribute(userProfile, "uid", subJson.getIntValue());
//
//		}
//		subJson = userJson.get("domain");
//		if (subJson != null) {
//			UserProfileHelper.addAttribute(userProfile, "username", subJson.getTextValue());
//		}
//
//		return userProfile;
//	}

}
