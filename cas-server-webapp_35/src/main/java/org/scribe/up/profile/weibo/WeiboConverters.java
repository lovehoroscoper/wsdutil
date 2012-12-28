package org.scribe.up.profile.weibo;

import org.scribe.up.profile.converter.JsonObjectConverter;

public final class WeiboConverters {
	public final static JsonObjectConverter linksConverter = new JsonObjectConverter(WeiboLinks.class);
}
