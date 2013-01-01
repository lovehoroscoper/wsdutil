package org.gonetbar.ssa.oauth;

import java.util.Map;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2012-12-31 上午10:56:49
 */
public class InitOpenLoginConfig {
	
	private Map<String,String> typeMapping;

	public final Map<String, String> getTypeMapping() {
		return typeMapping;
	}

	public final void setTypeMapping(Map<String, String> typeMapping) {
		this.typeMapping = typeMapping;
	}
	
	public String getProviderTypeByKey(String key){
		return typeMapping.get(key);
	}
	
}
