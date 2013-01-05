package org.gonetbar.ssa.constant;

import com.godtips.common.KeyedDigestMD5;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2013-1-1 下午3:42:27
 */
public class RegisterMd5 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	public static String getRegisterMd5(String providerId, String code, String typedId, String keyStr) {
		return KeyedDigestMD5.getKeyedDigest(providerId + code + typedId + keyStr, SystemPropertiesUtils.globals.getString("THIRD_REG_MD5_KEY"), "UTF-8");
	}

	public static String getRegisterAfterMd5(String accessToken, String typedId, String keyStr) {
		return KeyedDigestMD5.getKeyedDigest(accessToken + typedId + keyStr, SystemPropertiesUtils.globals.getString("THIRD_REG_AFTER_MD5_KEY"), "UTF-8");
	}

}
