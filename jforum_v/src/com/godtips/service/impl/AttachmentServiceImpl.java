package com.godtips.service.impl;

import java.util.List;
import java.util.Map;

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

	@Override
	public void addAttachment(Attachment a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAttachment(Attachment a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAttachment(int id, int postId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List selectAttachments(int postId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attachment selectAttachmentById(int attachId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addQuotaLimit(QuotaLimit limit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateQuotaLimit(QuotaLimit limit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeQuotaLimit(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeQuotaLimit(String[] ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGroupQuota(int groupId, int quotaId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cleanGroupQuota() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List selectQuotaLimit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuotaLimit selectQuotaLimitByGroup(int groupId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map selectGroupsQuotaLimits() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addExtensionGroup(AttachmentExtensionGroup g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateExtensionGroup(AttachmentExtensionGroup g) {
		// TODO Auto-generated method stub
		
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

}
