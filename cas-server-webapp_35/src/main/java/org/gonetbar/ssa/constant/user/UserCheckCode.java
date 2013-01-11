package org.gonetbar.ssa.constant.user;

/**
 * @desc 绑定用户验证多种情况
 * 
 *       1、{第三方帐号存在的,返回失败}
 * 
 *       2、{第三方帐号不存在,则添加}
 * 
 *       3、{我方帐号不存在,则添加}
 * 
 *       4、{我方帐号存在,则需要验证密码}
 *       
 *       5、{我方帐号,是否绑定过该类型的第三方账户,即每个第三方类型只能绑定一个}
 * 
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2013-1-10 下午2:01:48
 */
public class UserCheckCode {

	/**
	 * 用户名不存在
	 */
	public static final String CUN_0000 = "CUN0000";

	/**
	 * 我方用户名存在
	 */
	public static final String CUN_0001 = "CUN0001";

	/**
	 * 第三方用户名存在
	 */
	public static final String CUN_0002 = "CUN0002";
	
	/**
	 * 我方用户名存在,且已经绑定过该第三方账户
	 */
	public static final String CUN_0003 = "CUN0003";

	/**
	 * 密码不匹配
	 */
	public static final String CPASS_0003 = "CPASS0003";
	
	/**
	 * 异常
	 */
	public static final String CUN_1111 = "CUN_1111";

}
