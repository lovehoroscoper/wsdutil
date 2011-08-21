package com.godtips.dao.impl.mysql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jforum.dao.BookmarkDAO;
import net.jforum.entities.Bookmark;
import net.jforum.entities.BookmarkType;
import net.jforum.exceptions.InvalidBookmarkTypeException;

import com.godtips.base.dao.impl.BaseDaoImpl;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-21 下午8:58:55
 * @version v1.0
 */
public class BookmarkDaoImpl extends BaseDaoImpl implements BookmarkDAO {

	@Override
	public void add(Bookmark b) {
		this.addObject("BookmarkModel.add", b);
	}

	@Override
	public void update(Bookmark b) {
		this.updateObject("BookmarkModel.update", b);
	}

	@Override
	public void remove(int bookmarkId) {
		this.deleteObject("BookmarkModel.remove", bookmarkId);
	}

	@Override
	public List selectByUser(int userId, int relationType) {
		if (relationType == BookmarkType.FORUM) {
			return this.getForums(userId);
		}
		else if (relationType == BookmarkType.TOPIC) {
			return this.getTopics(userId);
		}
		else if (relationType == BookmarkType.USER) {
			return this.getUsers(userId);
		}
		else {
			throw new InvalidBookmarkTypeException("The type " + relationType + " is not a valid bookmark type");
		}
	}
	
	@Override
	public List selectByUser(int userId) {
		return this.queryList("BookmarkModel.selectUserBookmarks", userId);
	}

	@Override
	public Bookmark selectById(int bookmarkId) {
		Bookmark b = null;
		List list = this.queryList("BookmarkModel.selectById", bookmarkId);
		if(null != list && list.size() > 0){
			b = (Bookmark)list.get(0);
		}
		return b;
	}

	@Override
	public Bookmark selectForUpdate(int relationId, int relationType, int userId) {
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("relationId",relationId);
		map.put("relationType",relationType);
		map.put("userId",userId);
		List list = this.queryList("BookmarkModel.selectForUpdat", map);
		Bookmark b = null;
		if(null != list && list.size() > 0){
			b = (Bookmark)list.get(0);
		}
		return b;
	}

	protected List getForums(int userId){		
		return this.queryList("BookmarkModel.selectForumBookmarks", userId);
	}

	protected List getTopics(int userId){
		return this.queryList("BookmarkModel.selectTopicBookmarks", userId);
	}
	
	protected List getUsers(int userId){
		return this.queryList("BookmarkModel.selectUserBookmarks", userId);
	}
	
}
