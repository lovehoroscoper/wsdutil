package com.test.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

/**
 * @desc: 
 *
 *
 * @author weisd E-mail:weisd@junbao.net
 * @createtime:2011-6-22 下午10:11:55
 * @version:v1.0
 *
 */
public class TestSocket {

	private static Logger logger = Logger.getLogger(TestSocket.class);
	

	 

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedReader in = null;
		Socket socket = null;
//		PrintWriter out = null;
//		String sendMsg = "1231414\n";
//		String sendMsg = "\\u54c8\\u54c8\\u68\\u68";
		String sendMsg = "哈哈哈";
		String result = "";
		try {
			String corePlatformHost = "127.0.0.1";
			int corePlatformPort = 9999;
			int readTimeout = 10;
			socket = new Socket(corePlatformHost, corePlatformPort);
			socket.setSoTimeout(readTimeout * 1000);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
		    PrintStream out = new PrintStream(socket.getOutputStream(),true,"utf-8"); 
//			out = new PrintWriter(socket.getOutputStream(), true);
			out.println(new String(sendMsg.getBytes("UTF-8")));
			
			out.flush();
			result = in.readLine();
			logger.info("return :" + result);
		} catch (Exception e) {
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (IOException e) {
				logger.error("关闭socket连接异常:" + e.getMessage());
			}
			try {
				if (socket != null) {
					socket.close();
					socket = null;
				}
			} catch (IOException e) {
				logger.error("关闭socket连接异常" + e.getMessage());
			}
		}
	}
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		BufferedReader in = null;
//		Socket socket = null;
//		PrintWriter out = null;
////		String sendMsg = "1231414\n";
//		String sendMsg = "\\u54c8\\u54c8\\u68\\u68";
////		String sendMsg = "哈哈哈\n";
//		String result = "";
//		
//		
//		
//		try {
//			String corePlatformHost = "127.0.0.1";
//			int corePlatformPort = 9999;
//			int readTimeout = 10;
//			socket = new Socket(corePlatformHost, corePlatformPort);
//			socket.setSoTimeout(readTimeout * 1000);
//			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//			
////			out = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
//			out = new PrintWriter(socket.getOutputStream(), true);
////				out.println(new String(sendMsg.getBytes("UTF-8")));
////				out.println(new String(sendMsg.getBytes("ISO8859-1"),"ISO8859-1"));
//			
//			String ee = URLDecoder.decode(sendMsg, "UTF-8");
//			System.out.println(ee);
//			
////				out.println(new String(ee.getBytes("UTF-8")));
////				out.println(new String(sendMsg.getBytes("UTF-8")));
////				out.println(new String(sendMsg.getBytes()));
////			out.println(new String(sendMsg.getBytes("UTF-8")));
//			out.println(sendMsg);
//			
//			out.flush();
//			result = in.readLine();
//			logger.info("return :" + result);
//		} catch (Exception e) {
//		} finally {
//			if (out != null) {
//				out = null;
//			}
//			try {
//				if (in != null) {
//					in.close();
//					in = null;
//				}
//			} catch (IOException e) {
//				logger.error("关闭socket连接异常:" + e.getMessage());
//			}
//			try {
//				if (socket != null) {
//					socket.close();
//					socket = null;
//				}
//			} catch (IOException e) {
//				logger.error("关闭socket连接异常" + e.getMessage());
//			}
//		}
//	}

//		public static void main(String[] args) throws Exception{
//	        String old = "中文的";
//	        String s1 = new String(old.getBytes("gbk"),"iso-8859-1");
//	        String s2 = new String(s1.getBytes("iso-8859-1"),"gbk");
//	        String s3 = new String(s1.getBytes("GBK"),"UTF-8");
//	        System.out.println(s1);
//	        System.out.println(s2);//中文的
//	        System.out.println(s3);//中文的
//	        String str = new String(old.getBytes("gbk"),"gbk");
//	        System.out.println(str);//中文的
//	        String str22 = new String(old.getBytes("UTF-8"),"GBK");
//	        System.out.println(str22);//中文的
//	    }
	 
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
////		BufferedReader in = null;
////		Socket socket = null;
////		PrintWriter out = null;
//		String sendMsg = "1231414";
////		String sendMsg = "哈哈哈";
//		String result = "";
//			try {
//				String corePlatformHost = "127.0.0.1";
//				int corePlatformPort = 9999;
//				int readTimeout = 10;
////				socket = new Socket(corePlatformHost, corePlatformPort);
////				socket.setSoTimeout(readTimeout * 1000);
////				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
////				out = new PrintWriter(socket.getOutputStream(), true);
//				//打开监听通道
//				SocketChannel socketChannel  = SocketChannel.open(new InetSocketAddress(corePlatformHost,corePlatformPort));
//				//设置非阻塞模式
//				socketChannel.configureBlocking(false);
//				
//				//打开选择器并注册到信道
//				Selector selector = Selector.open();
//				socketChannel.register(selector,SelectionKey.OP_ACCEPT);//读取集
//				
//				
//				socketChannel.write(ByteBuffer.wrap(sendMsg.getBytes()));
//				
//			} catch (Exception e) {
//				
//			} finally {
//	
//			}
//	}

}
