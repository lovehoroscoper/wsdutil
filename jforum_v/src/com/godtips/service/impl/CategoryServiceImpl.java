package com.godtips.service.impl;

import java.util.List;

import net.jforum.dao.CategoryDAO;
import net.jforum.entities.Category;

import com.godtips.service.CategoryService;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-23 下午8:15:38
 * @version v1.0
 */
public class CategoryServiceImpl implements CategoryService {
	
	private CategoryDAO categoryDao;
	
	public CategoryDAO getCategoryDao() {
		return categoryDao;
	}

	public void setCategoryDao(CategoryDAO categoryDao) {
		this.categoryDao = categoryDao;
	}

	@Override
	public Category selectById(int categoryId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canDelete(int categoryId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void delete(int categoryId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Category category) {
		// TODO Auto-generated method stub

	}

	@Override
	public int addNew(Category category) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setOrderUp(Category category, Category otherCategory) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOrderDown(Category category, Category otherCategory) {
		// TODO Auto-generated method stub

	}

}
