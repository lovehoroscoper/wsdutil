package com.wode.base.util.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.godtips.common.UtilString;
import com.wode.base.entity.ModelRecordStrUtil;
import com.wode.base.entity.user.UserInfo;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2012-6-5 下午3:14:16
 */
public class UserValid {

	public static final String VALID_IS_MYSELF_NO = "no";
	public static final String VALID_IS_MYSELF_YES = "yes";

	public static final boolean VALID_USER_NULL_FALSE = false;
	public static final boolean VALID_USER_NULL_TRUE = true;

	/**
	 * 验证userID是否合法
	 * 
	 * @param userId
	 * @return
	 */
	public static String validUserId(String userId) {
		String valid_res = "";
		return valid_res;
	}

	/**
	 * 查找认证中心
	 * 
	 * @param request
	 * @return
	 */
	public static String getUserSSOSeqid(HttpServletRequest request) {
		String sso_Seqid = "";
		HttpSession session = request.getSession(false);
		if (null != session) {
			UserInfo login_user = (UserInfo) session.getAttribute(ModelRecordStrUtil.SESS_LOGIN_USER);
			if (null != login_user) {
				sso_Seqid = login_user.getUSER_LOCAL_ID();
			}
		}
		if (UtilString.isEmptyOrNullByTrim(sso_Seqid)) {
			sso_Seqid = "";
		}
		return sso_Seqid;
	}

	/**
	 * 查找子系统的Seqid
	 * 
	 * @param request
	 * @return
	 */
	public static String getUserSubSeqid(HttpServletRequest request) {
		String subSeqid = "";
		HttpSession session = request.getSession(false);
		if (null != session) {
			UserInfo login_user = (UserInfo) session.getAttribute(ModelRecordStrUtil.SESS_LOGIN_USER);
			if (null != login_user) {
				subSeqid = login_user.getSubSeqid();
			}
		}
		if (UtilString.isEmptyOrNullByTrim(subSeqid)) {
			subSeqid = "";
		}
		return subSeqid;
	}

	public static String validLoginIsmyself(HttpServletRequest request, String needUserId) {
		String ismyself = UserValid.VALID_IS_MYSELF_NO;
		HttpSession session = request.getSession(false);
		if (null != session && UtilString.notEmptyOrNullByTrim(needUserId)) {
			UserInfo login_user = (UserInfo) session.getAttribute(ModelRecordStrUtil.SESS_LOGIN_USER);
			if (null != login_user && needUserId.equals(login_user.getSubUserKey())) {
				ismyself = UserValid.VALID_IS_MYSELF_YES;
			}
		}
		return ismyself;
	}

	public static boolean validUserNotNull(UserInfo find_user) {
		boolean ismyself = UserValid.VALID_USER_NULL_FALSE;
		if (null != find_user && UtilString.notEmptyOrNullByTrim(find_user.getSubUserKey())) {
			ismyself = UserValid.VALID_USER_NULL_TRUE;
		}
		return ismyself;
	}

}
