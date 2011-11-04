package test;

import junit.framework.TestCase;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wsd.service.TestService;
import com.wsd.vo.User;



public class SpringJTATest extends TestCase {

	private static BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext-*.xml");
	
	public void testAddCustomer() throws Exception {
		TestService cm = (TestService)factory.getBean("testService");
		User u = new User();
		
		cm.addUser(u);
	}
	
	public void testAddCustomer2() throws Exception {
		TestService cm = (TestService)factory.getBean("testTypeService");
		User u = new User();

		cm.addUser(u);
	}

}
