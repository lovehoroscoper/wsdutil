package org.gonetbar.ssa.cas.filter.sms;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

/**
 * @desc 描述：资源源数据定义，即定义某一资源可以被哪些角色访问
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-12-12 上午11:38:15
 */
public class SsaFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource{

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();   
        ConfigAttribute ca = new SecurityConfig("ROLE_ADMIN");   
        atts.add(ca);   
       return atts;  
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		// TODO 修改
		return true;
	}



}
