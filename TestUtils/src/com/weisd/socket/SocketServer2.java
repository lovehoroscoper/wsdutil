//package com.weisd.socket;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//
//public class SocketServer2 {
//	public SocketServer2() {
//		Socket incoming;
//		ServerSocket so;
//		try {
//			so = new ServerSocket(9731);
//			System.out.println("等待客户端连接");
//
//			while (true) {
//				try {
//					incoming = so.accept();
//					System.out.println("已连接客户端");
//				} catch (IOException e) {
//					so.close();
//					e.printStackTrace();
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void main(String[] args) {
//		// PropertyConfigurator.configure("E:\\Myeclipse\\workspace\\oufeitest\\src\\oufei\\test\\log4j.properties");
//		new SocketServer2();
//	}
//
//	private static class GetInfo implements Runnable { // �߳���
//		private Socket incoming;
//		private String s = null;
//		private BufferedReader b;
//		private BufferedWriter c;
//		Thread t;
//
//		public GetInfo(Socket incoming) {
//			try {
//				this.incoming = incoming;
//				b = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
//				// c=new BufferedWriter(new
//				// OutputStreamWriter(incoming.getOutputStream()));
//				t = new Thread(this);
//				t.start();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		public void run() {
//			try {
//				Logger logger = Logger.getLogger(SocketServer.class);
//				String lines;
//				String line = "";
//				while ((lines = b.readLine()) != null) {
//					line += lines;
//				}
//				System.out.println("line----" + line);
//				System.out.println(line.length());
//				if (line.length() % 63 != 0) {
//					logger.info("交换平台发送的报文长度不正确！");
//					c.write("");
//
//				} else {
//					List<String> list1 = new ArrayList<String>();
//					for (int i = 0; i < line.length(); i += 63) {
//						list1.add(line.substring(i, i + 63));
//					}
//					Iterator list = list1.iterator();
//					while (list.hasNext()) {
//						String co = "";
//						co = (String) list.next();
//						logger.info("后台socket发送给我的内容：" + co);
//						String post_url = "http://esales1.ofcard.com:8088/onlineorder.do";
//						String content = ConcatPackage.getPackage(co);
//						// 用post方式发送http请求
//						String xmlcontent = "";
//						try {
//							xmlcontent = Sender.readContentFromPost(post_url, content);
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							System.out.println("不好啦，发送http请求出问题啦，哦也！");
//							e.printStackTrace();
//						}
//						if (xmlcontent.equals("04")) {
//							logger.info("号段错误");
//							c.write("111111 ");
//						} else if (xmlcontent.equals("14")) {
//							logger.info("金额错误");
//							c.write("111111 ");
//						} else {
//							System.out.println("查看取得的xml字符串是否为空：" + xmlcontent);
//							System.out.println("发送欧非任务已完成");
//							// 处理接收xml,并以socket发送到交换平台
//							Decodexml decoderxml = new Decodexml();
//							String packagesend = decoderxml.xmlElements(xmlcontent); // xml字符串被解析并被拼装为package
//							try {
//								System.out.println("组装的发送后台的SOCKET为：" + "[" + packagesend + "]");
//								logger.info("c.write前" + packagesend);
//								c = new BufferedWriter(new OutputStreamWriter(incoming.getOutputStream()));
//								c.write(packagesend);
//								c.toString();
//								System.out.println("c.toString" + c.toString());
//								// c.newLine();
//								// c.flush();
//								// c.close();
//								logger.info("c.write后----------------");
//								// Communicater.sendRequest(packagesend);
//							} catch (IOException e) {
//								System.out.println("发送给后台的socket失败！");
//								e.printStackTrace();
//							}
//						}
//
//					}
//
//				}
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//		}
//	}
//
//}