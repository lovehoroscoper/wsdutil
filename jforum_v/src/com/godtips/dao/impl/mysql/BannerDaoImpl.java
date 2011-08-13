package com.godtips.dao.impl.mysql;

import java.util.List;

import net.jforum.dao.BannerDAO;
import net.jforum.entities.Banner;

import com.godtips.base.dao.impl.BaseDaoImpl;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-13 下午10:39:18
 * @version v1.0
 */
public class BannerDaoImpl extends BaseDaoImpl implements BannerDAO {

	public Banner selectById(int bannerId) {
		return (Banner) this.findObject("BannerModel.selectById", bannerId);
	}

	public List selectAll() {
		return this.queryList("BannerModel.selectAll", null);
	}

	public boolean canDelete(int bannerId) {
		int total = (Integer) this.findObject("BannerModel.canDelete", bannerId);
		return total < 1 ? false : true;
	}

	public void delete(int bannerId) {
		this.deleteObject("BannerModel.delete", bannerId);
	}

	public void update(Banner banner) {
		this.updateObject("BannerModel.update", banner);
	}

	public int addNew(Banner banner) {		
		banner.setId(22);
		
		this.addObject("BannerModel.addNew", banner);
		return banner.getId();
	}

	public List selectActiveBannerByPlacement(int placement) {
		return this.queryList("BannerModel.selectActiveBannerByPlacement", placement);
	}
}
