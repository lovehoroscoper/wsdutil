package test.service;

import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.AbstractSystemTest;

import com.godtips.service.BookmarkService;

/**
 * 
 * @Description: 未测试
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-21 下午9:54:21
 * @version v1.0
 */
public class BookmarkTest  extends AbstractSystemTest {
	
	private static Logger logger = Logger.getLogger(BookmarkTest.class);

	@Autowired
	private BookmarkService bookmarkService;

	@Test
	public void testAdd() {
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
	public void testSelectByUserIntInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectByUserInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectById() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectForUpdate() {
		fail("Not yet implemented");
	}

}
