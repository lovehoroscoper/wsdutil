package org.gonetbar.ssa.constant;

/**
 * @desc 描述：
 * 
 *       需要与ehcache.xml中配置的cache名称一致
 * 
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2012-11-26 下午3:58:42
 */
public class SsoCachePreKey {

	//用户登录来源
	public final static String CACHE_USER_KEY_LOACL = "local@";
	public final static String CACHE_USER_KEY_THIRD = "third@";
	//OAUTH登录类型缓存
	public final static String CACHE_PROVIDER_KEY_OAUTH = "oauth@";

}
