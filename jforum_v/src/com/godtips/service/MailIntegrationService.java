package com.godtips.service;

import java.util.List;

import net.jforum.entities.MailIntegration;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-26 下午10:11:19
 * @version v1.0
 */
public interface MailIntegrationService {

	public void add(MailIntegration integration);
	
	public void update(MailIntegration integration);
	
	public void delete(int forumId);
	
	public MailIntegration find(int forumId);
	
	public List findAll();
	
}
