package org.gonetbar.ssa.entity;

import org.gonetbar.ssa.constant.SysVersion;
import org.springframework.security.core.GrantedAuthority;

public final class AclGrantedAuthority implements GrantedAuthority {

	private static final long serialVersionUID = SysVersion.SERIAL_VERSION_UID;

	private final String role;

	private String userid;
	private String roleid;
	private String rolestatus;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getRolestatus() {
		return rolestatus;
	}

	public void setRolestatus(String rolestatus) {
		this.rolestatus = rolestatus;
	}

	public AclGrantedAuthority(String role) {
		this.role = role;
	}

	public String getAuthority() {
		return role;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof AclGrantedAuthority) {
			return role.equals(((AclGrantedAuthority) obj).role);
		}

		return false;
	}

	public int hashCode() {
		return this.role.hashCode();
	}

	public String toString() {
		return this.role;
	}
}
