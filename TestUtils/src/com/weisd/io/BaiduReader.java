package com.weisd.io;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.net.InetSocketAddress;
import java.io.IOException;

public class BaiduReader {
	private Charset charset = Charset.forName("GBK");// 创建GBK字符集
	private SocketChannel channel;

	public void readHTMLContent() {
		try {
//			InetSocketAddress socketAddress = new InetSocketAddress("www.baidu.com", 80);
//			InetSocketAddress socketAddress = new InetSocketAddress("172.25.25.162", 9012);
			InetSocketAddress socketAddress = new InetSocketAddress("172.25.25.123", 9001);
//			InetSocketAddress socketAddress = new InetSocketAddress("172.25.25.123", 6003);
			// step1:打开连接
			channel = SocketChannel.open(socketAddress);
			
			channel.socket().setSoTimeout(10000);  

//			channel.
//			socketAddress.
			
			// step2:发送请求，使用GBK编码
//			channel.write(charset.encode("comm=8101&version=1.0&hfserialid=HF011112081711040557&channelserialid=&sendserialid=&dealtime=2011-12-09 00:05:56&dealamount=5000&resultno=0000&resultmsg=呵呵\r\n"));
//			channel.write(charset.encode("comm=8101&version=1.0&hfserialid=HF011112081711040557&channelserialid=&sendserialid=&dealtime=2011-12-09 00:05:56&dealamount=5000&resultno=0000&resultmsg=呵呵\r\n"));
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

	public static void main(String[] args) {
		new BaiduReader().readHTMLContent();
	}
}