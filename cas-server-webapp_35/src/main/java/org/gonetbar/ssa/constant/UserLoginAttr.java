package org.gonetbar.ssa.constant;

/**
 * @desc 描述：登录后必须设置的基础属性
 * 
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2013-1-8 下午2:32:38
 */
public class UserLoginAttr {

	/**
	 * 我方数据库中id_user 自增长数字类型
	 */
	public static final String USER_LOCAL_ID = "user_local_seqid";

	/**
	 * 注册的唯一邮箱username
	 */
	public static final String USER_LOCAL_UNIQUEKEY = "user_local_uniquekey";

	/**
	 *  UserLoginType.xxx
	 */
	public static final String USER_LOGIN_TYPE = "user_login_type";

	/**
	 * 第三方登录ID
	 */
	public static final String USER_THIRD_UNIQUEKEY = "user_third_uniquekey";

	/**
	 * 第三方登录类型
	 */
	public static final String THIRD_LOGIN_TYPE = "third_login_type";
	
	/**
	 * 第三方登录类型编号
	 */
	public static final String THIRD_LOGIN_PROVIDERID = "third_login_providerid";

}
