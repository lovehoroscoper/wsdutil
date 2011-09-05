package com.weisd.properties;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-8-17 上午9:49:33
 */
public class LoadAndSetPro {

	private static Logger logger = Logger.getLogger(LoadAndSetPro.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			PropertiesConfiguration globalPro = new PropertiesConfiguration("global.properties");
			
//			globalPro.get
			
			String pie = globalPro.getString("colors.pie");
			
			globalPro.getKeys();
			
			
			List list = globalPro.getList("colors.pie");
			
			logger.info(list);
			
			
			logger.info(pie);
			
//			colors.pie = #FF0000, #00FF00, #0000FF
//			globalPro.setProperty("colors.pie", "aaaaa");
			
			globalPro.addProperty("colors.pie", "hhhhhhhhhh");
			
			globalPro.save();
			
			List list2 = globalPro.getList("colors.pie");
			
			logger.info(list2);
			
			logger.info(list2.contains("hhhhhh"));
			
			
			globalPro.addProperty("colors.zzzz", "0000FFCCCC");//globalPro
			
			//colors.zzzz = #0000FFCCCC
			globalPro.save("colors.properties");
			
			
		} catch (ConfigurationException e1) {
			logger.error(e1);
		}
	}

}
