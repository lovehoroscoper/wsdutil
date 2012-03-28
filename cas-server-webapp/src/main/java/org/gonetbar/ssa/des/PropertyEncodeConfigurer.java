package org.gonetbar.ssa.des;

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

	public static final String keyStr = "KEYSTR";

	public void setSplitTag(String splitTag) {
		this.splitTag = splitTag;
	}

	public void setEncodeName(String encodeName) {
		this.encodeName = encodeName;
	}

	private String[] getEncodeNameArry(String encodeName, String split) {
		String[] arr = null;
		if (null != encodeName && !"".equals(encodeName.trim())) {
			arr = encodeName.split("[" + split + "]");
		}
		return arr;
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		String[] encodeNameArray = getEncodeNameArry(encodeName, splitTag);
		if (null != props && null != encodeNameArray && encodeNameArray.length > 0) {
			String keyStrValue = props.getProperty(keyStr);;
			for (int i = 0; i < encodeNameArray.length; i++) {
				String proKey = encodeNameArray[i];
				if (null == proKey || "".equals(proKey.trim())) {
					continue;
				}
				if (props.containsKey(proKey)) {
					String encode_value = props.getProperty(proKey);
					try {
						String new_value = SpringPropertiesDbDesUtil.getDecodeByEncode(encode_value, keyStrValue);
						//System.out.println("解密信息[ " + proKey + "|" + new_value + "]");
						props.setProperty(proKey, new_value);
					} catch (IOException e) {
						logger.error("属性文件中解密相关信息异常", e);
					}
				} else {
					logger.warn("属性文件中没有包含相应的键[" + proKey + "]");
				}
			}
			//设置错误的编码
			//props.setProperty(keyStr, "123456");
		}
		super.processProperties(beanFactoryToProcess, props);
	}

	public String getEncodeName() {
		return encodeName;
	}

	public String getSplitTag() {
		return splitTag;
	}

}
