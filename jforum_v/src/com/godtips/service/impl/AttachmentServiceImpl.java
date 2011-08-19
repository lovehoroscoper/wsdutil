package com.godtips.service.impl;

import java.util.List;
import java.util.Map;

import net.jforum.dao.AttachmentDAO;
import net.jforum.entities.Attachment;
import net.jforum.entities.AttachmentExtension;
import net.jforum.entities.AttachmentExtensionGroup;
import net.jforum.entities.QuotaLimit;

import com.godtips.service.AttachmentService;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-8-14 下午4:09:22
 */
public class AttachmentServiceImpl implements AttachmentService {
	
	private AttachmentDAO attachmentDao;

	@Override
	public void addAttachment(Attachment a) {
		attachmentDao.addAttachment(a);
	}

	@Override
	public void updateAttachment(Attachment a) {
		attachmentDao.updateAttachment(a);
	}

	@Override
	public void removeAttachment(int id, int postId) {
		attachmentDao.removeAttachment(id, postId);
	}

	@Override
	public List selectAttachments(int postId) {
		return attachmentDao.selectAttachments(postId);
	}

	@Override
	public Attachment selectAttachmentById(int attachId) {
		return attachmentDao.selectAttachmentById(attachId);
	}

	@Override
	public void addQuotaLimit(QuotaLimit limit) {
		attachmentDao.addQuotaLimit(limit);
	}

	@Override
	public void updateQuotaLimit(QuotaLimit limit) {
		attachmentDao.updateQuotaLimit(limit);
	}

	@Override
	public void removeQuotaLimit(int id) {
		attachmentDao.removeQuotaLimit(id);
	}

	@Override
	public void removeQuotaLimit(String[] ids) {
		attachmentDao.removeQuotaLimit(ids);
	}

	@Override
	public void setGroupQuota(int groupId, int quotaId) {
		attachmentDao.setGroupQuota(groupId, quotaId);
	}

	@Override
	public void cleanGroupQuota() {
		attachmentDao.cleanGroupQuota();
	}

	@Override
	public List selectQuotaLimit() {
		return attachmentDao.selectQuotaLimit();
	}

	@Override
	public QuotaLimit selectQuotaLimitByGroup(int groupId) {
		return attachmentDao.selectQuotaLimitByGroup(groupId);
	}

	@Override
	public Map selectGroupsQuotaLimits() {
		return attachmentDao.selectGroupsQuotaLimits();
	}

	@Override
	public void addExtensionGroup(AttachmentExtensionGroup g) {
		attachmentDao.addExtensionGroup(g);
	}

	@Override
	public void updateExtensionGroup(AttachmentExtensionGroup g) {
		attachmentDao.updateExtensionGroup(g);
	}

	@Override
	public void removeExtensionGroups(String[] ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List selectExtensionGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map extensionsForSecurity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addExtension(AttachmentExtension e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateExtension(AttachmentExtension e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeExtensions(String[] ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List selectExtensions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AttachmentExtension selectExtension(String extension) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPhysicalDownloadMode(int extensionGroupId) {
		// TODO Auto-generated method stub
		return false;
	}

	public AttachmentDAO getAttachmentDao() {
		return attachmentDao;
	}

	public void setAttachmentDao(AttachmentDAO attachmentDao) {
		this.attachmentDao = attachmentDao;
	}

}
