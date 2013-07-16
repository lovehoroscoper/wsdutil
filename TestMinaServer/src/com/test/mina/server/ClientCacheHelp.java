package com.test.mina.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.junbao.hf.utils.common.StringUtils;

public class ClientCacheHelp {

	private static ClientCacheHelp clientCacheHelp;

	public static Map<String, IoSession> clientCacheMap = new HashMap<String, IoSession>();

	public static Map<String, IoSession> getClientCacheMap() {
		return clientCacheMap;
	}

	public void setClientCacheMap(Map<String, IoSession> map) {
		clientCacheMap = map;
	}

	public IoSession getClentSession(String comm, String hfserialid, String num) {
		String key = "KEY_";
		IoSession se = clientCacheMap.get(key + num);
		if (null == se) {
			if ("1".equals(num)) {
				return clientCacheMap.get(key + "2");
			} else if ("2".equals(num)) {
				return clientCacheMap.get(key + "1");
			} else {
				return null;
			}
		} else {
			return se;
		}
	}

	public void putClentSession(String comm, String hfserialid, IoSession session) {
		String ips = StringUtils.getStringFromEmpty(session.getRemoteAddress().toString());
		String num = (int) (Math.random() * 2 + 1) + "";
		String pandKey = "KEY_" + num;
		IoSession ext_se = clientCacheMap.get(pandKey);
		if (null != ext_se) {
			String ext_ips = ext_se.getRemoteAddress().toString();
			if (!ips.equals(ext_ips)) {
				if ("1".equals(num)) {
					clientCacheMap.put("KEY_2", session);
				} else if ("2".equals(num)) {
					clientCacheMap.put("KEY_1", session);
				}
			}
		} else {
			clientCacheMap.put(pandKey, session);
		}
	}

	public static ClientCacheHelp getInstance() {
		if (clientCacheHelp == null) {
			clientCacheHelp = new ClientCacheHelp();
		}
		return clientCacheHelp;
	}

	public static void main(String[] args) {
		
//		IoSession s = new IoSession();
		
	}
	
}
