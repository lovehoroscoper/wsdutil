package com.test.mina.server.huaruida;

import org.apache.log4j.Logger;

import com.junbao.hf.utils.common.StringUtils;

public class DesCbcBiz {
	private static Logger logger = Logger.getLogger(DesCbcBiz.class);

	private static DesCbcBiz desCbcBiz;

	private byte[] key;

	public static DesCbcBiz getInstance() {
		if (desCbcBiz == null) {
			load();
		}
		return desCbcBiz;
	}

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}

	public static String getMAC(byte[] macKey, String macBlock) {
		if (null == macKey || StringUtils.isEmptyOrNullByTrim(macBlock)) {
			return "";
		}
		try {
			// 传入运算报文和工作密钥，调用getMAC方法可以获得mac校验码]
			byte[] mac = DES_CBC.getMAC(macKey, macBlock.getBytes("UTF-8"));
			return DES_CBC.toHex(mac);
		} catch (Exception e) {
			logger.error("生成MAC校验码异常", e);
			return "";
		}
	}

	public static void load() {
		logger.info("开始加载DesCbcBiz配置信息");
		try {
			if (null == desCbcBiz) {
				desCbcBiz = new DesCbcBiz();
			}
			logger.info("开始加载DesCbcBiz配置信息完成");
		} catch (Exception e) {
			logger.error("加载DesCbcBiz配置文件", e);
		}
	}

}
