package com.godtips.dao.impl.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.jforum.JForumExecutionContext;
import net.jforum.dao.RankingDAO;
import net.jforum.entities.Ranking;
import net.jforum.exceptions.DatabaseException;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.SystemGlobals;

import com.godtips.base.dao.impl.BaseDaoImpl;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-9-8 下午8:25:31
 * @version v1.0
 */
public class RankingDaoImpl extends BaseDaoImpl implements RankingDAO {

	@Override
	public Ranking selectById(int rankingId) {
		Ranking ranking = new Ranking();
		List list = this.queryList("RankingModel.selectById", rankingId);
		if (null != list && list.size() > 0) {
			ranking = (Ranking) list.get(0);
		}
		return ranking;
	}

	@Override
	public List selectAll() {
		List list = this.queryList("RankingModel.selectAll", null);
		return list;
	}

	@Override
	public void delete(int rankingId) {
		this.deleteObject("RankingModel.delete", rankingId);
	}

	@Override
	public void update(Ranking ranking) {
		this.updateObject("RankingModel.update", ranking);
	}

	@Override
	public void addNew(Ranking ranking) {
		this.addObject("RankingModel.addNew", ranking);
	}

	@Override
	public List selectSpecials() {
		List list = this.queryList("RankingModel.selectSpecials", null);
		return list;
	}

}
