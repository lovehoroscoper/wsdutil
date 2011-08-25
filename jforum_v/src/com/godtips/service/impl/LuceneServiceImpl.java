package com.godtips.service.impl;

import java.util.Date;
import java.util.List;

import net.jforum.dao.LuceneDAO;

import com.godtips.service.LuceneService;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-25 下午10:59:23
 * @version v1.0
 */
public class LuceneServiceImpl implements LuceneService {
	
	private LuceneDAO luceneDao;
	
	public LuceneDAO getLuceneDao() {
		return luceneDao;
	}

	public void setLuceneDao(LuceneDAO luceneDao) {
		this.luceneDao = luceneDao;
	}

	@Override
	public List getPostsToIndex(int fromPostId, int toPostId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getPostsData(int[] postIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int firstPostIdByDate(Date date) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int lastPostIdByDate(Date date) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int firstPostId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
