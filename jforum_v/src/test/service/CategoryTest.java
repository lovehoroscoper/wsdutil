package test.service;

import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.AbstractSystemTest;

import com.godtips.service.CategoryService;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-23 下午8:18:02
 * @version v1.0
 */
public class CategoryTest extends AbstractSystemTest{
	
	private static Logger logger = Logger.getLogger(CategoryTest.class);

	@Autowired
	private CategoryService categoryService;

	@Test
	public void testSelectById() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testCanDelete() {
		fail("Not yet implemented");
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNew() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetOrderUp() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetOrderDown() {
		fail("Not yet implemented");
	}

}
