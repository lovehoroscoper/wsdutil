package com.test.nio.testclient1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Timer;
import java.util.TimerTask;


public class ChannelClient {
	//注释详见com.telesound.nio.server.ClientThread.java
	private SocketChannel socketChannel;
//	private final String HOST = "192.168.11.80";
	private final String HOST = "127.0.0.1";
	private final int PORT = 5678;
	private Selector selector = null;
	private InputStream in;
	private OutputStream out;
	
	public ChannelClient() throws IOException {
		// TODO Auto-generated constructor stub
		initialize();//初始化
		comhandNet();
	}
	private void comhandNet() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				String send = "心跳访问";
				try {
					sendData(send.getBytes("UTF-16"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, 0, 10000);
	}

	//初始化客户端连接
	private void initialize() throws IOException {
		// TODO Auto-generated method stub
		//打开监听通道
		socketChannel  = SocketChannel.open(new InetSocketAddress(HOST,PORT));
		//设置非阻塞模式
		socketChannel.configureBlocking(false);
		
		//打开选择器并注册到信道
		selector = Selector.open();
		socketChannel.register(selector,SelectionKey.OP_READ|SelectionKey.OP_WRITE);//读取集
		//启动读取的线程----
		new ChannelReadClient(selector);
	}
	/**
	 * 发送数据到服务器
	 * @param byout 
	 * @throws IOException 
	 */
	public void sendData(byte[] byout) throws IOException{
		ByteBuffer writeBuf = ByteBuffer.wrap(byout);
		socketChannel.write(writeBuf);
//		out = socketChannel.socket().getOutputStream();
//		out.write(byout);
	}
	public static void main(String[] args) throws IOException {
		ChannelClient c = new ChannelClient();
		String send = "客户端访问";
		c.sendData(send.getBytes("UTF-16"));
	}
}
