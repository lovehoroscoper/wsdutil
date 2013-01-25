package org.scribe.up.profile.qq;

import org.scribe.up.profile.AttributesDefinition;
import org.scribe.up.profile.converter.Converters;

public class QQAttributesDefinition extends AttributesDefinition {

	public static final String RET = "ret";
	public static final String MSG = "msg";
	public static final String NICKNAME = "nickname";
	public static final String FIGUREURL = "figureurl";
	public static final String FIGUREURL_1 = "figureurl_1";
	public static final String FIGUREURL_2 = "figureurl_2";
	public static final String GENDER = "gender";
	public static final String VIP = "vip";
	public static final String LEVEL = "level";
	public static final String IS_YELLOW_YEAR_VIP = "is_yellow_year_vip";

	public QQAttributesDefinition() {
		addAttribute(RET, Converters.stringConverter);
		addAttribute(MSG, Converters.stringConverter);
		addAttribute(NICKNAME, Converters.stringConverter);
		addAttribute(FIGUREURL, Converters.stringConverter);
		addAttribute(FIGUREURL_1, Converters.stringConverter);
		addAttribute(FIGUREURL_2, Converters.stringConverter);
		addAttribute(GENDER, Converters.stringConverter);
		addAttribute(VIP, Converters.stringConverter);
		addAttribute(LEVEL, Converters.stringConverter);
		addAttribute(IS_YELLOW_YEAR_VIP, Converters.stringConverter);
	}
}
