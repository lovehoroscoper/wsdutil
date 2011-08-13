package com.godtips.service;

import java.util.List;

import net.jforum.entities.Banner;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-14 上午1:14:25
 * @version v1.0
 */
public interface BannerService {

	public Banner selectById(int bannerId);

	public List selectAll();

	public int addNew(Banner banner);

	public void update(Banner banner);

	/**
	 * canDelete
	 * @param bannerId
	 * @return
	 */
	public boolean findDelete(int bannerId);

	public void delete(int bannerId);

	public List selectActiveBannerByPlacement(int placement);
}
