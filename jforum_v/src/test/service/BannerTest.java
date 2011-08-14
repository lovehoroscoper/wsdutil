package test.service;

import static org.junit.Assert.fail;

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
public class BannerTest extends AbstractSystemTest{
	
	private static Logger logger = Logger.getLogger(BannerTest.class);
	
	@Autowired
	private BannerService bannerService;

	@Test
	public void testSelectById() {
		fail("Not yet implemented");
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
		banner.setActive(true);
		banner.setComment("haha");
		banner.setType(1);
		banner.setWidth(20);
		banner.setHeight(20);
		
		int resInt = bannerService.addNew(banner);
		
		logger.info(resInt);
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindDelete() {
		fail("Not yet implemented");
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectActiveBannerByPlacement() {
		fail("Not yet implemented");
	}

}
