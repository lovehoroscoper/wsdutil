package com.test.nio.testserver1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

public class ChannelServer {

	private static final int BUFFSIZE=1024;
	private static final int TIMEOUT= 3500;
	private static final int PORT = 5678;
	public static void main(String[] args) throws IOException {
		
		Selector selector = Selector.open();//创建选择器
		//打开监听信道
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		//与端口绑定
//		serverChannel.socket().bind(new InetSocketAddress("192.168.11.80",PORT));
		serverChannel.socket().bind(new InetSocketAddress("127.0.0.1",PORT));
		//设置非阻塞模式
		serverChannel.configureBlocking(false);
		//将选择器注册到监听信道，只有非阻塞信道才可以注册,并指出该信道可以Accept
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		//创建处理协议类型
		Protocol p = new ProtocolImpl(BUFFSIZE);
		
		while(true){
			//等待某信道就绪（或超时）
			if(selector.select(TIMEOUT)==0){
				System.out.println("独自等待");
				continue;
			}
			//取得迭代器selectedKey()中包含了每个准备好某一操作信道的selectionKey
			Iterator it = selector.selectedKeys().iterator();
			
			while(it.hasNext()){
				SelectionKey key = (SelectionKey) it.next();
				try{
					if(key.isAcceptable()){
						//有客户端请求时
						//接受请求并处理 处理该key
						p.handleAccept(key);
					}
					if(key.isReadable()){
						//从客户端取数据
						p.handleRead(key);
					}
					if(key.isValid()&&key.isWritable()){
						//客户端可写时进行发送
						Thread.sleep(1000);
						
						// TODO weisd不应该放到主线程中进行，应该启用线程
						p.handlWrite(key, ByteBuffer.wrap("发送客户端。。。。".getBytes("UTF-16")));
					}
					
				}catch(Exception e){
					it.remove();
					continue;
				}
				it.remove();
			}
		}
	}
}
