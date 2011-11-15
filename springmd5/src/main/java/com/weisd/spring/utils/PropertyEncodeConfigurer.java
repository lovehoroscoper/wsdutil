package com.weisd.spring.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-11-15 下午12:29:08
 */
public class PropertyEncodeConfigurer extends PropertyPlaceholderConfigurer {

	private static Logger logger = Logger.getLogger(PropertyEncodeConfigurer.class);
	/**
	 * 那些需要编码的字段名称
	 */
	private String encodeName;

	private String splitTag;

	private final String keyStr = "key11223342232";

	public void setSplitTag(String splitTag) {
		this.splitTag = splitTag;
	}

	public void setEncodeName(String encodeName) {
		this.encodeName = encodeName;
	}

	private String[] getEncodeNameArry(String encodeName, String split) {
		String[] arr = null;
		if (null != encodeName && !"".equals(encodeName.trim())) {
			arr = encodeName.split("[,]");
		}
		return arr;
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		String[] encodeArry = getEncodeNameArry(encodeName, splitTag);
		if (null != props && null != encodeArry && encodeArry.length > 0) {
			for (int i = 0; i < encodeArry.length; i++) {
				String proKey = encodeArry[i];
				if (props.containsKey(proKey)) {
					String encode_value = props.getProperty(proKey);
					try {
						String new_value;
						new_value = SpringPropertiesDbDesUtil.getDecodeByEncode(encode_value, keyStr);
						props.setProperty(proKey, new_value);
					} catch (IOException e) {
						logger.error("属性文件中解密相关信息异常", e);
					}
				} else {
					logger.warn("属性文件中没有包含相应的键[" + proKey + "]");
				}
			}
		}
		super.processProperties(beanFactoryToProcess, props);
	}

}
