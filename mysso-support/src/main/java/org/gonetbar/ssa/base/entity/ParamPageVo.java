package org.gonetbar.ssa.base.entity;

/**
 * 用于分页
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-7-2 下午11:23:32
 * @version v1.0
 */
public class ParamPageVo {
	// 当前页
	private int pageNo = 0;
	// 每页显示数目
	private int pageSize = 20;
	// 查询list
	private String sqlId;
	// 查询记录数
	private String countSqlId;
	// 查询vo
	private Object condition;

	public ParamPageVo() {
		
	}
	
	public ParamPageVo(int pageNo, int pageSize, String sqlId, String countSqlId, Object condition) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.sqlId = sqlId;
		this.countSqlId = countSqlId;
		this.condition = condition;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSqlId() {
		return sqlId;
	}

	public void setSqlId(String sqlId) {
		this.sqlId = sqlId;
	}

	public String getCountSqlId() {
		return countSqlId;
	}

	public void setCountSqlId(String countSqlId) {
		this.countSqlId = countSqlId;
	}

	public Object getCondition() {
		return condition;
	}

	public void setCondition(Object condition) {
		this.condition = condition;
	}

}
