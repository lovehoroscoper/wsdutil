package com.godtips.service.impl;

import java.util.List;

import net.jforum.dao.BanlistDAO;
import net.jforum.entities.Banlist;

import com.godtips.service.BanlistService;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-8-13 下午7:06:43
 */
public class BanlistServiceImpl implements BanlistService {

	private BanlistDAO banlistDao;
	
	@Override
	public void insert(Banlist b) {
		banlistDao.insert(b);
	}

	@Override
	public void delete(int banlistId) {
		banlistDao.delete(banlistId);
	}

	@Override
	public List selectAll() {
		return banlistDao.selectAll();
	}

	public void setBanlistDao(BanlistDAO banlistDao) {
		this.banlistDao = banlistDao;
	}
	
}
