package org.gonetbar.ssa.entity;

import org.gonetbar.ssa.constant.SysVersion;
import org.springframework.security.core.GrantedAuthority;

public final class AclGrantedAuthority implements GrantedAuthority {

	private static final long serialVersionUID = SysVersion.SERIAL_VERSION_UID;

	private String role;
	private String userid;
	private String roleid;
	private String rolestatus;

	public AclGrantedAuthority() {

	}

	public String getRole() {
		return role;
	}

	public String getUserid() {
		return userid;
	}

	public String getRoleid() {
		return roleid;
	}

	public String getRolestatus() {
		return rolestatus;
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
