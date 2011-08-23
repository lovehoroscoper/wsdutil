package com.godtips.dao.impl.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.godtips.base.dao.impl.BaseDaoImpl;
import com.godtips.util.StringUtils;

import net.jforum.JForumExecutionContext;
import net.jforum.dao.CategoryDAO;
import net.jforum.entities.Category;
import net.jforum.exceptions.DatabaseException;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.SystemGlobals;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-23 下午8:14:17
 * @version v1.0
 */
public class CategoryDaoImpl extends BaseDaoImpl implements CategoryDAO {

	@Override
	public Category selectById(int categoryId) {
		List list = this.queryList("CategoryModel.selectById", categoryId);
		Category c = new Category();
		if (null != list && list.size() > 0) {
			c = (Category)list.get(0);
		}
		return c;
	}

	@Override
	public List selectAll() {
		return this.queryList("CategoryModel.selectAll", null);
	}

	@Override
	public boolean canDelete(int categoryId) {
		int count = (Integer)this.findObject("CategoryModel.canDelete", categoryId);
		return count < 1;
	}

	@Override
	public void delete(int categoryId) {
		this.deleteObject("CategoryModel.delete", categoryId);
	}

	@Override
	public void update(Category category) {
		this.updateObject("CategoryModel.update", category);
	}

	@Override
	public int addNew(Category category) {
		int order = 1;
		String maxOrder = (String)this.findObject("CategoryModel.getMaxOrder", null);
		if(!StringUtils.isEmptyOrNullByTrim(maxOrder)){
			order = Integer.valueOf(maxOrder) + 1;
		}
		category.setOrder(order);
		this.addObjectArray("CategoryModel.addNew", category);
		int id = category.getId();
		//category.setId(id);
		//category.setOrder(order);
		return id;
	}

	@Override
	public void setOrderUp(Category category, Category otherCategory) {
		this.setOrder(category, otherCategory);
	}

	@Override
	public void setOrderDown(Category category, Category otherCategory) {
		this.setOrder(category, otherCategory);
	}
	
	private void setOrder(Category category, Category otherCategory){
		int tmpOrder = otherCategory.getOrder();
		otherCategory.setOrder(category.getOrder());
		category.setOrder(tmpOrder);
		this.updateObject("CategoryModel.setOrderById", otherCategory);
		this.updateObject("CategoryModel.setOrderById", category);
	}

}
