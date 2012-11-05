package org.gonetbar.ssa.dao.impl;

import java.util.List;

import org.gonetbar.ssa.dao.SecurityMetadataSourceDao;
import org.gonetbar.ssa.entity.DbRoleAttribute;
import org.gonetbar.ssa.entity.MatcherInfo;
import org.springframework.security.access.ConfigAttribute;

public class SecurityMetadataSourceDaoImpl extends BaseDaoImpl implements SecurityMetadataSourceDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<MatcherInfo> queryRequestUrlList(MatcherInfo queryVo) {
		return this.queryList("org.gonetbar.ssa.entity.MatcherInfo.queryRequestUrlListId", queryVo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigAttribute> queryConfigAttributeCollectionValue(MatcherInfo queryVo) {
		return this.queryList("org.gonetbar.ssa.entity.MatcherInfo.queryConfigAttributeCollectionValueId", queryVo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigAttribute> queryConfigAttributeCollectionNull(DbRoleAttribute queryVo) {
		return this.queryList("org.gonetbar.ssa.entity.MatcherInfo.queryConfigAttributeCollectionNullId", queryVo);
	}

}
