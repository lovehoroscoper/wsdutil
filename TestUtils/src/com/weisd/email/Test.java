package com.weisd.email;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * @desc 描述：
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2011-7-26 下午2:54:33
 */
public class Test {

//	/**
//	 * @param args
//	 */
	 public static void main(String[] args) {

		 try {
			test1();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	 }
	 public static void test1() throws EmailException {
		 // TODO Auto-generated method stub
		 
			HtmlEmail email = new HtmlEmail();
			email.setCharset("UTF-8");
//			String host = Config.getProperty("EMAILSERVERADDRESS");
//			String user = Config.getProperty("EMAILSERVERNAME");
//			String pass = Config.getProperty("EMAILSERVERPASSWORD");
			String host = "220.189.210.142";
			String user = "junbaohf@junbao.net";
			String pass = "998835";
			String from = "junbaohf@junbao.net";
//			String host = "smtp.junbao.net";
//			String user = "weisd@junbao.net";
//			String pass = "wad123456";
//			String from = "weisd@junbao.net";
//			String host = "smtp.163.com";
//			String user = "weisd_test007008@163.com";
//			String pass = "123456";
//			String from = "weisd_test007008@163.com";
			
			email.setHostName(host);
			email.setAuthentication(user, pass);			

			email.addTo("weisd@junbao.net");
			email.setFrom(from, "君宝科技");
			email.setSubject("fffff");
			email.setHtmlMsg("<html><head></head><body>ha33333333333ha </body></html>");
			email.send();
	 }
	 public static void test3() {
	 // TODO Auto-generated method stub
	
	  List toEmails = new ArrayList();
	  toEmails.add("weisd@junbao.net");
	  List toBBCEmails = new ArrayList();
	  toBBCEmails.add("weisd@junbao.net");
	 
	 
	 
//	  EmailUtil.sendMail(toEmails, null, "ddd","232323");
	 }
	
	public static void test() {
		// TODO Auto-generated method stub

		HtmlEmail email = new HtmlEmail();
		email.setCharset("UTF-8");
		email.setHostName("smtp.junbao.net");
		try {
			email.addTo("weisd@junbao.net", "weisd");
			email.setFrom("weisd@junbao.net", "weisd");
			email.setSubject("这是一个主题");
			email.setAuthentication("weisd@junbao.net", "wad20042004");
			email.setMsg("这是一个消息...........");
			//URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");
			//String cid = email.embed(url, "Apache logo");
			email.setHtmlMsg("<html>测试十四和</html>");
			email.setTextMsg("Your email client does not support HTML messages 这是一个TEXTMEG");
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
		System.out.println("发送成功！");
	}

}
