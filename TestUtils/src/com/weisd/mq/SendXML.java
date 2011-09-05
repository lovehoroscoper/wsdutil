package com.weisd.mq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQBytesMessage;

import com.weisd.fmt.ftl.FreemarkerGetXml;

/**
 * 
 * @Description:
 * 
 * @author weisd Email:xiyangdewuse@163.com
 * @date 2011-9-1 下午5:57:06
 * @version v1.0
 */
public class SendXML {

	public static void main(String[] args)  {
		search();
	}
	
	public static void search()  {
		
		try {
			// 连接工厂
			ConnectionFactory connFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD,
			// "tcp://172.25.53.94:61616");
					"tcp://172.25.25.94:6161");
//					"tcp://192.168.1.100:6161");

			// 连接到JMS提供者
			Connection conn = connFactory.createConnection();
			conn.start();

			// 事务性会话，自动确认消息
			// 0-SESSION_TRANSACTED 1-AUTO_ACKNOWLEDGE
			// 2-CLIENT_ACKNOWLEDGE 3-DUPS_OK_ACKNOWLEDGE
			Session session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);

			// 消息的目的地
			Destination destination = session.createQueue("ebs_async_req");

			// 消息生产者
			// 1-NON_PERSISTENT 2-PERSISTENT
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT); // 不持久化

//			 创建文本消息
//			 TextMessage message = session.createTextMessage("Hello ActiveMQ1");
//			
//			 //发送消息
//			 producer.send(message);
			
				String xml = FreemarkerGetXml.search(1);
				// TextMessage message =
				// session.createTextMessage("Hello ActiveMQ1_" + i);
				ActiveMQBytesMessage message = (ActiveMQBytesMessage) session.createBytesMessage();
				message.writeBytes(xml.getBytes("UTF-8"));

				// 发送消息
				producer.send(message);
				session.commit(); // 在事务性会话中，只有commit之后，消息才会真正到达目的地
			System.out.println("33333333");
			producer.close();
			session.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void order()  {
		
		try {
			// 连接工厂
			ConnectionFactory connFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD,
			// "tcp://172.25.53.94:61616");
					"tcp://172.25.25.94:6161");
//					"tcp://192.168.1.100:6161");

			// 连接到JMS提供者
			Connection conn = connFactory.createConnection();
			conn.start();

			// 事务性会话，自动确认消息
			// 0-SESSION_TRANSACTED 1-AUTO_ACKNOWLEDGE
			// 2-CLIENT_ACKNOWLEDGE 3-DUPS_OK_ACKNOWLEDGE
			Session session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);

			// 消息的目的地
			Destination destination = session.createQueue("ebs_async_req");

			// 消息生产者
			// 1-NON_PERSISTENT 2-PERSISTENT
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT); // 不持久化

//			 创建文本消息
//			 TextMessage message = session.createTextMessage("Hello ActiveMQ1");
//			
//			 //发送消息
//			 producer.send(message);
			
			for (int i = 101; i < 201; i++) {
				String xml = FreemarkerGetXml.sendXML(i);
				// TextMessage message =
				// session.createTextMessage("Hello ActiveMQ1_" + i);
				ActiveMQBytesMessage message = (ActiveMQBytesMessage) session.createBytesMessage();
				message.writeBytes(xml.getBytes("UTF-8"));

				// 发送消息
				producer.send(message);
				session.commit(); // 在事务性会话中，只有commit之后，消息才会真正到达目的地
			}
			System.out.println("33333333");
			producer.close();
			session.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
