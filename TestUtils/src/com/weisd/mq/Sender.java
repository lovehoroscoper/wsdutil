package com.weisd.mq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQBytesMessage;

public class Sender{
	public static void main(String[] args) {
		try{
			new Sender().execute();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void execute() throws Exception {
		//连接工厂
		ConnectionFactory connFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD,
				"tcp://172.25.25.94:6161");
//				"tcp://172.25.25.123:61616");
//				"tcp://172.25.25.161:61616");
		
		//连接到JMS提供者
		Connection conn = connFactory.createConnection();
		conn.start();
		
		//事务性会话，自动确认消息		
		//0-SESSION_TRANSACTED    1-AUTO_ACKNOWLEDGE   		
		//2-CLIENT_ACKNOWLEDGE    3-DUPS_OK_ACKNOWLEDGE
		Session session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
		
		//消息的目的地
//		Destination destination = session.createQueue("ebs_async");
		Destination destination = session.createQueue("ebs.req");
		
		//消息生产者		
		//1-NON_PERSISTENT  2-PERSISTENT
		MessageProducer producer = session.createProducer(destination);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT); //不持久化
		
		//创建文本消息
//		TextMessage message = session.createTextMessage("Hello ActiveMQ1");
//		
//		//发送消息
//		producer.send(message);
		for (int i = 0; i < 1; i++) {
//			TextMessage message = session.createTextMessage("Hello ActiveMQ1_" + i);
			ActiveMQBytesMessage message = (ActiveMQBytesMessage)session.createBytesMessage();
			message.writeBytes("weisd".getBytes());
			
			//发送消息 setJMSExpiration
//			message.setJMSExpiration(10000);
			
			producer.send(message);
			session.commit(); //在事务性会话中，只有commit之后，消息才会真正到达目的地
		}
		System.out.println("33333333");
//		for (int i = 0; i < 5000; i++) {
//			TextMessage message = session.createTextMessage("Hello ActiveMQ1_" + i);
//			//发送消息
//			producer.send(message);
//			session.commit(); //在事务性会话中，只有commit之后，消息才会真正到达目的地
//		}

		
//		session.commit(); //在事务性会话中，只有commit之后，消息才会真正到达目的地
		producer.close();
		session.close();
		conn.close();
	}
}