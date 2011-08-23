package com.godtips.service;

import java.util.List;

import net.jforum.entities.Category;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-23 下午8:14:36
 * @version v1.0
 */
public interface CategoryService {

	public Category selectById(int categoryId);
	
	public List selectAll();
	
	public boolean canDelete(int categoryId);
	
	public void delete(int categoryId);
	
	public void update(Category category);
	
	public int addNew(Category category);

	public void setOrderUp(Category category, Category otherCategory);

	public void setOrderDown(Category category, Category otherCategory);
}
