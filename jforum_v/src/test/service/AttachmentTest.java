package test.service;

import java.util.List;
import java.util.Map;

import net.jforum.entities.Attachment;
import net.jforum.entities.AttachmentExtension;
import net.jforum.entities.AttachmentExtensionGroup;
import net.jforum.entities.AttachmentInfo;
import net.jforum.entities.QuotaLimit;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.AbstractSystemTest;

import com.godtips.service.AttachmentService;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-8-14 下午4:21:36
 */
public class AttachmentTest extends AbstractSystemTest {

	private static Logger logger = Logger.getLogger(AttachmentTest.class);

	@Autowired
	private AttachmentService attachmentService;

	@Test
	public void testAddAttachment() {
		
//		PreparedStatement p = null;
//		try {
//			p = this.getStatementForAutoKeys("AttachmentModel.addAttachment");
//			p.setInt(1, a.getPostId());
//			p.setInt(2, a.getPrivmsgsId());
//			p.setInt(3, a.getUserId());
//
//			this.setAutoGeneratedKeysQuery(SystemGlobals.getSql("AttachmentModel.lastGeneratedAttachmentId"));
//			int id = this.executeAutoKeysQuery(p);
//			p.close();
//			p = null;
//
//			p = JForumExecutionContext.getConnection().prepareStatement(
//					SystemGlobals.getSql("AttachmentModel.addAttachmentInfo"));
//			p.setInt(1, id);
//			p.setString(2, a.getInfo().getPhysicalFilename());
//			p.setString(3, a.getInfo().getRealFilename());
//			p.setString(4, a.getInfo().getComment());
//			p.setString(5, a.getInfo().getMimetype());
//			p.setLong(6, a.getInfo().getFilesize());
//			p.setTimestamp(7, new Timestamp(a.getInfo().getUploadTimeInMillis()));
//			p.setInt(8, 0);
//			p.setInt(9, a.getInfo().getExtension().getId());
//			p.executeUpdate();
//
//			this.updatePost(a.getPostId(), 1);
			
			for (int i = 0; i < 5; i++) {
				Attachment a = new Attachment();
				a.setPostId(i);
				a.setUserId(i);
				a.setPrivmsgsId(i);
				
				AttachmentInfo info = new AttachmentInfo();
				info.setPhysicalFilename("py_"+i);
				info.setRealFilename("real_"+i);
				info.setComment("comm_"+i);
				info.setMimetype("mime_"+i);
				info.setFilesize(i);
				info.setUploadTimeInMillis(50000);
				a.setInfo(info);
				attachmentService.addAttachment(a);
			}


	}

	@Test
	public void testUpdateAttachment() {
		//ok
		Attachment a = new Attachment();
		AttachmentInfo info = new AttachmentInfo();
		info.setComment("upcomm");
		info.setDownloadCount(3);
		a.setId(6);
		a.setInfo(info);
		
		attachmentService.updateAttachment(a);
	}

	@Test
	public void testRemoveAttachment() {
		//未完成
		attachmentService.deleteAttachment(1, 1);
	}

	@Test
	public void testSelectAttachments() {
		//ok
		List list = attachmentService.selectAttachments(3);
		logger.info(list.size());
	}

	@Test
	public void testSelectAttachmentById() {
		//ok
		Attachment a= attachmentService.selectAttachmentById(6);
		logger.info(a);
	}

	@Test
	public void testAddQuotaLimit() {
		// ok
		for (int i = 0; i < 10; i++) {
			QuotaLimit limit = new QuotaLimit();
			limit.setDescription("tsets" + i);
			limit.setId(i);
			limit.setSize(i);
			limit.setType(i);
			attachmentService.addQuotaLimit(limit);
		}
	}

	@Test
	public void testUpdateQuotaLimit() {
		// ok
		for (int i = 0; i < 10; i++) {
			QuotaLimit limit = new QuotaLimit();
			limit.setDescription("update" + i);
			limit.setId(i);
			limit.setSize(i + 1);
			limit.setType(i + 1);
			attachmentService.updateQuotaLimit(limit);
		}
	}

