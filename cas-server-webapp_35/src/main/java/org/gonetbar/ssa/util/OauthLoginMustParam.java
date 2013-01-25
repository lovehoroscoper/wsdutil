package org.gonetbar.ssa.util;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gonetbar.ssa.constant.LoginInitMd5;
import org.gonetbar.ssa.constant.Oauth20Attr;

/**
 * @desc 描述：用于同一构造第三方登录必须参数
 * 
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2013-1-22 下午1:34:20
 */
public class OauthLoginMustParam {

	public static String getMd5State(final HttpServletRequest request, String type, boolean isSetState) {
		String keyStr = UUID.randomUUID().toString();
		String state = LoginInitMd5.getLoginInitMd5("", "", type, keyStr);
		HttpSession session = request.getSession();
		session.setAttribute(Oauth20Attr.OAUTH_KEYSTR, keyStr);
		if(isSetState){
			session.setAttribute(Oauth20Attr.OAUTH_STATE, state);
		}
		return state;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
