package org.scribe.up.profile.qq;

import java.util.Map;

import org.scribe.up.profile.AttributesDefinition;
import org.scribe.up.profile.AttributesDefinitions_sub;
import org.scribe.up.profile.UserProfile;

public class QQProfile extends UserProfile {

	private static final long serialVersionUID = -6217746917499078805L;

	protected AttributesDefinition getAttributesDefinition() {
		return AttributesDefinitions_sub.qqDefinition;
	}

	public QQProfile() {
		super();
	}

	public QQProfile(Object id) {
		super(id);
	}

	public QQProfile(Object id, Map<String, Object> attributes) {
		super(id, attributes);
	}

}
