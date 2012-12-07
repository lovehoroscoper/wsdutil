package com.godtips.sso.acl.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.userdetails.UserDetails;

import com.godtips.sso.acl.entity.AclGrantedAuthority;
import com.godtips.sso.acl.entity.AclUserVo;
import com.godtips.sso.acl.entity.DbRoleAttribute;
import com.godtips.sso.acl.entity.MatcherInfo;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2012-7-29 下午12:05:21
 */
public interface SecurityMetadataSourceService {

	public List<MatcherInfo> queryRequestUrlList(String subsysid);

	public MatcherInfo queryConfigAttributeCollectionKey(String systemid, String url, final HttpServletRequest request);

	/**
	 * 根据菜单ID获取菜单拥有的所有权限
	 * 
	 * @param key
	 * @param queryVo
	 * @return
	 */
	public List<ConfigAttribute> queryConfigAttributeCollectionValue(String systemid, String key, MatcherInfo queryVo);

	/**
	 * queryConfigAttributeCollectionKey 当查询返回null时候必须限制
	 * 
	 * @param url
	 * @param request
	 * @return
	 */
	public List<ConfigAttribute> queryConfigAttributeCollectionNull(String systemid, DbRoleAttribute queryVo);

	/**
	 * 查询URL所属子系统
	 * 
	 * @param pattern
	 * @return
	 */
	public MatcherInfo querySubSysId(MatcherInfo queryVo);

	public List<AclGrantedAuthority> queryUserAuthorities(long id_user, String username_unique);

	public UserDetails queryUserDetailsByAclUser(String username_unique, AclUserVo aclUser);

}
