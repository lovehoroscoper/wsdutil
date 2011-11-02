package com.test.client;

import java.net.InetSocketAddress;
import java.util.Date;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.test.client.vo.User;

/**
 * @desc:
 * 
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @createtime:2011-5-29 ����03:07:20
 * @version:v1.0
 * 
 */
public class TestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NioSocketConnector connector = new NioSocketConnector();

		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory();
		textLineCodecFactory.setDecoderMaxLineLength(4000);
		chain.addLast("myChin", new ProtocolCodecFilter(textLineCodecFactory));
		connector.setHandler(new MinaDBCoreGWHandler());
		connector.setConnectTimeoutMillis(10000);

		// connector.getSessionConfig().setWriteTimeout(3000);
		// connector.getSessionConfig().setWriteTimeout(3000);
		connector.getSessionConfig().setWriteTimeout(5);
		int sss = connector.getSessionConfig().getWriteTimeout();

		 connector.getSessionConfig().setTcpNoDelay(true);
		// connector.getSessionConfig().setSoLinger(-1);

		// ConnectFuture cf = connector.connect(new
		// InetSocketAddress("192.168.1.111", 80));
		// ConnectFuture cf = connector.connect(new
		// InetSocketAddress("192.168.2.201", 9001));
		// ConnectFuture cf = connector.connect(new
		// InetSocketAddress("192.168.2.201", 9001));
		ConnectFuture cf = connector.connect(new InetSocketAddress("127.0.0.1", 9999));
//		 ConnectFuture cf = connector.connect(new InetSocketAddress("127.0.0.1", 8080));

		cf.awaitUninterruptibly();
		try {
			cf.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		IoSession session = cf.getSession();

		// User u = new User();
		// u.setId(6);
		// u.setName("333");
		// session.setAttribute("user", u);
		//
		// System.out.println();
		// System.out.println();
		// System.out.println();

		// String hfserialid = "JB0000";
		// for (int i = 0; i < 10; i++) {
		// String orderid = "" + (i);
		// String req = "hfserialid=" + hfserialid + i+
		String ss = "HEARTBEAT_REQUEST";
		System.out.println("start time:" + new Date().getTime());
		WriteFuture writeResult = session.write(ss);
		writeResult.awaitUninterruptibly(5000);
		
		writeResult.join();
		
		if (writeResult.isWritten()){
			System.out.println("111111");
			//return;
	
		}else {
	
			// 处理
			System.out.println("22222:");
	
		}

//		writeResult.addListener(new IoFutureListener() {
//
//			@Override
//			public void operationComplete(IoFuture future) {
//				WriteFuture wfuture = (WriteFuture) future;
//
//				if (wfuture.isWritten()){
//					System.out.println("111111");
//					return;
//
//				}else {
//
//					// 处理
//					System.out.println("22222:");
//
//				}
//			}
//		});

		System.out.println(writeResult.isWritten());
		System.out.println(writeResult.isDone());
		// session.close(true);

		System.out.println();

	}

}
