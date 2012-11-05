package org.gonetbar.ssa.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.gonetbar.ssa.entity.DbRoleAttribute;
import org.gonetbar.ssa.entity.MatcherInfo;
import org.springframework.security.access.ConfigAttribute;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2012-7-29 下午12:05:21
 */
public interface SecurityMetadataSourceService {

	public List<MatcherInfo> queryRequestUrlList(String subsysid);

	public MatcherInfo queryConfigAttributeCollectionKey(String url, final HttpServletRequest request);

	public List<ConfigAttribute> queryConfigAttributeCollectionValue(String key, MatcherInfo queryVo);

	/**
	 * queryConfigAttributeCollectionKey 当查询返回null时候必须限制
	 * 
	 * @param url
	 * @param request
	 * @return
	 */
	public List<ConfigAttribute> queryConfigAttributeCollectionNull(DbRoleAttribute queryVo);
	
	/**
	 * 查询URL所属子系统
	 * @param pattern
	 * @return
	 */
	public MatcherInfo querySubSysId(MatcherInfo queryVo);

}
