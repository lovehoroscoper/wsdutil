package com.weisd.spring.utils;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-11-15 下午12:29:08
 */
public class PropertyEncodeConfigurer extends PropertyPlaceholderConfigurer{
	
	private String encodeName;

	public void setEncodeName(String encodeName) {
		System.out.println("-------------:" + encodeName);
		
		this.encodeName = encodeName;
	}
	
	

}
