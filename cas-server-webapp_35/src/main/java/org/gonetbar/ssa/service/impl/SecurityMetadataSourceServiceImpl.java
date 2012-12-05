package org.gonetbar.ssa.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.gonetbar.ssa.cache.SsoCacheManager;
import org.gonetbar.ssa.constant.CacheName;
import org.gonetbar.ssa.constant.CachePreKey;
import org.gonetbar.ssa.constant.ConfigCasKeys;
import org.gonetbar.ssa.constant.SystemPropertiesUtils;
import org.gonetbar.ssa.dao.SecurityMetadataSourceDao;
import org.gonetbar.ssa.entity.DbRoleAttribute;
import org.gonetbar.ssa.entity.MatcherInfo;
import org.gonetbar.ssa.service.SecurityMetadataSourceService;
import org.gonetbar.ssa.util.CommUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.AntPathRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;
import org.springframework.stereotype.Service;

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
		String cache_key = CachePreKey.CACHE_MENU_KEY_ALL + subsysid;
		List<MatcherInfo> list = (List<MatcherInfo>) SsoCacheManager.get(CacheName.CACHE_MENU, cache_key);
		if (null == list) {
			MatcherInfo queryVo = new MatcherInfo();
			queryVo.setSubsysid(subsysid);
			list = securityMetadataSourceDao.queryRequestUrlList(queryVo);
			SsoCacheManager.set(CacheName.CACHE_MENU, cache_key, (Serializable) list);
		}
		return list;
	}

	@Override
	public MatcherInfo queryConfigAttributeCollectionKey(String url, HttpServletRequest request) {
		String subsysid = CommUtils.getStringFromEmpty(SystemPropertiesUtils.globals.getString(ConfigCasKeys.SUBSYSID_DEF));
		String cache_key = CachePreKey.CACHE_MENU_KEY_ONE + subsysid + "#" + url;
		MatcherInfo resVo = SsoCacheManager.get(MatcherInfo.class, CacheName.CACHE_MENU, cache_key);
		if (null == resVo) {
			resVo = queryMatcherVoByKey(subsysid, request);
			SsoCacheManager.set(CacheName.CACHE_MENU, cache_key, resVo);
		}
		return resVo;
	}
	
	private MatcherInfo queryMatcherVoByKey(String subsysid, HttpServletRequest request) {
		long start = System.currentTimeMillis();
		long end = start;
		List<MatcherInfo> list = queryRequestUrlList(subsysid);
		if (null != list) {
			//TODO 这里如果数据量大可能会循环很久
			//String url = getRequestPath(request);			
			for (MatcherInfo requestMatcherVo : list) {
				MatcherInfo matcherVo = (MatcherInfo) requestMatcherVo;
				RequestMatcher matcher = new AntPathRequestMatcher(matcherVo.getLinkurl());
				if (matcher.matches(request)) {
					end = System.currentTimeMillis();
					long s = (end - start)/1000;
					if(s > 3){
						logger.error("------------------匹配URL[" + getRequestPath(request) + "]时间超过限制");
					}
					return matcherVo;
				}
			}
			end = System.currentTimeMillis();
			long s = (end - start)/1000;
			if(s > 3){
				logger.error("------------------匹配URL[" + getRequestPath(request) + "]时间超过限制");
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigAttribute> queryConfigAttributeCollectionValue(String key, MatcherInfo queryVo) {
		String cache_key = CachePreKey.CACHE_ROLE_KEY_MENU + queryVo.getMenuid();
		List<ConfigAttribute> list = (List<ConfigAttribute>) SsoCacheManager.get(CacheName.CACHE_ROLE, cache_key);
		if (null == list) {
			list = securityMetadataSourceDao.queryConfigAttributeCollectionValue(queryVo);
			SsoCacheManager.set(CacheName.CACHE_ROLE, cache_key, (Serializable) list);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigAttribute> queryConfigAttributeCollectionNull(DbRoleAttribute queryVo) {
		String cache_key = CachePreKey.CACHE_ROLE_KEY_DEFAULT + "";
		List<ConfigAttribute> list = (List<ConfigAttribute>) SsoCacheManager.get(CacheName.CACHE_ROLE, cache_key);
		if (null == list) {
			list = securityMetadataSourceDao.queryConfigAttributeCollectionNull(queryVo);
			SsoCacheManager.set(CacheName.CACHE_ROLE, cache_key, (Serializable) list);
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
