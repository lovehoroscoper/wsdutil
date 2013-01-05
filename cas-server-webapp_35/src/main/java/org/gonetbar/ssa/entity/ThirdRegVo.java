package org.gonetbar.ssa.entity;

import org.scribe.up.profile.UserProfile;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:xiyangdewuse@gmail.com
 * @version 创建时间：2012-12-31 下午7:04:20
 */
public class ThirdRegVo {

	private String keyStr;

	private String md5Valid;	

	private String accessToken;

	private String providerId;

	private String providerType;
	
	private UserProfile userProfile;

	public ThirdRegVo(String providerId, String providerType, String accessToken, UserProfile userProfile,String keyStr) {
		this.accessToken = accessToken;
		this.providerId = providerId;
		this.providerType = providerType;
		this.userProfile = userProfile;
		this.keyStr = keyStr;
	}

	public final String getKeyStr() {
		return keyStr;
	}

	public final void setKeyStr(String keyStr) {
		this.keyStr = keyStr;
	}

	public final String getMd5Valid() {
		return md5Valid;
	}

	public final void setMd5Valid(String md5Valid) {
		this.md5Valid = md5Valid;
	}

	public ThirdRegVo() {
	}

	public final String getAccessToken() {
		return accessToken;
	}

	public final void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
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
