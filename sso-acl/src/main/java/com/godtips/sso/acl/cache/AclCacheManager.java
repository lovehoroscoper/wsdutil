package com.godtips.sso.acl.cache;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2012-11-26 下午7:33:34
 */
public class AclCacheManager {

	private static Logger logger = LoggerFactory.getLogger(AclCacheManager.class);

	private static CacheManager cacheManager;

	public void setCacheManager(CacheManager cacheManager) {
		if (null == cacheManager) {
			logger.error("spring依赖注入cacheManager为空!");
		}
		AclCacheManager.cacheManager = cacheManager;
	}

	private final static Cache _GetCache(String name) {
		return cacheManager.getCache(name);
	}

	private final static Object _get(String name, Serializable key) {
		Object obj = _GetCache(name).get(key);
		if (null != obj) {
			// TODO weisd 为什么要多出这个
			return ((ValueWrapper) obj).get();
		}
		return null;
	}

	public final static Object get(String name, Serializable key) {
		if (name != null && key != null) {
			return _get(name, key);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public final static <T> T get(Class<T> resultClass, String name, Serializable key) {
		if (name != null && key != null) {
			return (T) _get(name, key);
		}
		return null;
	}

	public final static void set(String name, Serializable key, Serializable value) {
		if (name != null && key != null && value != null) {
			_GetCache(name).put(key, value);
		}
	}

	public final static void evict(String name, Serializable key) {
		if (name != null && key != null) {
			_GetCache(name).evict(key);
		}
	}

	public final static void justEvict(String name, Serializable key) {
		if (name != null && key != null) {
			Cache cache = _GetCache(name);
			if (cache != null) {
				cache.evict(key);
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
