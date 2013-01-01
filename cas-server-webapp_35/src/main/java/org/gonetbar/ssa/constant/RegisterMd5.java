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
		// TODO Auto-generated method stub

	}

	public static String getRegisterMd5(String providerId, String code, String typedId) {
		return KeyedDigestMD5.getKeyedDigest(providerId + code + typedId, SystemPropertiesUtils.globals.getString("THIRD_REG_MD5_KEY"), "UTF-8");
	}

}
