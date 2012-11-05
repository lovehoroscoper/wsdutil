package org.gonetbar.ssa.dao;

import java.util.List;

import org.gonetbar.ssa.entity.DbRoleAttribute;
import org.gonetbar.ssa.entity.MatcherInfo;
import org.springframework.security.access.ConfigAttribute;

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

}
