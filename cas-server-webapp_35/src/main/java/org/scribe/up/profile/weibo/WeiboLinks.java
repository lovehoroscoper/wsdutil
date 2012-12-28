package org.scribe.up.profile.weibo;

import java.io.Serializable;

import org.codehaus.jackson.JsonNode;
import org.scribe.up.profile.JsonObject;
import org.scribe.up.profile.converter.Converters;

public class WeiboLinks extends JsonObject implements Serializable {

	private static final long serialVersionUID = -6237109518285832256L;

	private String self;

	private String help;

	private String site;

	public WeiboLinks(Object json) {
		super(json);
	}

	@Override
	protected void buildFromJson(JsonNode json) {
		this.self = Converters.urlConverter.convertFromJson(json, "self");
		this.help = Converters.urlConverter.convertFromJson(json, "help");
		this.site = Converters.urlConverter.convertFromJson(json, "site");
	}

	public String getSelf() {
		return self;
	}

	public String getHelp() {
		return help;
	}

	public String getSite() {
		return site;
	}
}
