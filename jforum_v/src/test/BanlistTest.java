package test;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @desc 描述：
 *
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-8-13 下午7:09:56
 */
public class BanlistTest {

	private static Logger logger = Logger.getLogger(BanlistTest.class);
	private static BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext-*.xml");
	
	@Test
	public void insert(){
		
	};
	@Test
	public void delete(){
		
	};
	@Test
	public void selectAll(){
		
		
	};
}
