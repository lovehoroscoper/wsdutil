package com.godtips.sso.acl.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.AntPathRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;
import org.springframework.stereotype.Service;

import com.godtips.common.UtilString;
import com.godtips.sso.acl.cache.AclCacheManager;
import com.godtips.sso.acl.constant.AclCacheName;
import com.godtips.sso.acl.constant.AclCachePreKey;
import com.godtips.sso.acl.dao.SecurityMetadataSourceDao;
import com.godtips.sso.acl.entity.AclGrantedAuthority;
import com.godtips.sso.acl.entity.AclUserVo;
import com.godtips.sso.acl.entity.DbRoleAttribute;
import com.godtips.sso.acl.entity.MatcherInfo;
import com.godtips.sso.acl.service.SecurityMetadataSourceService;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2012-7-29 下午12:05:32
 */
@Service("securityMetadataSourceService")
public class SecurityMetadataSourceServiceImpl implements SecurityMetadataSourceService {

	private static Logger logger = Logger.getLogger(SecurityMetadataSourceServiceImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public List<MatcherInfo> queryRequestUrlList(String subsysid) {
		String cache_key = AclCachePreKey.CACHE_MENU_KEY_ALL + subsysid;
		List<MatcherInfo> list = (List<MatcherInfo>) AclCacheManager.get(AclCacheName.CACHE_MENU, cache_key);
		if (null == list) {
			MatcherInfo queryVo = new MatcherInfo();
			queryVo.setSubsysid(subsysid);
			list = securityMetadataSourceDao.queryRequestUrlList(queryVo);
			AclCacheManager.set(AclCacheName.CACHE_MENU, cache_key, (Serializable) list);
		}
		return list;
	}

	@Override
	public MatcherInfo queryConfigAttributeCollectionKey(String systemid, String url, HttpServletRequest request) {
		String cache_key = AclCachePreKey.CACHE_MENU_KEY_ONE + systemid + "#" + url;
		MatcherInfo resVo = AclCacheManager.get(MatcherInfo.class, AclCacheName.CACHE_MENU, cache_key);
		if (null == resVo) {
			resVo = queryMatcherVoByKey(systemid, request);
			AclCacheManager.set(AclCacheName.CACHE_MENU, cache_key, resVo);
		}
		return resVo;
	}

	private MatcherInfo queryMatcherVoByKey(String subsysid, HttpServletRequest request) {
		long start = System.currentTimeMillis();
		long end = start;
		List<MatcherInfo> list = queryRequestUrlList(subsysid);
		if (null != list) {
			// TODO 这里如果数据量大可能会循环很久
			// String url = getRequestPath(request);
			for (MatcherInfo requestMatcherVo : list) {
				MatcherInfo matcherVo = (MatcherInfo) requestMatcherVo;
				RequestMatcher matcher = new AntPathRequestMatcher(matcherVo.getLinkurl());
				if (matcher.matches(request)) {
					end = System.currentTimeMillis();
					long s = (end - start) / 1000;
					if (s > 3) {
						logger.error("------------------匹配URL[" + getRequestPath(request) + "]时间超过限制");
					}
					return matcherVo;
				}
			}
			end = System.currentTimeMillis();
			long s = (end - start) / 1000;
			if (s > 3) {
				logger.error("------------------匹配URL[" + getRequestPath(request) + "]时间超过限制");
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigAttribute> queryConfigAttributeCollectionValue(String systemid, String key, MatcherInfo queryVo) {
		String cache_key = AclCachePreKey.CACHE_ROLE_KEY_MENU + systemid + "#" + queryVo.getMenuid();
		List<ConfigAttribute> list = (List<ConfigAttribute>) AclCacheManager.get(AclCacheName.CACHE_ROLE, cache_key);
		if (null == list) {
			list = securityMetadataSourceDao.queryConfigAttributeCollectionValue(queryVo);
			AclCacheManager.set(AclCacheName.CACHE_ROLE, cache_key, (Serializable) list);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigAttribute> queryConfigAttributeCollectionNull(String systemid, DbRoleAttribute queryVo) {
		String cache_key = AclCachePreKey.CACHE_ROLE_KEY_DEFAULT + systemid + "#";
		List<ConfigAttribute> list = (List<ConfigAttribute>) AclCacheManager.get(AclCacheName.CACHE_ROLE, cache_key);
		if (null == list) {
			list = securityMetadataSourceDao.queryConfigAttributeCollectionNull(queryVo);
			AclCacheManager.set(AclCacheName.CACHE_ROLE, cache_key, (Serializable) list);
		}
		return list;
	}

	private String getRequestPath(HttpServletRequest request) {
		String url = request.getServletPath();
		if (request.getPathInfo() != null) {
			url += request.getPathInfo();
		}
		url = url.toLowerCase();
		return url;
	}

	@SuppressWarnings("unchecked")
	public List<AclGrantedAuthority> queryUserAuthorities(long id_user, String username_unique) {
		String cache_key = AclCachePreKey.CACHE_USERDETAILS_KEY_AUTHORITY + username_unique;
		List<AclGrantedAuthority> list = (List<AclGrantedAuthority>) AclCacheManager.get(AclCacheName.CACHE_USERDETAILS, cache_key);
		if (null == list) {
			AclUserVo findVo = new AclUserVo();
			findVo.setId_user(id_user);
			list = securityMetadataSourceDao.queryUserAuthorities(findVo);
			AclCacheManager.set(AclCacheName.CACHE_USERDETAILS, cache_key, (Serializable) list);
		}
		return list;
	}

	@Override
	public MatcherInfo querySubSysId(MatcherInfo queryVo) {
		return null;
	}

	private SecurityMetadataSourceDao securityMetadataSourceDao;

	@Resource(name = "securityMetadataSourceDao")
	public void setSecurityMetadataSourceDao(SecurityMetadataSourceDao securityMetadataSourceDao) {
		this.securityMetadataSourceDao = securityMetadataSourceDao;
	}

}
