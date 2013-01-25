package org.gonetbar.ssa.constant;

import com.godtips.common.KeyedDigestMD5;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2013-1-1 下午3:42:27
 */
public class LoginInitMd5 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	public static String getLoginInitMd5(String key, String secret, String type, String keyStr) {
		return KeyedDigestMD5.getKeyedDigest(key + secret + keyStr + type, SystemPropertiesUtils.globals.getString("LOGIN_INIT_MD5_KEY"), "UTF-8");
	}

}
