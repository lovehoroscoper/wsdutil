package org.gonetbar.ssa.cas.filter.sms;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.gonetbar.ssa.entity.MatcherInfo;
import org.gonetbar.ssa.service.SecurityMetadataSourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

/**
 * @desc 描述：资源源数据定义，即定义某一资源可以被哪些角色访问
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-12-12 上午11:38:15
 */
public class AclFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	protected final Logger logger = LoggerFactory.getLogger(AclFilterInvocationSecurityMetadataSource.class);

	private SecurityMetadataSourceService securityMetadataSourceService;

	public Collection<ConfigAttribute> getAttributes(Object object) {
		final HttpServletRequest request = ((FilterInvocation) object).getRequest();
		return queryConfigAttributeCollection(request);
	}

	private Collection<ConfigAttribute> queryConfigAttributeCollection(final HttpServletRequest request) {
		String url = getRequestPath(request);
		if (StringUtils.isBlank(url)) {
			return null;
		}
		MatcherInfo queryVo = securityMetadataSourceService.queryConfigAttributeCollectionKey(url, request);
		// TODO MatcherInfo 有可能为空
		List<ConfigAttribute> list = (List<ConfigAttribute>) securityMetadataSourceService.queryConfigAttributeCollectionValue(queryVo.getLinkurl(), queryVo);
		if (list.isEmpty()) {
			list = (List<ConfigAttribute>) securityMetadataSourceService.queryConfigAttributeCollectionNull(null);
		}
		if (null == list || list.isEmpty()) {
			// TODO 专门的捕获异常信息
			throw new RuntimeException("无法获取访问地址权限列表,拒绝访问!!!");
		}
		return list;
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		// TODO 暂时 weisd
		return null;
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Resource(name = "securityMetadataSourceService")
	public void setSecurityMetadataSourceService(SecurityMetadataSourceService securityMetadataSourceService) {
		this.securityMetadataSourceService = securityMetadataSourceService;
	}

	private String getRequestPath(final HttpServletRequest request) {
		String url = request.getServletPath();
		if (request.getPathInfo() != null) {
			url += request.getPathInfo();
		}
		url = url.toLowerCase();
		return url;
	}
}
