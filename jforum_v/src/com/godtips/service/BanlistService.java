package com.godtips.service;

import java.util.List;

import net.jforum.entities.Banlist;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-8-13 下午7:06:22
 */
public interface BanlistService {

	public void insert(Banlist b);

	public void delete(int banlistId);

	public List selectAll();
}
