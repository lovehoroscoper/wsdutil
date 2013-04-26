package com.wode.base.util;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2012-7-5 下午7:00:18
 */
public class TermJsonFmt {

	public static String getTreeJsonStr(Object obj, final boolean isParentShow) {
		PropertyFilter proFilter = new PropertyFilter() {
			public boolean apply(Object source, String name, Object value) {
				if ("termId".equals(name) || "termName".equals(name) || "parentId".equals(name) || "termList".equals(name) || "slug".equals(name) || ("isParent".equals(name) && isParentShow)) {
					return true;
				}
				return false;
			}
		};
		SerializeWriter out = new SerializeWriter();
		JSONSerializer serializer = new JSONSerializer(out);
		serializer.getPropertyFilters().add(proFilter);
		serializer.write(obj);
		return out.toString();
	}

}
