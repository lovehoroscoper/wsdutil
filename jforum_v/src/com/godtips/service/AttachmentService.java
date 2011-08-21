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

	/**
	 * old name : removeAttachment
	 * @param id
	 * @param postId
	 */
	public void deleteAttachment(int id, int postId);

	public List selectAttachments(int postId);

	public Attachment selectAttachmentById(int attachId);

	public void addQuotaLimit(QuotaLimit limit);

	public void updateQuotaLimit(QuotaLimit limit);

	/**
	 * removeQuotaLimit
	 * @param id
	 */
	public void deleteQuotaLimit(int id);

	public void deleteQuotaLimit(int[] ids);

	/**
	 * old name : setGroupQuota
	 * @param groupId
	 * @param quotaId
	 */
	public void addGroupQuota(int groupId, int quotaId);

	public void deleteGroupQuota();

	public List selectQuotaLimit();

	public QuotaLimit selectQuotaLimitByGroup(int groupId);

	public Map selectGroupsQuotaLimits();

	public void addExtensionGroup(AttachmentExtensionGroup g);

	public void updateExtensionGroup(AttachmentExtensionGroup g);

	/**
	 * old name : removeExtensionGroups
	 * @param ids
	 */
	public void deleteExtensionGroups(int[] ids);

	public List selectExtensionGroups();

	/**
	 * old name : extensionsForSecurity
	 * @return
	 */
	public Map queryExtensionsForSecurity();

	public void addExtension(AttachmentExtension e);

	public void updateExtension(AttachmentExtension e);

	/**
	 * OLD NAME : removeExtensions
	 * @param ids
	 */
	public void deleteExtensions(int[] ids);

	public List selectExtensions();

	/**
	 * tinyint 返回hashmap数据不匹配
	 * @param extension
	 * @return
	 */
	public AttachmentExtension selectExtension(String extension);

	/**
	 * old name : isPhysicalDownloadMode
	 * @param extensionGroupId
	 * @return
	 */
	public boolean findPhysicalDownloadMode(int extensionGroupId);
}
