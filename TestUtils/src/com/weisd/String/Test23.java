package com.weisd.String;

import com.junbao.hf.utils.common.StringUtils;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-10-19 上午10:10:26
 */
public class Test23 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Test23 t = new Test23();
		
		String oldStr = " 12354 , , 77";
		String newStr = t.getStrIn(oldStr);

		System.out.println(newStr);
	}


	public String getStrIn(String oldStr) {
		String ids = "";
		if (StringUtils.isEmptyOrNullByTrim(oldStr)) {
			ids = "";
		} else {
			String[] arryT = oldStr.split("[,]");
			for (String s : arryT) {
				if(!StringUtils.isEmptyOrNullByTrim(s)){
					ids += StringUtils.getStringFromEmpty(s);
					ids += ",";
				}
			}
			if (ids != null && !"".equals(ids)) {
				ids = ids.substring(0, ids.length() - 1);
			}
		}
		return ids;
	}
}
