package com.godtips.sso.acl.constant;

/**
 * @desc 描述：
 * 
 *       需要与ehcache.xml中配置的cache名称一致
 * 
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2012-11-26 下午3:58:42
 */
public class AclCachePreKey {

	/**
	 * 用户权限:userdetail
	 */
	public final static String CACHE_USERDETAILS_KEY_USERDETAIL = "userdetail@";

	/**
	 * 用户权限:authority
	 */
	public final static String CACHE_USERDETAILS_KEY_AUTHORITY = "authority@";

	/**
	 * 菜单:menu_all
	 */
	public final static String CACHE_MENU_KEY_ALL = "menu_all@";

	/**
	 * 菜单:menu_one
	 */
	public final static String CACHE_MENU_KEY_ONE = "menu_one@";

	/**
	 * 权限:role_menu
	 */
	public final static String CACHE_ROLE_KEY_MENU = "role_menu@";

	/**
	 * 权限:role_default
	 */
	public final static String CACHE_ROLE_KEY_DEFAULT = "role_default@";

}
