package org.gonetbar.ssa.init;

import org.apache.log4j.Logger;
import org.gonetbar.ssa.constant.SystemPropertiesUtils;

public class StartServer {
	private static Logger logger = Logger.getLogger(StartServer.class);

	private String configName;

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public void startListener() {
		try {
			logger.info("*********开始加载配置参数*********");
			SystemPropertiesUtils.initGlobals(null, configName);
			logger.info("*********加载配置参数完成*****");
		} catch (Exception e) {
			logger.error("加载配置参数异常", e);
			this.stopListener();
		}

		logger.info("*********启动服务-完成*****");
	}

	public void stopListener() {
		logger.info("*********服务停止成功*****");
		System.exit(1);
	}

}
