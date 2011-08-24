package com.weisd.mq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Receiver2{
	public static void main(String[] args) {
		try{
			new Receiver2().execute();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void execute() throws Exception {
		//连接工厂
		ConnectionFactory connFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD,
				"tcp://localhost:61616");
		
		//连接到JMS提供者
		Connection conn = connFactory.createConnection();
		conn.start();
		
		//事务性会话，自动确认消息
		Session session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
		
		//消息的来源地
		Destination destination = session.createQueue("queue.hello");
		
		//消息消费者
		MessageConsumer consumer = session.createConsumer(destination);
		
		boolean flag = true;
		
		while(flag){
			TextMessage message = (TextMessage)consumer.receive(1000); //毫秒数
			System.out.println("2      没commit");
			session.commit(); //在事务性会话中，接收到消息后必须commit，否则下次启动接收者时还会收到旧数据
			
			if(message!=null){
				System.out.println("2    " + message.getText());
			}else{
				System.out.println("2     无消息");
				Thread.sleep(5000);
				//break;				
			}
		}
		
		consumer.close();
		session.close();
		conn.close();
	}
}