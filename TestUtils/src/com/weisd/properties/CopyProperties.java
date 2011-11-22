package com.weisd.properties;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-11-15 下午1:44:39
 */
public class CopyProperties {

	/**
	 * @param args
	 * @throws ConfigurationException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws NoSuchMethodException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws ConfigurationException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
		// TODO Auto-generated method stub
		Properties props = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream("D:\\junbao_newpro\\TestUtils\\src\\sizes.properties"));
			props.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		props.setProperty("username", "22222");
//		props.s

//		Configuration config = ConfigurationConverter.getConfiguration(props);
//		PropertiesConfiguration configuration = new PropertiesConfiguration("global.properties");
//		
////		configuration.
//		Properties props2 =  new Properties();
//		//BeanUtils.copyProperties(props, props2);
//		
//		
//		props2 = (Properties)BeanUtils.cloneBean(props);
//		
//		System.out.println(props2);
//		
//		
////		config.
	}

}
