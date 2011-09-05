package com.godtips.service.impl;

import net.jforum.dao.ModerationDAO;

import com.godtips.service.ModerationService;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-26 下午11:22:39
 * @version v1.0
 */
public class ModerationServiceImpl implements ModerationService {
	
	private ModerationDAO moderationDao;

	public ModerationDAO getModerationDao() {
		return moderationDao;
	}

	public void setModerationDao(ModerationDAO moderationDao) {
		this.moderationDao = moderationDao;
	}
	
}
