package com.godtips.service.impl;

import java.util.List;

import net.jforum.dao.BookmarkDAO;
import net.jforum.entities.Bookmark;

import com.godtips.service.BookmarkService;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-21 下午8:59:32
 * @version v1.0
 */
public class BookmarkServiceImpl implements BookmarkService {
	
	private BookmarkDAO bookmarkDao;

	public BookmarkDAO getBookmarkDao() {
		return bookmarkDao;
	}

	public void setBookmarkDao(BookmarkDAO bookmarkDao) {
		this.bookmarkDao = bookmarkDao;
	}

	@Override
	public void add(Bookmark b) {
		bookmarkDao.add(b);
	}

	@Override
	public void update(Bookmark b) {
		bookmarkDao.update(b);
	}

	@Override
	public void delete(int bookmarkId) {
		bookmarkDao.remove(bookmarkId);
	}

	@Override
	public List selectByUser(int userId, int relationType) {
		return bookmarkDao.selectByUser(userId, relationType);
	}

	@Override
	public List selectByUser(int userId) {
		return bookmarkDao.selectByUser(userId);
	}

	@Override
	public Bookmark selectById(int bookmarkId) {
		return bookmarkDao.selectById(bookmarkId);
	}

	@Override
	public Bookmark selectForUpdate(int relationId, int relationType, int userId) {
		return bookmarkDao.selectForUpdate(relationId, relationType, userId);
	}
	
}
