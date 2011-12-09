package com.weisd.io;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;

public class TestNioSetReadTimeOut {
	private Charset charset = Charset.forName("GBK");// 创建GBK字符集
	private SocketChannel channel;

	/**
	 * 读取超时：
	 * http://sw1982.iteye.com/blog/870612
	 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4614802
	 */
	public void readHTMLContentTimeOut() {
		try {
			InetSocketAddress socketAddress = new InetSocketAddress("172.25.25.123", 9001);
			// step1:打开连接
			channel = SocketChannel.open(socketAddress);
			
			Socket sock = channel.socket();  
			sock.setSoTimeout(10000);
			
			//1.//i.读取buffer的chanel   用来设置超时
			//2.InputStream is = sock.socket().getInputStream();   
			//3.ReadableByteChannel readCh = Channels.newChannel(is);  
			
			// step2:发送请求，使用GBK编码
			channel.write(charset.encode("存储=呵呵\r\n"));
			// step3:读取数据			
			InputStream is = sock.getInputStream();   
			ReadableByteChannel readCh = Channels.newChannel(is);  
			ByteBuffer buffer = ByteBuffer.allocate(1024);// 创建1024字节的缓冲
			while (readCh.read(buffer) != -1) {
				buffer.flip();// flip方法在读缓冲区字节操作之前调用。
				
				CharBuffer  res = charset.decode(buffer);
				String ss = "" + res;
				System.out.println(ss);
				System.out.println(ss.trim());
				String s2 = ss.trim();
				System.out.println(s2);
				System.out.println("读取到一次消息");
				
				// 使用Charset.decode方法将字节转换为字符串
				buffer.clear();// 清空缓冲
			}
		} catch (IOException e) {
			System.err.println(e.toString());
		} finally {
			if (channel != null) {
				try {
					channel.close();
					System.out.println("close");
				} catch (IOException e) {
				}
			}
		}
	}
	
	/**
	 * 如果服务器不关闭连接就一直阻塞
	 */
	public void readHTMLContent() {
		try {
			InetSocketAddress socketAddress = new InetSocketAddress("172.25.25.123", 9001);
			// step1:打开连接
			channel = SocketChannel.open(socketAddress);
			// step2:发送请求，使用GBK编码
//			channel.write(charset.encode(""));
//			channel.write(charset.encode(""));
			channel.write(charset.encode("comm=呵呵\r\n"));
			// step3:读取数据
			ByteBuffer buffer = ByteBuffer.allocate(1024);// 创建1024字节的缓冲
			while (channel.read(buffer) != -1) {
				buffer.flip();// flip方法在读缓冲区字节操作之前调用。
				
				CharBuffer  res = charset.decode(buffer);
				String ss = "" + res;
				System.out.println(ss);
				System.out.println(ss.trim());
				String s2 = ss.trim();
				System.out.println(s2);
				System.out.println("读取到一次消息");
				
				// 使用Charset.decode方法将字节转换为字符串
				buffer.clear();// 清空缓冲
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (channel != null) {
				try {
					channel.close();
					System.out.println("close");
				} catch (IOException e) {
				}
			}
		}
	}

	public static void main(String[] args) {
		new TestNioSetReadTimeOut().readHTMLContent();
	}
}