package com.godtips.dao.impl.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.jforum.JForumExecutionContext;
import net.jforum.dao.SmilieDAO;
import net.jforum.entities.Smilie;
import net.jforum.exceptions.DatabaseException;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.SystemGlobals;

import com.godtips.base.dao.impl.BaseDaoImpl;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-9-8 下午9:02:52
 * @version v1.0
 */
public class SmilieDaoImpl extends BaseDaoImpl implements SmilieDAO {

	@Override
	public int addNew(Smilie smilie) {
		this.addObject("SmiliesModel.addNew", smilie);
		return smilie.getId();
	}

	@Override
	public void delete(int id) {
		this.deleteObject("SmiliesModel.delete", id);
	}

	@Override
	public void update(Smilie smilie) {
		this.updateObject("SmiliesModel.update", smilie);
	}

	@Override
	public List selectAll() {
		List list = this.queryList("SmiliesModel.selectAll", null);
		return list;
	}

	@Override
	public Smilie selectById(int id) {
		Smilie s = new Smilie();
		List list = this.queryList("SmiliesModel.selectById", id);
		if(null != list && list.size() > 0){
			s = (Smilie)list.get(0);
		}
		return s;
	}

}
