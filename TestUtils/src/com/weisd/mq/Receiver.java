package com.weisd.mq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQTextMessage;

public class Receiver{
	public static void main(String[] args) {
		try{
			new Receiver().execute();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void execute() throws Exception {
		//连接工厂
		ConnectionFactory connFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD,
				"tcp://localhost:6161");
		//加载消息以及并发数量
		//http://kangzye.blog.163.com/blog/static/368192232010101552139117/
		//http://log-cd.iteye.com/blog/373112
		
		//jms介绍
//		http://saupb.blog.163.com/blog/static/47124178201092544356675/
//		http://saupb.blog.163.com/blog/static/47124178201092544356675/
//		2.8 JMS 支持并发
//		JMS 对象是否支持并发
//		Destination 是
//		ConnectionFacto
//		ry
//		是
//		Connection 是
//		Session 否
//		MessageProducer 否
//		MessageConsumer 否

//		当然可以! 严格说来每个并发的producer都应该用一个单独session(虽然使所有的producer 共用同一个session，activemq也会工作的很好).为每一个consumer的并发消费创建一个session(因为所有的消息都通过一个单独的线程分发到一个session中),你可以在每个连接中有尽可能多的session。如果要进一步并发消费JMS消息，可以使用MDP（Message Dirven POJOs）。 
		//http://blog.csdn.net/hangke/article/details/2559925
		//http://webservices.ctocio.com.cn/tips/80/6047580_4.shtml
//		http://www.360doc.com/content/07/0817/08/7856_677879.shtml
		
		//连接到JMS提供者
		Connection conn = connFactory.createConnection();
		conn.start();
		
		//事务性会话，自动确认消息
		Session session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
		
		//消息的来源地
		Destination destination = session.createQueue("queue.hello");
		
		//消息消费者 .. 
		MessageConsumer consumer = session.createConsumer(destination);
		// 一旦 建立消费者 那么就会接收消息了
		
		boolean flag = true;
		
//		while(flag){
		if(flag){
			ActiveMQTextMessage message = (ActiveMQTextMessage)consumer.receive(1000); //毫秒数
//			Message message = (Message)consumer.receive(1000); //毫秒数
//			ActiveMQObjectMessage message = (ActiveMQObjectMessage)consumer.receive(1000); //毫秒数
//			message.get
			
			
			//JMSRedelivered
			boolean recFlag = false;
			int count = 0;
			if(null != message){
				recFlag = message.getJMSRedelivered();
				count = message.getRedeliveryCounter();
			}
			
			System.out.println("1        没commit:" + recFlag);
			if(message!=null){
			//	System.out.println("1   " + message.getText());
			}else{
				System.out.println("1      无消息");
				Thread.sleep(5000);
				//break;				
			}
			session.commit(); //在事务性会话中，接收到消息后必须commit，否则下次启动接收者时还会收到旧数据
			
//			if(message!=null){
//				System.out.println("1   " + message.getText());
//			}else{
//				System.out.println("1      无消息");
//				Thread.sleep(5000);
//				//break;				
//			}
		}
		
		consumer.close();
		// 这个 一关闭  那么 就不会在接受消息 ，原来已经接收的消息 会被放回 ，另一个线程重新接受
		
//		consumer.
		
		//不能再接受了
		TextMessage message = (TextMessage)consumer.receive(1000); //毫秒数
		System.out.println("1   " + message.getText());
		
		session.close();
		conn.close();
	}
}