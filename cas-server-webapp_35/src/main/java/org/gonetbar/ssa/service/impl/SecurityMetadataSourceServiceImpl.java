package org.gonetbar.ssa.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.gonetbar.ssa.dao.SecurityMetadataSourceDao;
import org.gonetbar.ssa.entity.DbRoleAttribute;
import org.gonetbar.ssa.entity.MatcherInfo;
import org.gonetbar.ssa.service.SecurityMetadataSourceService;
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

	@Override
	public List<MatcherInfo> queryRequestUrlList(String subsysid) {
		// TODO 缓存
		MatcherInfo queryVo = new MatcherInfo();
		queryVo.setSubsysid(subsysid);
		List<MatcherInfo> list = securityMetadataSourceDao.queryRequestUrlList(queryVo);
		if (null == list) {
			list = new ArrayList<MatcherInfo>();
		}
		return list;
	}

	@Override
	public MatcherInfo queryConfigAttributeCollectionKey(String url, HttpServletRequest request) {
		// TODO 缓存
		String subsysid = "1";
		
		System.out.println("------------测试系统---subsysid---------------");
		
		List<MatcherInfo> list = queryRequestUrlList(subsysid);
		if (null != list) {
			for (MatcherInfo requestMatcherVo : list) {
				MatcherInfo matcherVo = (MatcherInfo) requestMatcherVo;
				RequestMatcher matcher = new AntPathRequestMatcher(matcherVo.getLinkurl());
				if (matcher.matches(request)) {
					return matcherVo;
				}
			}
		}
		//return new MatcherInfo();
		return null;
	}

	@Override
	public List<ConfigAttribute> queryConfigAttributeCollectionValue(String key, MatcherInfo queryVo) {
		// TODO 缓存
		List<ConfigAttribute> list = null;
		if (null != queryVo) {
			list = securityMetadataSourceDao.queryConfigAttributeCollectionValue(queryVo);
		}
//		if (null == list) {
//			list = new ArrayList<ConfigAttribute>();
//		}
		return list;
	}

	@Override
	public List<ConfigAttribute> queryConfigAttributeCollectionNull(DbRoleAttribute queryVo) {
		if (null == queryVo) {
			queryVo = new DbRoleAttribute();
		}
		return securityMetadataSourceDao.queryConfigAttributeCollectionNull(queryVo);
	}

	@Override
	public MatcherInfo querySubSysId(MatcherInfo queryVo) {
		// TODO Auto-generated method stub
		return null;
	}

	private SecurityMetadataSourceDao securityMetadataSourceDao;

	@Resource(name = "securityMetadataSourceDao")
	public void setSecurityMetadataSourceDao(SecurityMetadataSourceDao securityMetadataSourceDao) {
		this.securityMetadataSourceDao = securityMetadataSourceDao;
	}

}
