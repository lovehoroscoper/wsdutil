package com.godtips.dao.impl.mysql;

import net.jforum.dao.ApiDAO;

import com.godtips.base.dao.impl.BaseDaoImpl;
import com.godtips.util.StringUtils;

/**
 * 
 * @author Administrator
 *
 */
public class ApiDaoImpl extends BaseDaoImpl implements ApiDAO {
	/**
	 * @see net.jforum.dao.ApiDAO#isValid(java.lang.String)
	 */
	public boolean isValid(String apiKey) {
		String resStr = (String)this.findObject("ApiModel.isValid", apiKey);
		return !StringUtils.isEmptyOrNullByTrim(resStr);
	}
}