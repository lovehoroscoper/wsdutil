package com.godtips.service.impl;

import net.jforum.dao.PollDAO;

import com.godtips.service.PollService;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-9-3 下午7:55:58
 * @version v1.0
 */
public class PollServiceImpl implements PollService {
	
	private PollDAO pollDao;

	public PollDAO getPollDao() {
		return pollDao;
	}

	public void setPollDao(PollDAO pollDao) {
		this.pollDao = pollDao;
	}
	

}
