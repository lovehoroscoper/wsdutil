package test.service;

import java.util.List;

import net.jforum.entities.Banner;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.AbstractSystemTest;

import com.godtips.service.BannerService;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-14 上午1:28:52
 * @version v1.0
 */
public class BannerTest extends AbstractSystemTest {

	private static Logger logger = Logger.getLogger(BannerTest.class);

	@Autowired
	private BannerService bannerService;

	@Test
	public void testSelectById() {
		// Banner b = bannerService.selectById(1);
		Banner b = bannerService.selectById(3);
		logger.info(b.isActive());
	}

	@Test
	public void testSelectAll() {
		List list = bannerService.selectAll();
		logger.info(list.size());
	}

	@Test
	public void testAddNew() {
		Banner banner = new Banner();
		banner.setName("weisd");
		banner.setPlacement(1);
		banner.setDescription("测试");
		banner.setClicks(1);
		banner.setViews(1);
		banner.setUrl("http://www.godtips.com");
		banner.setWeight(10);
		// banner.setActive(true);
		banner.setActive(false);
		banner.setComment("haha");
		banner.setType(1);
		banner.setWidth(20);
		banner.setHeight(20);

		int resInt = bannerService.addNew(banner);
		logger.info(resInt);
	}

	@Test
	public void testUpdate() {
//		Banner banner = new Banner();
//		banner.setName("weisd2");
//		banner.setPlacement(2);
//		banner.setDescription("测试2");
//		banner.setClicks(2);
//		banner.setViews(2);
//		banner.setUrl("http://www.godtips.com/ccav");
//		banner.setWeight(2);
//		banner.setActive(true);
//		banner.setComment("haha2");
//		banner.setType(2);
//		banner.setWidth(22);
//		banner.setHeight(22);
//		
//		banner.setId(2);
		Banner banner = new Banner();
		banner.setName("weisd3");
		banner.setPlacement(2);
		banner.setDescription("测试3");
		banner.setClicks(3);
		banner.setViews(3);
		banner.setUrl("http://www.godtips.com/ccav3");
		banner.setWeight(3);
		banner.setActive(true);
		banner.setComment("haha3");
		banner.setType(3);
		banner.setWidth(3);
		banner.setHeight(3);

		banner.setId(2);

		bannerService.update(banner);
	}

	@Test
	public void testFindDelete() {
		boolean resFlag = bannerService.findDelete(5);
		logger.info(resFlag);
	}

	@Test
	public void testDelete() {
		bannerService.delete(3);
	}

	@Test
	public void testSelectActiveBannerByPlacement() {
		List list = bannerService.selectActiveBannerByPlacement(1);
		logger.info(list.size());
	}

}
