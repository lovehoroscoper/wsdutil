package com.test.nio.testclient1;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class ChannelReadClient extends Thread {

	private Selector selector;
	
	public ChannelReadClient(Selector selector) {
		this.selector = selector;
		new Thread(this).start();
	}

	public void run() {
		// TODO Auto-generated method stub
		try{
			while(selector.select()>0){//有事件发生时
				Iterator it = selector.selectedKeys().iterator();//对可用通道操作对应的selectedKey迭代
				while(it.hasNext()){
					SelectionKey skey = (SelectionKey) it.next();
					if(skey.isValid()&&skey.isReadable()){//如果有效且可读
						
						// TODO 应该使用线程池 对后续数据进行处理
						
						//读取channel中数据
						SocketChannel channel = (SocketChannel) skey.channel();
						ByteBuffer bf = ByteBuffer.allocate(4000000);
						channel.read(bf);
						bf.flip();
						// 将字节转化为为UTF-16的字符串   
			            String receivedString=Charset.forName("UTF-16").newDecoder().decode(bf).toString();
			            
			            // 控制台打印出来
			            System.out.println("接收到来自服务器"+channel.socket().getRemoteSocketAddress()+"的信息:"+receivedString);
			            
			            //为下次读取准备
//			            skey.interestOps(SelectionKey.OP_READ|SelectionKey.OP_WRITE);
			            skey.interestOps(SelectionKey.OP_READ|SelectionKey.OP_WRITE);
					}
					it.remove();//删除已经处理的SelectionKey
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
