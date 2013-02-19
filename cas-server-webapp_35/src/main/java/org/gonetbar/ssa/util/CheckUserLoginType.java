package org.gonetbar.ssa.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gonetbar.ssa.constant.UserLoginAttr;
import org.gonetbar.ssa.constant.UserLoginType;
import org.scribe.up.profile.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.godtips.common.UtilRegex;
import com.godtips.common.UtilString;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2013-1-8 下午2:28:47
 */
public class CheckUserLoginType {

	private static Logger logger = LoggerFactory.getLogger(CheckUserLoginType.class);

	public static String getLoginTypeByUid(String uid) {
		if (UtilString.isEmptyOrNullByTrim(uid)) {
			return "";
		}
		String[] u_arr = uid.split(UserProfile.SEPARATOR);
		if (UtilRegex.checkEmail(uid)) {
			return UserLoginType.LOGIN_TYPE_LOCAL;
		} else if (null != u_arr && u_arr.length == 2) {
			return UserLoginType.LOGIN_TYPE_OAUTH;
		} else {
			return "";
		}
	}

	public static String getProviderTypeByUid(String uid) {
		if (UtilString.isEmptyOrNullByTrim(uid)) {
			return "";
		}
		String[] u_arr = uid.split(UserProfile.SEPARATOR);
		if (null != u_arr && u_arr.length == 2) {
			return u_arr[0];
		} else {
			return "";
		}
	}

	public static String getThirdUserIdTypeByUid(String uid) {
		if (UtilString.isEmptyOrNullByTrim(uid)) {
			return "";
		}
		String[] u_arr = uid.split(UserProfile.SEPARATOR);
		if (null != u_arr && u_arr.length == 2) {
			return u_arr[1];
		} else {
			return "";
		}
	}

	@SuppressWarnings("rawtypes")
	public static Map<String, String> getLoginMainAttr(Object attr_obj) {
		Map<String, String> m = new HashMap<String, String>();
		if (null != attr_obj && attr_obj instanceof ArrayList) {
			List list = (List) attr_obj;
			for (int i = 0; i < list.size(); i++) {
				Object one_attr = list.get(i);
				if (one_attr instanceof String) {
					String str = (String) one_attr;
					if (UtilString.notEmptyOrNullByTrim(str)) {
						String[] one_arry = str.trim().split("[\n]");
						if (null != one_arry && one_arry.length > 0) {
							String key = UtilString.getStringFromEmpty(one_arry[0]);
							String value = "";
							if (one_arry.length > 1) {
								value = UtilString.getStringFromEmpty(one_arry[1]);
							}
							m.put(key, value);
						}
					}
				} else {
					logger.error("getLoginMainAttr中为Object属性[" + one_attr + "]");
				}
			}
		} else if (attr_obj instanceof String) {
			String attr_str = (String) attr_obj;
			if (UtilString.notEmptyOrNullByTrim(attr_str)) {
				String[] attr_arry = attr_str.trim().split("[\n]");
				for (int i = 0; i < attr_arry.length; i++) {
					String key = UtilString.getStringFromEmpty(attr_arry[i]);
					if (UserLoginAttr.THIRD_LOGIN_PROVIDERID.equals(key) || UserLoginAttr.USER_THIRD_UNIQUEKEY.equals(key)) {
						m.put(key, UtilString.getStringFromEmpty(attr_arry[i + 1]));
					}
				}
			}
		}
		return m;
	}

}
