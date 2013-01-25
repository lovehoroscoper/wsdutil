package org.gonetbar.ssa.service.impl;

import org.gonetbar.ssa.cache.SsoCacheManager;
import org.gonetbar.ssa.constant.SsoCacheName;
import org.gonetbar.ssa.constant.SsoCachePreKey;
import org.gonetbar.ssa.service.LogoutCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.godtips.sso.acl.cache.AclCacheManager;
import com.godtips.sso.acl.constant.AclCacheName;
import com.godtips.sso.acl.constant.AclCachePreKey;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2013-1-18 下午2:59:32
 */
public class LogoutCacheServiceImpl implements LogoutCacheService {

	private static Logger logger = LoggerFactory.getLogger(LogoutCacheServiceImpl.class);

	@Override
	public String removeLoginCacheBy(String user_local_uniquekey, String providerId, String thirdUserId) {
		// 查找 本地唯一 主键，已经对应的id号 注意先后
		try {
			// String cache_key = AclCachePreKey.CACHE_USERDETAILS_KEY_AUTHORITY
			// + username_unique;
			AclCacheManager.justEvict(AclCacheName.CACHE_USERDETAILS, AclCachePreKey.CACHE_USERDETAILS_KEY_AUTHORITY + user_local_uniquekey);
			logger.debug("清除用户缓存完成[" + AclCachePreKey.CACHE_USERDETAILS_KEY_AUTHORITY + user_local_uniquekey + "]");
		} catch (Exception e) {
			logger.error("清除用户缓存异常[" + AclCachePreKey.CACHE_USERDETAILS_KEY_AUTHORITY + user_local_uniquekey + "]");
		}
		try {
			// String cache_key =
			// AclCachePreKey.CACHE_USERDETAILS_KEY_USERDETAIL +
			// username_unique;
			AclCacheManager.justEvict(AclCacheName.CACHE_USERDETAILS, AclCachePreKey.CACHE_USERDETAILS_KEY_USERDETAIL + user_local_uniquekey);
			logger.debug("清除用户缓存完成[" + AclCachePreKey.CACHE_USERDETAILS_KEY_USERDETAIL + user_local_uniquekey + "]");
		} catch (Exception e) {
			logger.error("清除用户缓存异常[" + AclCachePreKey.CACHE_USERDETAILS_KEY_USERDETAIL + user_local_uniquekey + "]");
		}
		try {
			// String cache_key = SsoCachePreKey.CACHE_USER_KEY_THIRD +
			// providerId + "#" + thirdUserId;
			SsoCacheManager.justEvict(SsoCacheName.CACHE_USER, SsoCachePreKey.CACHE_USER_KEY_THIRD + providerId + "#" + thirdUserId);
			logger.debug("清除用户缓存完成[" + SsoCachePreKey.CACHE_USER_KEY_THIRD + providerId + "#" + thirdUserId + "]");
		} catch (Exception e) {
			logger.error("清除用户缓存异常[" + SsoCachePreKey.CACHE_USER_KEY_THIRD + providerId + "#" + thirdUserId + "]");
		}
		try {
			// String cache_key = SsoCachePreKey.CACHE_USER_KEY_LOACL +
			// findVo.getUsername();
			SsoCacheManager.justEvict(SsoCacheName.CACHE_USER, SsoCachePreKey.CACHE_USER_KEY_LOACL + user_local_uniquekey);
			logger.debug("清除用户缓存完成[" + SsoCachePreKey.CACHE_USER_KEY_LOACL + user_local_uniquekey + "]");
		} catch (Exception e) {
			logger.error("清除用户缓存异常[" + SsoCachePreKey.CACHE_USER_KEY_LOACL + user_local_uniquekey + "]");
		}
		return null;
	}

}
