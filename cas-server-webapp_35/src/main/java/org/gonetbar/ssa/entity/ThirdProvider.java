package org.gonetbar.ssa.entity;

import java.io.Serializable;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2013-1-1 下午3:21:21
 */
public class ThirdProvider implements Serializable {

	private String providerId;
	private String providerType;
	private String status;

	public final String getProviderId() {
		return providerId;
	}

	public final void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public final String getProviderType() {
		return providerType;
	}

	public final void setProviderType(String providerType) {
		this.providerType = providerType;
	}

	public final String getStatus() {
		return status;
	}

	public final void setStatus(String status) {
		this.status = status;
	}

}
