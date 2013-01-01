package org.gonetbar.ssa.entity;

import org.scribe.up.profile.UserProfile;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2012-12-31 下午7:04:20
 */
public class ThirdRegVo {

	private String code;

	private String providerId;

	private String providerType;

	private UserProfile userProfile;

	public ThirdRegVo(String providerId, String providerType, String code, UserProfile userProfile) {
		this.code = code;
		this.providerId = providerId;
		this.providerType = providerType;
		this.userProfile = userProfile;
	}

	public ThirdRegVo() {
	}

	public final String getCode() {
		return code;
	}

	public final void setCode(String code) {
		this.code = code;
	}

	public final UserProfile getUserProfile() {
		return userProfile;
	}

	public final void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

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

}
