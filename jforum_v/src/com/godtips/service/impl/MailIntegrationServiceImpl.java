package com.godtips.service.impl;

import java.util.List;

import net.jforum.dao.MailIntegrationDAO;
import net.jforum.entities.MailIntegration;

import com.godtips.service.MailIntegrationService;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-26 下午10:11:46
 * @version v1.0
 */
public class MailIntegrationServiceImpl implements MailIntegrationService {
	
	private MailIntegrationDAO mailIntegrationDao;

	@Override
	public void add(MailIntegration integration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(MailIntegration integration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int forumId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MailIntegration find(int forumId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public MailIntegrationDAO getMailIntegrationDao() {
		return mailIntegrationDao;
	}

	public void setMailIntegrationDao(MailIntegrationDAO mailIntegrationDao) {
		this.mailIntegrationDao = mailIntegrationDao;
	}
	
}
