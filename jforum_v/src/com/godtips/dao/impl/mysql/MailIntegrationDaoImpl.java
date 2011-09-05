package com.godtips.dao.impl.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.jforum.JForumExecutionContext;
import net.jforum.dao.MailIntegrationDAO;
import net.jforum.entities.MailIntegration;
import net.jforum.exceptions.DatabaseException;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.SystemGlobals;

import com.godtips.base.dao.impl.BaseDaoImpl;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-26 下午10:10:44
 * @version v1.0
 */
public class MailIntegrationDaoImpl extends BaseDaoImpl implements MailIntegrationDAO {

	@Override
	public void add(MailIntegration integration) {
		this.addObject("MailIntegration.add", integration);
	}

	@Override
	public void update(MailIntegration integration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(int forumId) {
		this.deleteObject("MailIntegration.delete", forumId);
	}

	@Override
	public MailIntegration find(int forumId) {
		MailIntegration m = null;
		List list = this.queryList("MailIntegration.find", forumId);
		if (null != list && list.size() > 0) {
			m = (MailIntegration)list.get(0);
		}
		return m;
	}

	@Override
	public List findAll() {
		return this.queryList("MailIntegration.findAll", null);
	}

}
