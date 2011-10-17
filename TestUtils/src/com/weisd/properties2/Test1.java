package com.weisd.properties2;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-18 下午8:07:51
 * @version v1.0
 */
public class Test1 {

	/**
	 * @param args
	 * @throws ConfigurationException 
	 */
	public static void main(String[] args) throws ConfigurationException {
		PropertiesConfiguration urlPatterns = new PropertiesConfiguration("urlPattern.properties");
		
//		urlPatterns.get.getProperty("");
		
		
		for (Iterator iter = urlPatterns.getKeys(); iter.hasNext(); ) {
			Map.Entry entry = (Map.Entry) iter.next();
			System.out.println((String)entry.getKey() + "    " + (String)entry.getValue());
		}
	}
	
//	public static void main(String[] args) {
//
//		try {
//			PropertiesConfiguration config = new PropertiesConfiguration("test.properties");
//
//			// System.out.println(config);
//			//
//			// String header = config.getHeader();
//			// System.out.println(header);
//			//
//
//			String haha = config.getString("hahah");
//			
//			Iterator iter = config.getKeys();
//			// Iterator iter = null;
//
//			File f = config.getFile();
//			
//			while (iter.hasNext()) {
//
//				String entry = (String) iter.next();
//
//				
//				System.out.println(entry);
//
//			}
//
//		} catch (ConfigurationException e) {
//			e.printStackTrace();
//		}
//	}

}
