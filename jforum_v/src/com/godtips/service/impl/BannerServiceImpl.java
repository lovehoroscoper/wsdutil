package com.godtips.service.impl;

import java.util.List;

import net.jforum.dao.BannerDAO;
import net.jforum.entities.Banner;

import com.godtips.service.BannerService;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-14 上午1:14:39
 * @version v1.0
 */
public class BannerServiceImpl implements BannerService{
	
	private BannerDAO bannerDao;

	@Override
	public Banner selectById(int bannerId) {
		return bannerDao.selectById(bannerId);
	}

	@Override
	public List selectAll() {
		return bannerDao.selectAll();
	}

	@Override
	public int addNew(Banner banner) {
		return bannerDao.addNew(banner);
	}

	@Override
	public void update(Banner banner) {
		bannerDao.update(banner);		
	}

	@Override
	public boolean findDelete(int bannerId) {
		return bannerDao.canDelete(bannerId);
	}

	@Override
	public void delete(int bannerId) {
		bannerDao.delete(bannerId);		
	}

	@Override
	public List selectActiveBannerByPlacement(int placement) {
		return bannerDao.selectActiveBannerByPlacement(placement);
	}

	public void setBannerDao(BannerDAO bannerDao) {
		this.bannerDao = bannerDao;
	}
	
}
