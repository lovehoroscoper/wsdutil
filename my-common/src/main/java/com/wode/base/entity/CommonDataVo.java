package com.wode.base.entity;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2012-5-23 上午9:59:46
 */
public class CommonDataVo {

	private String id;// ID
	private String name;// 名称
	private String typeCode;// 类型编码

	public CommonDataVo() {

	}

	public CommonDataVo(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public CommonDataVo(String id, String name, String typeCode) {
		this.id = id;
		this.name = name;
		this.typeCode = typeCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

}
