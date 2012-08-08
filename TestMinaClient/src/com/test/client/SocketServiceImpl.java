package com.test.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

/**
 * Socket实现类
 * 
 * @author：liujiangtao
 * @since：2011-4-27 下午02:35:44
 * @version:
 */
public class SocketServiceImpl  {
	private static Logger logger = Logger.getLogger(SocketServiceImpl.class);

	public String sendMessageToCore(String ip, String port, String message, int timeout) {
		int timeoutTemp = 10;
		if (0 != timeout) {
			timeoutTemp = timeout;
		}
		String result = null;
		BufferedReader in = null;
		Socket socket = null;
		PrintWriter out = null;
		try {
			socket = new Socket(ip, Integer.parseInt(port));
//			socket.setSoTimeout(timeoutTemp * 1000);
			socket.setSoTimeout(30000 * 1000);
			out = new PrintWriter(socket.getOutputStream(), true);			
			out.println(new String(message.getBytes("GBK")));
			out.flush();
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				result = in.readLine();
				
				if(null == result){
					logger.error("kong:");
				}else{
					logger.error(" not kong:");
				}
				
				// 6.27 weisd 经讨论,只要发生异常返回失败,损失在接受范围内
			} catch (SocketTimeoutException e) {
				logger.error("SocketUtils:socket接收超时(返回失败)|发送信息:" + message, e);
				// result = "";
			} catch (IOException e) {// 只要信息发送过去了,接收结果未正常
				// result = "";
				logger.error("SocketUtils:socket无法获取到核心返回|发送信息:" + message, e);
			}
		} catch (NumberFormatException e) {
			logger.error("SocketUtils:无法创建连接异常.", e);
		} catch (UnknownHostException e) {
			logger.error("SocketUtils:无法创建连接异常.", e);
		} catch (SocketTimeoutException e) {
			// result = "";
			logger.error("SocketUtils:socket发送超时(返回失败)|发送信息:" + message, e);
		} catch (IOException e) {
			logger.error("SocketUtils out:IO流异常", e);
		} finally {
//			if (out != null) {
//				out.close();
//				out = null;
//			}
//			try {
//				if (in != null) {
//					in.close();
//					in = null;
//				}
//			} catch (IOException e) {
//				logger.error("关闭socket连接中BufferedReader异常:" + e.getMessage());
//			}
//			try {
//				if (socket != null) {
//					socket.close();
//					socket = null;
//				}
//			} catch (IOException e) {
//				logger.error("关闭socket连接中Socket异常:" + e.getMessage());
//			}
		}
		logger.info("收单平台(接收到)核心平台返回消息：" + result);
		return result;
	}

	
//	public static void main(String[] args) throws IOException {
//		SocketServiceImpl t = new SocketServiceImpl();
////		String r = URLEncoder.encode("水水水", "UTF-8");
////		String ss = "comm=8001&version=1.0&onlineid=108721&agentid=PHONE&ordersource=2&orderid=20111207130252&mobilenum=13023610040&chargeamount=1&payamount=1.1&ordertime=20111207130252&mark=" + r + "&receivetime=20111207130245";
////		String r = URLEncoder.encode("水水水", "UTF-8");
//		String ss = "comm=8001&version=1.0&onlineid=108721&agentid=PHONE&ordersource=2&orderid=20111207130252&mobilenum=13023610040&chargeamount=1&payamount=1.1&ordertime=20111207130252&mark=解决&receivetime=20111207130245";
//		ss = URLDecoder.decode(ss, "UTF-8");
//		System.out.println(ss);
//		String aa = t.sendMessageToCore("172.25.25.162", "9012", ss, 20);
//		
//		System.out.println(aa);
//	}
	public static void main(String[] args) throws IOException {
		SocketServiceImpl t = new SocketServiceImpl();
//		String r = URLEncoder.encode("水水水", "UTF-8");
//		String ss = "comm=8001&version=1.0&onlineid=108721&agentid=PHONE&ordersource=2&orderid=20111207130252&mobilenum=13023610040&chargeamount=1&payamount=1.1&ordertime=20111207130252&mark=" + r + "&receivetime=20111207130245";
//		String r = URLEncoder.encode("水水水", "UTF-8");
		String ss = "version=1.0&onlineid=107620&agentid=DE201108091428260543&ordersource=2&orderid=201201111018220&mobilenum=15201386036&chargeamount=30&payamount=29.7&ordertime=20120111101822&mark=test";
		String aa = t.sendMessageToCore("172.25.25.123", "9001", ss, 20);
		
		System.out.println(aa);
	}
}
