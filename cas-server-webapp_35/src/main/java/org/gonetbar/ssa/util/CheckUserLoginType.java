package org.gonetbar.ssa.util;

import java.util.HashMap;
import java.util.Map;

import org.gonetbar.ssa.constant.UserLoginAttr;
import org.gonetbar.ssa.constant.UserLoginType;
import org.scribe.up.profile.UserProfile;

import com.godtips.common.UtilRegex;
import com.godtips.common.UtilString;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2013-1-8 下午2:28:47
 */
public class CheckUserLoginType {

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

	public static Map<String, String> getLoginMainAttr(String attr_str) {
		Map<String, String> m = new HashMap<String, String>();
		if (UtilString.notEmptyOrNullByTrim(attr_str)) {
			String[] attr_arry = attr_str.trim().split("[\n]");
			for (int i = 0; i < attr_arry.length; i++) {
				String key = UtilString.getStringFromEmpty(attr_arry[i]);
				if (UserLoginAttr.THIRD_LOGIN_PROVIDERID.equals(key) || UserLoginAttr.USER_THIRD_UNIQUEKEY.equals(key)) {
					m.put(key, UtilString.getStringFromEmpty(attr_arry[i + 1]));
				}
			}
		}
		return m;
	}

}
