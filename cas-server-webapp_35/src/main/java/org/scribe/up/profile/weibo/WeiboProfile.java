package org.scribe.up.profile.weibo;

import java.util.Map;

import org.scribe.up.profile.AttributesDefinition;
import org.scribe.up.profile.AttributesDefinitions_sub;
import org.scribe.up.profile.UserProfile;

public class WeiboProfile extends UserProfile {

	private static final long serialVersionUID = -6217746917499078805L;

	protected AttributesDefinition getAttributesDefinition() {
		return AttributesDefinitions_sub.weiboDefinition;
	}

	public WeiboProfile() {
		super();
	}

	public WeiboProfile(Object id) {
		super(id);
	}

	public WeiboProfile(Object id, Map<String, Object> attributes) {
		super(id, attributes);
	}

	public String getDisplayName() {
		return (String) attributes.get(WeiboAttributesDefinition.DISPLAY_NAME);
	}

	public String getUsername() {
		return (String) attributes.get(WeiboAttributesDefinition.USERNAME);
	}

	public String getEmail() {
		return (String) attributes.get(WeiboAttributesDefinition.EMAIL);
	}

	public int getPrimaryBlog() {
		return getSafeInt((Integer) attributes.get(WeiboAttributesDefinition.PRIMARY_BLOG));
	}

	public boolean isPrimaryBlogDefined() {
		return attributes.get(WeiboAttributesDefinition.PRIMARY_BLOG) != null;
	}

	public String getAvatarUrl() {
		return (String) attributes.get(WeiboAttributesDefinition.AVATAR_URL);
	}

	public String getProfileUrl() {
		return (String) attributes.get(WeiboAttributesDefinition.PROFILE_URL);
	}

	public WeiboLinks getLinks() {
		return (WeiboLinks) attributes.get(WeiboAttributesDefinition.LINKS);
	}
}
