package test.service;

import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.AbstractSystemTest;

import com.godtips.service.ConfigService;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-23 下午9:32:05
 * @version v1.0
 */
public class ConfigTest extends AbstractSystemTest{

	private static Logger logger = Logger.getLogger(ConfigTest.class);

	@Autowired
	private ConfigService configService;
	
	@Test
	public void testInsert() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectByName() {
		fail("Not yet implemented");
	}

}
