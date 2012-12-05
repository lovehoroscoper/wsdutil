package org.gonetbar.ssa.constant;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class SystemPropertiesUtils {
	// private static final Logger logger =
	// Logger.getLogger(SystemPropertiesUtils.class);
	// private static SystemPropertiesUtils sysproperties = new
	// SystemPropertiesUtils();
	public static PropertiesConfiguration globals = new PropertiesConfiguration();

	// public static PropertiesConfiguration urlPatterns = new
	// PropertiesConfiguration();

	private SystemPropertiesUtils() {
	}

	public static void initGlobals(String appPath, String mainConfigurationFile) throws ConfigurationException {
		globals = new PropertiesConfiguration(mainConfigurationFile);
	}

}