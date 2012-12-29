package org.scribe.up.profile.weibo;

import org.scribe.up.profile.AttributesDefinition;
import org.scribe.up.profile.converter.Converters;

public class WeiboAttributesDefinition extends AttributesDefinition {

	public static final String DISPLAY_NAME = "display_name";
	public static final String USERNAME = "username";
	public static final String EMAIL = "email";
	public static final String PRIMARY_BLOG = "primary_blog";
	public static final String AVATAR_URL = "avatar_URL";
	public static final String PROFILE_URL = "profile_URL";

	public WeiboAttributesDefinition() {
		addAttribute(DISPLAY_NAME, Converters.stringConverter);
		addAttribute(USERNAME, Converters.stringConverter);
		addAttribute(EMAIL, Converters.stringConverter);
		addAttribute(PRIMARY_BLOG, Converters.integerConverter);
		addAttribute(AVATAR_URL, Converters.urlConverter);
		addAttribute(PROFILE_URL, Converters.urlConverter);
	}
}
