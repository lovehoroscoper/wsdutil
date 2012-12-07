package com.godtips.sso.acl.dao.impl;

import java.util.List;

import org.springframework.security.access.ConfigAttribute;

import com.godtips.sso.acl.dao.SecurityMetadataSourceDao;
import com.godtips.sso.acl.entity.AclGrantedAuthority;
import com.godtips.sso.acl.entity.AclUserVo;
import com.godtips.sso.acl.entity.DbRoleAttribute;
import com.godtips.sso.acl.entity.MatcherInfo;

public class SecurityMetadataSourceDaoImpl extends AclBaseDaoImpl implements SecurityMetadataSourceDao {

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

	@SuppressWarnings("unchecked")
	@Override
	public List<AclGrantedAuthority> queryUserAuthorities(AclUserVo findVo) {
		return this.queryList("org.gonetbar.ssa.entity.UserInfoVoMapper.queryUserAuthoritiesId", findVo);
	}

}
