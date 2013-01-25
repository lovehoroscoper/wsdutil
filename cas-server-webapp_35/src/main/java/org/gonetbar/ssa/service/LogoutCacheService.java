package org.gonetbar.ssa.service;

/**
 * 退出清除缓存
 * 
 * @author Administrator
 * 
 */
public interface LogoutCacheService {

	public String removeLoginCacheBy(String user_local_uniquekey, String providerId, String thirdUserId);
}
