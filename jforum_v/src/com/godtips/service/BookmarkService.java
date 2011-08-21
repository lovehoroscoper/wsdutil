package com.godtips.service;

import java.util.List;

import net.jforum.entities.Bookmark;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-21 下午8:59:11
 * @version v1.0
 */
public interface BookmarkService {

	public void add(Bookmark b);

	public void update(Bookmark b);

	public void delete(int bookmarkId);

	public List selectByUser(int userId, int relationType);

	public List selectByUser(int userId);

	public Bookmark selectById(int bookmarkId);

	public Bookmark selectForUpdate(int relationId, int relationType, int userId);

}
