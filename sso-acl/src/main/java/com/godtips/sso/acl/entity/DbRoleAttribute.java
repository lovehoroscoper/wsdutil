package com.godtips.sso.acl.entity;

import org.springframework.security.access.ConfigAttribute;

/**
 * 和SecurityConfig 一样
 * 
 * @author
 * 
 */
public class DbRoleAttribute implements ConfigAttribute {

	private String idAcl;
	private String principalType;
	private String principalId;
	private String idRole;
	private String aclState;
	private String validType;
	private String roleStatus;
	private String roleKey;
	private String roleSort;
	
	public String getRoleSort() {
		return roleSort;
	}

	public void setRoleSort(String roleSort) {
		this.roleSort = roleSort;
	}

	public String getIdAcl() {
		return idAcl;
	}

	public void setIdAcl(String idAcl) {
		this.idAcl = idAcl;
	}

	public String getPrincipalType() {
		return principalType;
	}

	public void setPrincipalType(String principalType) {
		this.principalType = principalType;
	}

	public String getPrincipalId() {
		return principalId;
	}

	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}

	public String getIdRole() {
		return idRole;
	}

	public void setIdRole(String idRole) {
		this.idRole = idRole;
	}

	public String getAclState() {
		return aclState;
	}

	public void setAclState(String aclState) {
		this.aclState = aclState;
	}

	public String getValidType() {
		return validType;
	}

	public void setValidType(String validType) {
		this.validType = validType;
	}

	public String getRoleStatus() {
		return roleStatus;
	}

	public void setRoleStatus(String roleStatus) {
		this.roleStatus = roleStatus;
	}

	public String getRoleKey() {
		return roleKey;
	}

	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}

	public DbRoleAttribute() {
		this.roleKey = "";
	}

	public DbRoleAttribute(String config) {
		this.roleKey = config;
	}

	public boolean equals(Object obj) {
		if (obj instanceof ConfigAttribute) {
			ConfigAttribute attr = (ConfigAttribute) obj;
			return this.roleKey.equals(attr.getAttribute());
		}
		return false;
	}

	public String getAttribute() {
		return this.roleKey;
	}

	public int hashCode() {
		return this.roleKey.hashCode();
	}

	public String toString() {
		return this.roleKey;
	}

}
