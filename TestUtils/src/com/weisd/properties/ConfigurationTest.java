package com.weisd.properties;

import java.io.File;
import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang.ArrayUtils;

public class ConfigurationTest {
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws ConfigurationException {
		// 操作 properties文件,直接读取src下的文件
//		Configuration configuration = new PropertiesConfiguration("global.properties");
//		PropertiesConfiguration configuration = new PropertiesConfiguration("global.properties");
//		
//		
//		
//		System.out.println(configuration.getString("CORE_PLATFORM_IPS"));
//		
//		configuration.setProperty("CORE_PLATFORM_IPS", "haha33333333333ha");
//		
//		
//		configuration.save();
//		
//		System.out.println(configuration.getString("CORE_PLATFORM_IPS"));
		
//		System.out.println(configuration.getString("name"));
		// 逗号分割
//		String[] arrays = configuration.getStringArray("member");
//		System.out.println(ArrayUtils.toString(arrays));
//		// 保存
//		PropertiesConfiguration c2 = new PropertiesConfiguration();
//		c2.setProperty("member", "phl,hxdg,bj,sanya");
//	//	c2.save();
//		// 保存到指定文件中--本例直接存在项目目录下
//		c2.save(new File("configbak.properties"));
//		// 当在工程目录下和src目录下，有同名配置文件时,读取工程下的;若工程下无文件，则再去查找src下是否有该文件
//		Configuration cc = new PropertiesConfiguration("configbak.properties");
//		System.out.println(cc.getString("member") + "名字");
//
//		// 操作XML文件
//		XMLConfiguration config = new XMLConfiguration("my.xml");
//		// 获取节点值 路径中不包括根节点名
//		String s1 = config.getString("disks.u-disk");
//		System.out.println(s1);
//		// 获取节点属性值
//		String s2 = config.getString("raid[@name]");
//		System.out.println(s2);
//		// 获得动态属性
//		String s3 = config.getString("disks.soft-disk");
//		System.out.println(s3);
//		// 获取列表
//		List rs = config.getList("raid.r");
//		System.out.println(rs);
//		config.save(new File("c://my.xml"));
		
//		Configuration configuration = new PropertiesConfiguration("global.properties");
		
		CompositeConfiguration config = new CompositeConfiguration(); 
		config.addConfiguration(new PropertiesConfiguration("oschina.properties")); 

		String usernaem = config.getString("username"); 
		String password = config.getString("password"); 


	}
}