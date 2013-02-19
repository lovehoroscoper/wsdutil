package org.gonetbar.ssa.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.gonetbar.ssa.constant.SystemPropertiesUtils;
import org.gonetbar.ssa.constant.UserLoginType;
import org.gonetbar.ssa.des.SpringPropertiesDbDesUtil;
import org.gonetbar.ssa.entity.UserProviderInfoVo;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jgroups.util.UUID;

import com.godtips.common.UtilDate;
import com.godtips.common.UtilMsgFmt;
import com.godtips.common.UtilString;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2013-2-2 下午4:10:24
 */
public class LgotucaCookie {

	private static final int header_len = 4;

	public static String encodeCookieValue(String user_local_uniquekey, String third_login_providerid, String user_third_uniquekey, String loginTGT) {
		String key = SystemPropertiesUtils.globals.getString("LGOTUCA_COOKIE_DES_KEY");
		if (UtilString.isEmptyOrNullByTrim(key) || UtilString.isEmptyOrNullByTrim(user_local_uniquekey) || UtilString.isEmptyOrNullByTrim(loginTGT)) {
			return "";
		}
		// 头4长度+body+头4长度+body
		StringBuffer sb = new StringBuffer();
		int u_l_u_len = user_local_uniquekey.length();
		int t_l_p_len = third_login_providerid.length();
		int u_t_u_len = user_third_uniquekey.length();
		String dateStr = UtilDate.getFormatCurrDate("yyyyMMddHHmmss");
		int d_len = dateStr.length();
		String res = "";
		sb.append(UtilMsgFmt.fmtSupplyZeroToLeft(u_l_u_len + "", header_len)).append(user_local_uniquekey).append(UtilMsgFmt.fmtSupplyZeroToLeft(t_l_p_len + "", header_len))
				.append(third_login_providerid).append(UtilMsgFmt.fmtSupplyZeroToLeft(u_t_u_len + "", header_len)).append(user_third_uniquekey)
				.append(UtilMsgFmt.fmtSupplyZeroToLeft(d_len + "", header_len)).append(dateStr);
		try {
			String code = SpringPropertiesDbDesUtil.getEncodeByInfo(sb.toString(), loginTGT + SystemPropertiesUtils.globals.getString("LGOTUCA_COOKIE_DES_KEY"));
			res = URLEncoder.encode(code, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			res = "";
		}
		return res;
	}

	public static UserProviderInfoVo decodeCookieValue(String cookieValue, String loginTGT) {
		UserProviderInfoVo vo = new UserProviderInfoVo();
		String key = SystemPropertiesUtils.globals.getString("LGOTUCA_COOKIE_DES_KEY");
		if (UtilString.isEmptyOrNullByTrim(key) || UtilString.isEmptyOrNullByTrim(cookieValue) || UtilString.isEmptyOrNullByTrim(loginTGT)) {
			return vo;
		}
		String t_value = "";
		try {
			String t_cookieValue = URLDecoder.decode(cookieValue, "UTF-8");
			t_value = SpringPropertiesDbDesUtil.getDecodeByEncode(t_cookieValue, loginTGT + SystemPropertiesUtils.globals.getString("LGOTUCA_COOKIE_DES_KEY"));
		} catch (Exception e) {
			t_value = "";
		}
		if (UtilString.notEmptyOrNullByTrim(t_value)) {
			splitMsg(t_value, vo);
		}
		return vo;
	}

	private static void splitMsg(String t_value, UserProviderInfoVo vo) {
		try {
			if (t_value.length() > 3) {
				String newStr = t_value;
				int length = Integer.valueOf(newStr.substring(0, 4)) + 4;
				String user_local_uniquekey = newStr.substring(4, length);
				newStr = newStr.substring(length, newStr.length());

				length = Integer.valueOf(newStr.substring(0, 4)) + 4;
				String third_login_providerid = newStr.substring(4, length);
				newStr = newStr.substring(length, newStr.length());

				length = Integer.valueOf(newStr.substring(0, 4)) + 4;
				String user_third_uniquekey = newStr.substring(4, length);
				newStr = newStr.substring(length, newStr.length());

				vo.setUsername(user_local_uniquekey);
				vo.setProviderid(third_login_providerid);
				vo.setThirduserid(user_third_uniquekey);
			}
		} catch (Exception e) {

		}
	}

	public static void setCookie(String loginType, HttpServletResponse response, String p_user_local_uniquekey, TicketGrantingTicket ticket) {
		if (UserLoginType.LOGIN_TYPE_LOCAL.equals(loginType)) {

		} else if (UserLoginType.LOGIN_TYPE_OAUTH.equals(loginType)) {

		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String tgt = UUID.randomUUID().toString();
		String user_local_uniquekey = "111111111111111@163.com";
		String third_login_providerid = "1234";
		String user_third_uniquekey = "DFESDFESSSSSSSSSSSSSSSSSSS";
		// String cookieValue = encodeCookieValue(user_local_uniquekey,
		// third_login_providerid, user_third_uniquekey, tgt);
		// System.out.println(user_local_uniquekey);
		// System.out.println(third_login_providerid);
		// System.out.println(user_third_uniquekey);
		// System.out.println(cookieValue);

		String cookieValue = "wpvCJy5kXuqXU%2Fspu5kr%2BR9hN6SP74kfub34T3pkd6kgwGTNE0xi9Wpvouh%2Fnk2T";

		tgt = "";
		cookieValue = "";

		UserProviderInfoVo vo = decodeCookieValue_test(cookieValue, tgt);
		System.out.println(vo.getUsername());
		System.out.println(vo.getProviderid());
		System.out.println(vo.getThirduserid());

	}

	public static UserProviderInfoVo decodeCookieValue_test(String cookieValue, String loginTGT) {
		UserProviderInfoVo vo = new UserProviderInfoVo();
		String key = "";
		if (UtilString.isEmptyOrNullByTrim(key) || UtilString.isEmptyOrNullByTrim(cookieValue) || UtilString.isEmptyOrNullByTrim(loginTGT)) {
			return vo;
		}
		String t_value = "";
		try {
			String t_cookieValue = URLDecoder.decode(cookieValue, "UTF-8");
			t_value = SpringPropertiesDbDesUtil.getDecodeByEncode(t_cookieValue, loginTGT + key);
		} catch (Exception e) {
			t_value = "";
		}
		if (UtilString.notEmptyOrNullByTrim(t_value)) {
			splitMsg(t_value, vo);
		}
		return vo;
	}

}
