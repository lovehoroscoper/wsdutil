package test;

import java.util.List;

import net.jforum.entities.Banlist;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.godtips.service.BanlistService;

/**
 * 
 * @Description: 
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-8-13 下午9:30:57
 * @version v1.0
 */
public class BanlistTest extends AbstractSystemTest{
	
	private static Logger logger = Logger.getLogger(BanlistTest.class);
	
	@Autowired
	private BanlistService banlistService;

	@Test
	public void testInsert() {
		/*Banlist b = new Banlist();
		b.setUserId(777);
		b.setIp("192.168.0.1");
		b.setEmail("xiyangdewuse@163.com");
		banlistService.insert(b);
		//获取到主键ID
		logger.info(b.getId());*/
		
		for (int i = 0; i < 10; i++) {
			Banlist b = new Banlist();
			b.setUserId(777);
			b.setIp("192.168.0.1");
			b.setEmail("xiyangdewuse@163.com");
			banlistService.insert(b);
			//获取到主键ID
			logger.info(b.getId());
		}
	}

	@Test
	public void testDelete() {
		banlistService.delete(20);
	}

	@Test
	public void testSelectAll() {
		List list = banlistService.selectAll();
		//即使是没数据 也不会返回null，size = 0
		logger.info(list.size());
	}

}
