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
import org.apache.activemq.command.ActiveMQBytesMessage;

public class ReceiverOrderRes {
	public static void main(String[] args) {
		try {
			new ReceiverOrderRes().execute();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * JMSRedelivered 如果一个客户端收到一个设置了JMSRedelivered
	 * 属性的消息，则表示可能该客户端曾经在早些时候收到过该消息，但并没有签收(acknowledged)。 JMSType 消息类型的识别符。
	 * 
	 * @throws Exception
	 */
	public void execute() throws Exception {
		// 连接工厂
		ConnectionFactory connFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, 
				ActiveMQConnection.DEFAULT_PASSWORD, 
//				"tcp://172.25.25.161:61616");
				"tcp://115.238.110.119:61616");

		// 连接到JMS提供者
		Connection conn = connFactory.createConnection();
		conn.start();

		// 事务性会话，自动确认消息
		Session session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);

		// 消息的来源地
//		Destination destination = session.createQueue("ebs.resp");
		Destination destination = session.createQueue("ebs_async");

		// 消息消费者
		MessageConsumer consumer = session.createConsumer(destination);

		boolean flag = true;

		while (flag) {
			// TextMessage message = (TextMessage)consumer.receive(1000); //毫秒数

			Message message = consumer.receive(1000);
			if (null == message) {
				break;
			}

			if (message instanceof TextMessage) {
				System.out.println("TextMessage");
			} else if (message instanceof ActiveMQBytesMessage) {
				ActiveMQBytesMessage messageByte = (ActiveMQBytesMessage) message;
				String returnBuf = new String(messageByte.getContent().getData(), "UTF-8");
				System.out.println(returnBuf);
			} else {
				System.out.println("nothing");
			}
			session.commit(); // 在事务性会话中，接收到消息后必须commit，否则下次启动接收者时还会收到旧数据

			// if(message!=null){
			// System.out.println("2    " + message.getText());
			//
			// }else{
			// break;
			// System.out.println("2     无消息");
			// Thread.sleep(5000);
			// //break;
			// }
		}

		consumer.close();
		session.close();
		conn.close();
	}
}