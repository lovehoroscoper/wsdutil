package test;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.godtips.service.ApiService;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-8-13 下午3:42:23
 */
public class ApiTest {
	
	private static Logger logger = Logger.getLogger(ApiTest.class);
	private static BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext-*.xml");

	@Test
	public void testFindValid() {
		ApiService apiService = (ApiService) factory.getBean("apiService");
		boolean flag = apiService.findValid("api");
		logger.debug(flag);
		logger.info(flag);
	}

}
