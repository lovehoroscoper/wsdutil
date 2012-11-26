package org.gonetbar.ssa.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;

/**
 * 
 * @author Administrator
 * 
 */
public class SsoCacheManager {

	private static Logger logger = LoggerFactory.getLogger(SsoCacheManager.class);

	private static CacheManager cacheManager;

	public void setCacheManager(CacheManager cacheManager) {
		SsoCacheManager.cacheManager = cacheManager;
	}
	

}
