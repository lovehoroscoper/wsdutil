package com.test.mina.server.filter;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.firewall.Subnet;

/**
 * 应为IP限制有需要write信息,只能write一次,如果多次blockSession 那么不能再次write应为已经关闭
 * 
 * sessionCreated--sessionOpened -- ....--messageSent
 * 
 * 
 * return 0008
 * 
 * @author Administrator
 * 
 */
public class AccessIpFilter extends IoFilterAdapter {

	private final List<Subnet> allowlist = new CopyOnWriteArrayList<Subnet>();

	private final static Logger logger = Logger.getLogger(AccessIpFilter.class);

	/**
	 * 第一层拦截
	 */
	@Override
	public void sessionCreated(NextFilter nextFilter, IoSession session) {
		if (isAllowed(session)) {
			nextFilter.sessionCreated(session);
		} else {
			blockSession(session);
		}
	}

	/**
	 * 第二层拦截
	 */
	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message) {
		if (isAllowed(session)) {
			nextFilter.messageReceived(session, message);
		} else {
			blockSession(session);
		}
	}

	private void blockSession(IoSession session) {
		boolean closeFlag = session.isClosing();
		if (!closeFlag) {
			//session.write("0008");
		}
		// 必须用true 用false多次关闭报异常
		session.close(true);
	}

	private boolean isAllowed(IoSession session) {
		if (0 == allowlist.size()) {
			return true;
		}
		SocketAddress remoteAddress = session.getRemoteAddress();
		if (remoteAddress instanceof InetSocketAddress) {
			InetAddress address = ((InetSocketAddress) remoteAddress).getAddress();
			for (Subnet subnet : allowlist) {
				if (subnet.inSubnet(address)) {
					// 能访问的ip地址
					return true;
				}
			}
		}
		logger.error("该IP地址限制通过:" + remoteAddress);
		return false;
	}

	public void setAllowlist(InetAddress[] addresses) {
		if (addresses == null) {
			throw new IllegalArgumentException("设置允许访问IP地址为空");
		}
		allowlist.clear();
		for (int i = 0; i < addresses.length; i++) {
			InetAddress addr = addresses[i];
			block(addr);
		}
	}

	public void setSubnetAllowlist(Subnet[] subnets) {
		if (subnets == null) {
			throw new IllegalArgumentException("设置允许访问IP地址为空");
		}
		allowlist.clear();
		for (Subnet subnet : subnets) {
			allow(subnet);
		}
	}

	public void setAllowlist(Iterable<InetAddress> addresses) {
		if (addresses == null) {
			throw new IllegalArgumentException("设置允许访问IP地址为空");
		}
		allowlist.clear();
		for (InetAddress address : addresses) {
			block(address);
		}
	}

	public void setSubnetAllowlist(Iterable<Subnet> subnets) {
		if (subnets == null) {
			throw new IllegalArgumentException("设置允许访问IP地址为空");
		}
		allowlist.clear();
		for (Subnet subnet : subnets) {
			allow(subnet);
		}
	}

	public void block(InetAddress address) {
		if (address == null) {
			throw new IllegalArgumentException("设置允许访问IP地址为空");
		}
		allow(new Subnet(address, 32));
	}

	public void allow(Subnet subnet) {
		if (subnet == null) {
			throw new IllegalArgumentException("设置允许访问IP地址为空");
		}
		allowlist.add(subnet);
	}
}
