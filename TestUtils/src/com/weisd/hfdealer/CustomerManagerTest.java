//package com.weisd.hfdealer;
//
//import java.util.HashMap;
//
//import junit.framework.TestCase;
//
//import org.apache.commons.configuration.beanutils.BeanFactory;
//  public class CustomerManagerTest extends TestCase {
//
//	private static BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext-*.xml");
//	
//	public void testAddCustomerSub() throws Exception {
//		EsalseNoticeService cm = (EsalseNoticeService)factory.getBean("esalseNoticeService");
////		Test2Service cm2 = (Test2Service)factory.getBean("testSubService");
//
//			cm.addNotice(new HashMap());
//	}
////	public void testAddCustomer() throws Exception {
////		TestService cm = (TestService)factory.getBean("testService");
////		Test2Service cm2 = (Test2Service)factory.getBean("test2Service");
////		for(int i=0; i<1; i++){
////			User u = new User();
////			u.setId(i+"");
////			u.setName("name"+i);
////			u.setPass("pass"+i);
////////			cm.addUser(u);
//////			cm.doUser(u);
////////			cm.addUser2(u);
////////			cm.doUser2(u);
////////			cm.addUser3(u);
////////			cm.doUser3(u);
//////			cm.addUser(u);
//////			cm.doUser(u);
//////			cm2.doUser(u);
//////			cm.addUser2(u);
//////			cm.doUser2(u);
//////			cm.addUser3(u);
//////			cm.doUser3(u);
//////			JtaTransactionManager
////			try{
////				cm.doUser(u);
////			}catch (Exception e) {
////				// TODO: handle exception
////				e.printStackTrace();
////			}
////			try{
////				cm2.doUser(u);
////			}catch (Exception e) {
////				// TODO: handle exception
////				e.printStackTrace();
////			}
////		}
////	}
////
////	public void testUpdateCustomer() {
////		fail("Not yet implemented");
////	}
////
////	public void testDelCustomer() {
////		CustomerManager cm = (CustomerManager)factory.getBean("customerManager");
////		for(int i=20; i<30; i++){
////			cm.delCustomer(i);
////		}
////	}
////
////	public void testFindCustomer() {
////		fail("Not yet implemented");
////	}
////
////	public void testSearchAll() {
////		fail("Not yet implemented");
////	}
////
////	public void testSearchCustomers() {
////		CustomerManager cm = (CustomerManager)factory.getBean("customerManager");
////		Customer customer = new Customer();
////		
////		//???ò?????
////		customer.setCity("????");
////		customer.setAddress("xx");
////		
////		List customers = cm.searchCustomers(customer);
////		for (Iterator iterator = customers.iterator(); iterator.hasNext();) {
////			Customer c = (Customer) iterator.next();
////			System.out.println(c.getId()+","+c.getName());
////		}
////	}
////	
////	//???hibernate?????????????4???
////	public void testSearchCustomers02() {
////		CustomerManager cm = (CustomerManager)factory.getBean("customerManager");
////		Customer customer = new Customer();
////		
////		//???ò?????
////		customer.setName("1");
////		customer.setAddress("xx");
////		
////		List customers = cm.searchCustomers(customer);
////		for (Iterator iterator = customers.iterator(); iterator.hasNext();) {
////			Customer c = (Customer) iterator.next();
////			System.out.println(c.getId()+","+c.getName());
////		}
////	}
////	
////	//???Example??????
////	public void testSearchCustomers03() {
////		CustomerManager cm = (CustomerManager)factory.getBean("customerManager");
////		Customer customer = new CompanyCustomer();//new Customer();
////		
////		//???ò?????
////		customer.setId(100); //ID????????????????
////		customer.setName("1");
////		customer.setAddress("9");
////		
////		List customers = cm.searchCustomers(customer);
////		for (Iterator iterator = customers.iterator(); iterator.hasNext();) {
////			Customer c = (Customer) iterator.next();
////			System.out.println(c.getId()+","+c.getName()+","+c.getAddress()+","+c);
////		}
////	}
//
//}
//
