package org.gonetbar.ssa.util;

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

}
