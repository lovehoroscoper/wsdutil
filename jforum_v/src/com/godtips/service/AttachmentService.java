package com.godtips.service;

import java.util.List;
import java.util.Map;

import net.jforum.entities.Attachment;
import net.jforum.entities.AttachmentExtension;
import net.jforum.entities.AttachmentExtensionGroup;
import net.jforum.entities.QuotaLimit;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-8-14 下午4:08:54
 */
public interface AttachmentService {

	public void addAttachment(Attachment a);

	public void updateAttachment(Attachment a);

	public void removeAttachment(int id, int postId);

	public List selectAttachments(int postId);

	public Attachment selectAttachmentById(int attachId);

	public void addQuotaLimit(QuotaLimit limit);

	public void updateQuotaLimit(QuotaLimit limit);

	public void removeQuotaLimit(int id);

	public void removeQuotaLimit(String[] ids);

	public void setGroupQuota(int groupId, int quotaId);

	public void cleanGroupQuota();

	public List selectQuotaLimit();

	public QuotaLimit selectQuotaLimitByGroup(int groupId);

	public Map selectGroupsQuotaLimits();

	public void addExtensionGroup(AttachmentExtensionGroup g);

	public void updateExtensionGroup(AttachmentExtensionGroup g);

	public void removeExtensionGroups(String[] ids);

	public List selectExtensionGroups();

	public Map extensionsForSecurity();

	public void addExtension(AttachmentExtension e);

	public void updateExtension(AttachmentExtension e);

	public void removeExtensions(String[] ids);

	public List selectExtensions();

	public AttachmentExtension selectExtension(String extension);

	public boolean isPhysicalDownloadMode(int extensionGroupId);
}
