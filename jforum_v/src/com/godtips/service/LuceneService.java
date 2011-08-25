package com.godtips.service;

import java.util.Date;
import java.util.List;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-25 下午10:58:47
 * @version v1.0
 */
public interface LuceneService {

	public List getPostsToIndex(int fromPostId, int toPostId);
	
	public List getPostsData(int[] postIds);
	
	public int firstPostIdByDate(Date date);
	
	public int lastPostIdByDate(Date date);

	public int firstPostId();
}
