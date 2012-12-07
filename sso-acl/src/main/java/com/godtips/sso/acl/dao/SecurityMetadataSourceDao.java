package com.godtips.sso.acl.dao;

import java.util.List;

import org.springframework.security.access.ConfigAttribute;

import com.godtips.sso.acl.entity.AclGrantedAuthority;
import com.godtips.sso.acl.entity.AclUserVo;
import com.godtips.sso.acl.entity.DbRoleAttribute;
import com.godtips.sso.acl.entity.MatcherInfo;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2012-7-29 下午3:05:31
 */
public interface SecurityMetadataSourceDao {

	public List<MatcherInfo> queryRequestUrlList(MatcherInfo queryVo);

	public List<ConfigAttribute> queryConfigAttributeCollectionValue(MatcherInfo queryVo);

	public List<ConfigAttribute> queryConfigAttributeCollectionNull(DbRoleAttribute queryVo);

	public List<AclGrantedAuthority> queryUserAuthorities(AclUserVo findVo);

}
