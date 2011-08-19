package com.godtips.dao.impl.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jforum.dao.AttachmentDAO;
import net.jforum.entities.Attachment;
import net.jforum.entities.AttachmentExtension;
import net.jforum.entities.AttachmentExtensionGroup;
import net.jforum.entities.AttachmentInfo;
import net.jforum.entities.QuotaLimit;

import com.godtips.base.dao.impl.BaseDaoImpl;
import com.godtips.util.StringUtils;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-8-14 下午3:05:45
 */
public class AttachmentDaoImpl extends BaseDaoImpl implements AttachmentDAO {
	/**
	 * @see net.jforum.dao.AttachmentDAO#addQuotaLimit(net.jforum.entities.QuotaLimit)
	 */
	public void addQuotaLimit(QuotaLimit limit) {
		this.addObject("AttachmentModel.addQuotaLimit", limit);
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#updateQuotaLimit(net.jforum.entities.QuotaLimit)
	 */
	public void updateQuotaLimit(QuotaLimit limit) {
		this.updateObject("AttachmentModel.updateQuotaLimit", limit);
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#cleanGroupQuota()
	 */
	public void cleanGroupQuota() {
		this.deleteObject("AttachmentModel.deleteGroupQuota", null);
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#setGroupQuota(int, int)
	 */
	public void setGroupQuota(int groupId, int quotaId) {		
		//TODO 这个要怎么传递多个参数
		this.addObjectArray("AttachmentModel.setGroupQuota", groupId,quotaId);
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#removeQuotaLimit(int)
	 */
	public void removeQuotaLimit(int id) {
		this.removeQuotaLimit(new String[] { Integer.toString(id) });
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#removeQuotaLimit(java.lang.String[])
	 */
	public void removeQuotaLimit(String[] ids) {		
		this.deleteObject("AttachmentModel.removeQuotaLimit", ids);
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#selectQuotaLimit()
	 */
	public List selectQuotaLimit() {		
		return this.queryList("AttachmentModel.selectQuotaLimit", null);
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#selectQuotaLimit()
	 */
	public QuotaLimit selectQuotaLimitByGroup(int groupId) {
		QuotaLimit ql = null;
		//TODO 这里要测试看 如果返回多个的结果是怎么样的
		//return this.findObject("AttachmentModel.selectQuotaLimitByGroup", groupId);
		List list = this.queryList("AttachmentModel.selectQuotaLimitByGroup", groupId);
		if(null != list && list.size() > 0 ){
			ql = (QuotaLimit)list.get(0);
		}
		return ql;
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#selectGroupsQuotaLimits()
	 */
	public Map selectGroupsQuotaLimits() {
		Map m = new HashMap();
		List list = this.queryList("AttachmentModel.selectGroupsQuotaLimits", null);
		if(null != list && list.size() > 0){
			for (int j = 0; j < list.size(); j++) {
				QuotaLimit ql = (QuotaLimit)list.get(j);
				m.put(ql.getId(), ql.getSize());
			}
		}
		return m;
	}

	protected QuotaLimit getQuotaLimit(ResultSet rs) throws SQLException {
		QuotaLimit ql = new QuotaLimit();
		ql.setDescription(rs.getString("quota_desc"));
		ql.setId(rs.getInt("quota_limit_id"));
		ql.setSize(rs.getInt("quota_limit"));
		ql.setType(rs.getInt("quota_type"));

		return ql;
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#addExtensionGroup(net.jforum.entities.AttachmentExtensionGroup)
	 */
	public void addExtensionGroup(AttachmentExtensionGroup g) {		
		this.addObject("AttachmentModel.addExtensionGroup", g);
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#removeExtensionGroups(java.lang.String[])
	 */
	public void removeExtensionGroups(String[] ids) {		
		this.deleteObject("AttachmentModel.removeExtensionGroups", ids);
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#selectExtensionGroups()
	 */
	public List selectExtensionGroups() {
		return this.queryList("AttachmentModel.selectExtensionGroups", null);
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#extensionsForSecurity()
	 */
	public Map extensionsForSecurity() {
		Map m = new HashMap();		
		List list = this.queryList("AttachmentModel.extensionsForSecurity", null);
		if(null != list && list.size() > 0){
			for (int j = 0; j < list.size(); j++) {
				AttachmentExtensionGroup ae = (AttachmentExtensionGroup)list.get(j);
				int allow = ae.getId();
				if (allow == 1) {
					allow = ae.getDownloadMode();
				}
				m.put(ae.getName(), Boolean.valueOf(allow == 1));
			}
		}
		return m;
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#updateExtensionGroup(net.jforum.entities.AttachmentExtensionGroup)
	 */
	public void updateExtensionGroup(AttachmentExtensionGroup g) {
		this.updateObject("AttachmentModel.updateExtensionGroups", g);
	}

	protected AttachmentExtensionGroup getExtensionGroup(ResultSet rs) throws SQLException {
		AttachmentExtensionGroup g = new AttachmentExtensionGroup();
		g.setId(rs.getInt("extension_group_id"));
		g.setName(rs.getString("name"));
		g.setUploadIcon(rs.getString("upload_icon"));
		g.setAllow(rs.getInt("allow") == 1);
		g.setDownloadMode(rs.getInt("download_mode"));

		return g;
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#addExtension(net.jforum.entities.AttachmentExtension)
	 */
	public void addExtension(AttachmentExtension extension) {
		//TODO 这个要检查
		//p.setString(4, extension.getExtension().toLowerCase());
		this.addObject("AttachmentModel.addExtension", extension);
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#removeExtensions(java.lang.String[])
	 */
	public void removeExtensions(String[] ids) {		
		this.deleteObject("AttachmentModel.removeExtension", ids);
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#selectExtensions()
	 */
	public List selectExtensions() {
		List l = new ArrayList();
		List list = this.queryList("AttachmentModel.selectExtensions", null);
		if(null != list && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				Map resMap = (Map)list.get(0);
				l.add(this.getExtension(resMap));
			}
		}
		return l;
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#updateExtension(net.jforum.entities.AttachmentExtension)
	 */
	public void updateExtension(AttachmentExtension extension) {		
		this.updateObject("AttachmentModel.updateExtension", extension);
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#selectExtension(java.lang.String)
	 */
	public AttachmentExtension selectExtension(String extension) {
		return this.searchExtension("extension", extension);
	}

	private AttachmentExtension selectExtension(int extensionId) {
		return this.searchExtension("extension_id", new Integer(extensionId));
	}

	private AttachmentExtension searchExtension(String paramName, Object paramValue) {
		AttachmentExtension e = new AttachmentExtension();
		Map map = new HashMap();
		map.put("paramName", paramName);
		map.put("paramValue", paramValue);
		List list = this.queryList("AttachmentModel.selectExtension", map);
		if(null != list && list.size() > 0){
			Map resMap = (Map)list.get(0);
			e = this.getExtension(resMap);
		}else{
			e.setUnknown(true);
		}
		return e;
	}

	protected AttachmentExtension getExtension(Map resMap){
		AttachmentExtension e = new AttachmentExtension();
		e.setAllow(1 == (Integer)resMap.get("allow"));
		e.setComment((String)resMap.get("description"));
		e.setExtension((String)resMap.get("extension"));
		e.setExtensionGroupId((Integer)resMap.get("extension_group_id"));
		e.setId((Integer)resMap.get("extension_id"));
		String icon = (String)resMap.get("upload_icon");
		if (StringUtils.isEmptyOrNullByTrim(icon)) {
			icon = (String)resMap.get("group_icon");
		}
		e.setUploadIcon(icon);
		return e;
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#addAttachment(net.jforum.entities.Attachment)
	 */
	public void addAttachment(Attachment a) {		
		//TODO 这些都必须是一个事务里面的了
		this.addObject("AttachmentModel.addAttachment", a);
		System.out.println(a.getId());
		this.addObject("AttachmentModel.addAttachmentInfo", a);
		this.updatePost(a.getPostId(), 1);
	}

	protected void updatePost(int postId, int count) {
		Map map = new HashMap();
		map.put("postId",postId);
		map.put("count", count);
		this.updateObject("AttachmentModel.updatePost", map);
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#removeAttachment(int, int)
	 */
	public void removeAttachment(int id, int postId) {
		this.deleteObject("AttachmentModel.removeAttachmentInfo", id);
		this.deleteObject("AttachmentModel.removeAttachment", id);
		int count = (Integer)this.findObject("AttachmentModel.countPostAttachments", postId);
		if(count > 0){
			this.updatePost(postId, count);
		}
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#updateAttachment(net.jforum.entities.Attachment)
	 */
	public void updateAttachment(Attachment a) {		
		this.updateObject("AttachmentModel.updateAttachment", a);
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#selectAttachments(int)
	 */
	public List selectAttachments(int postId) {		
		List l = new ArrayList();
		List list = this.queryList("AttachmentModel.selectAttachments", postId);
		if(null != list && list.size() > 0){
			for (int i = 0; i < list.size() ; i++) {
				Map resMap = (Map)list.get(i);
				l.add(this.getAttachment(resMap));
			}
		}
		return l;
	}

	protected Attachment getAttachment(Map resMap){
		Attachment a = new Attachment();
		a.setId((Integer)resMap.get("attach_id"));
		a.setPostId((Integer)resMap.get("post_id"));
		a.setPrivmsgsId((Integer)resMap.get("privmsgs_id"));
		AttachmentInfo ai = new AttachmentInfo();
		ai.setComment((String)resMap.get("description"));
		ai.setDownloadCount((Integer)resMap.get("download_count"));
		ai.setFilesize((Long)resMap.get("filesize"));
		ai.setMimetype((String)resMap.get("mimetype"));
		ai.setPhysicalFilename((String)resMap.get("physical_filename"));
		ai.setRealFilename((String)resMap.get("real_filename"));
		ai.setUploadTime(new Date(((Timestamp)resMap.get("upload_time")).getTime()));
		ai.setExtension(this.selectExtension((String)resMap.get("extension_id")));
		a.setInfo(ai);
		return a;
	}

	/**
	 * @see net.jforum.dao.AttachmentDAO#selectAttachmentById(int)
	 */
	public Attachment selectAttachmentById(int attachId) {
			Attachment e = null;
			List list = this.queryList("AttachmentModel.selectAttachmentById", attachId);
			if(null != list && list.size() > 0){
				Map resMap = (Map)list.get(0);
				e = this.getAttachment(resMap);
			}
			return e;
	}

	public boolean isPhysicalDownloadMode(int extensionGroupId) {
		boolean result = true;
		//TODO 这个要测试返回多个记录看看
		List list = this.queryList("AttachmentModel.isPhysicalDownloadMode", extensionGroupId);
		if(null != list && list.size() > 0){
			int resInt = (Integer)list.get(0);
			result = (resInt == 2);
		}
		return result;
	}
}