	@Test
	public void testRemoveQuotaLimitInt() {
		// ok
		attachmentService.deleteQuotaLimit(2);
	}

	@Test
	public void testRemoveQuotaLimitStringArray() {
		// ok
		attachmentService.deleteQuotaLimit(new int[] { 1, 5 });
	}

	@Test
	public void testSetGroupQuota() {
		// ok
		for (int i = 0; i < 10; i++) {
			attachmentService.addGroupQuota(i, i);
		}

	}

	@Test
	public void testCleanGroupQuota() {
		// ok
		attachmentService.deleteGroupQuota();
	}

	@Test
	public void testSelectQuotaLimit() {
		// ok
		List list = attachmentService.selectQuotaLimit();
		logger.info(list.size());
		logger.info(list);
	}

	@Test
	public void testSelectQuotaLimitByGroup() {
		// QuotaLimit ql = attachmentService.selectQuotaLimitByGroup(6);
		// ok
		QuotaLimit ql = attachmentService.selectQuotaLimitByGroup(4);
		logger.info(ql);
	}

	@Test
	public void testSelectGroupsQuotaLimits() {
		// ok
		Map map = attachmentService.selectGroupsQuotaLimits();
		logger.info(map);
	}

	@Test
	public void testAddExtensionGroup() {
		//ok
		for (int i = 0; i < 10; i++) {
			AttachmentExtensionGroup e = new AttachmentExtensionGroup();
			e.setName("eName_" + i);
			e.setAllow(i > 4);
			e.setUploadIcon("http://image");
			e.setDownloadMode(i);
			attachmentService.addExtensionGroup(e);
		}

	}

	@Test
	public void testUpdateExtensionGroup() {
		//ok
		for (int i = 0; i < 10; i++) {
			AttachmentExtensionGroup g = new AttachmentExtensionGroup();
			g.setName("updateNmae_" + i);
			g.setAllow(i < 5);
			g.setUploadIcon("updddd");
			g.setDownloadMode(i+1);
			g.setId(i+1);
			
			attachmentService.updateExtensionGroup(g);
		}

	}

	@Test
	public void testRemoveExtensionGroups() {
		//ok
		attachmentService.deleteExtensionGroups(new int[]{1,2});
	}

	@Test
	public void testSelectExtensionGroups() {
		//ok
		List list = attachmentService.selectExtensionGroups();
		logger.info(list.size());
	}

	@Test
	public void testExtensionsForSecurity() {
		//ok
		Map map = attachmentService.queryExtensionsForSecurity();
		logger.info(map);
	}

	@Test
	public void testAddExtension() {
		//ok
		for (int i = 0; i < 10; i++) {
			AttachmentExtension e = new AttachmentExtension();
			
			e.setExtensionGroupId(i+1);
			e.setComment("comm_" + i);
			e.setUploadIcon("addUP");
			e.setExtension("EEhhER2");
			e.setAllow(i>5);
			attachmentService.addExtension(e);
		}
	}

	@Test
	public void testUpdateExtension() {
		//ok
		AttachmentExtension e = new AttachmentExtension();
		e.setExtensionGroupId(6);
		e.setComment("up");
		e.setUploadIcon("up");
		e.setExtension("HHHdddDDD");
		e.setAllow(false);
		e.setId(8);
		
		attachmentService.updateExtension(e);
		

	}

	@Test
	public void testRemoveExtensions() {
		//ok
		attachmentService.deleteExtensions(new int[]{4,6}	);
	}

	@Test
	public void testSelectExtensions() {
		// error
		List list = attachmentService.selectExtensions();
		logger.info(list);
	}

	@Test
	public void testSelectExtension() {
		//ok
		//attachmentService.selectExtension("eehher2");
		attachmentService.selectExtension("eehher8");
	}

	@Test
	public void testIsPhysicalDownloadMode() {
		//ok
		boolean f = attachmentService.findPhysicalDownloadMode(6);
		logger.info(f);
	}

}
