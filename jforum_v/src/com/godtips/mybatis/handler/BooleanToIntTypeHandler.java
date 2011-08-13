package com.godtips.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * 
 * @Description: boolean 值 与 int 值转换
 * 
 * java == > db   :java 中 boolean 值 转换为 db 中的 int 
 * db   == > java :db 中的 int 转换为 java中 boolean
 * 
 * boolean  true  ： 1 
 * boolean  false ： 0
 * 
 * 对于 null 值是否有影响
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-13 下午11:46:36
 * @version v1.0
 */
public class BooleanToIntTypeHandler extends BaseTypeHandler  implements TypeHandler {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
		 ps.setInt(i, (Boolean) parameter ? 1 : 0);
	}

	@Override
	public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
		 return rs.getInt(columnName) == 1 ;
	}

	@Override
	public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return  cs.getInt(columnIndex) == 1 ;
	}

}
