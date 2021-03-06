package com.weisd.socket.nio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOClient {

	/* 标识数字 */
	private static int flag = 0;
	/* 缓冲区大小 */
	private static int BLOCK = 100;
	/* 接受数据缓冲区 */
	private static ByteBuffer sendbuffer = ByteBuffer.allocate(BLOCK);
	/* 发送数据缓冲区 */
	private static ByteBuffer receivebuffer = ByteBuffer.allocate(BLOCK);
	/* 服务器端地址 */
	private final static InetSocketAddress SERVER_ADDRESS = new InetSocketAddress("127.0.0.1", 9999);

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// 打开socket通道
		SocketChannel socketChannel = SocketChannel.open();
		// 设置为非阻塞方式
		socketChannel.configureBlocking(false);
		// 打开选择器
		Selector selector = Selector.open();
		// 注册连接服务端socket动作
		socketChannel.register(selector, SelectionKey.OP_CONNECT);
		// 连接
		socketChannel.connect(SERVER_ADDRESS);
		// 分配缓冲区大小内存

		Set<SelectionKey> selectionKeys;
		Iterator<SelectionKey> iterator;
		SelectionKey selectionKey;
		SocketChannel client;
		String receiveText;
		String sendText;
		int count = 0;

		while (true) {
			// 选择一组键，其相应的通道已为 I/O 操作准备就绪。
			// 此方法执行处于阻塞模式的选择操作。
			selector.select();
			// 返回此选择器的已选择键集。
			selectionKeys = selector.selectedKeys();
			// System.out.println(selectionKeys.size());
			iterator = selectionKeys.iterator();
			while (iterator.hasNext()) {
				selectionKey = iterator.next();
				if (selectionKey.isConnectable()) {
					System.out.println("client connect");
					client = (SocketChannel) selectionKey.channel();
					// 判断此通道上是否正在进行连接操作。
					// 完成套接字通道的连接过程。
					if (client.isConnectionPending()) {
						client.finishConnect();
						System.out.println("完成连接!");
						sendbuffer.clear();
						sendbuffer.put("Hello,Server\r\n".getBytes());
						sendbuffer.flip();
						client.write(sendbuffer);
					}
					client.register(selector, SelectionKey.OP_READ);
				} else if (selectionKey.isReadable()) {
					client = (SocketChannel) selectionKey.channel();
					// 将缓冲区清空以备下次读取
					receivebuffer.clear();
					// 读取服务器发送来的数据到缓冲区中
					// count = client.read(receivebuffer);
					// int limit = receivebuffer.capacity();
					// if (count > 0) {
					// receiveText = new String(receivebuffer.array(), 0,
					// count);
					// System.out.println("客户端接受服务器端数据--[" +
					// receiveText.length() + "]" + receiveText);
					// client.register(selector, SelectionKey.OP_WRITE);
					// }
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					int readInt = 0;
					System.out.println("--------------------------");
					while ((readInt = client.read(receivebuffer)) > 0) {
						// 如果获得数据长度比缓冲区大小小的话
						if (readInt < receivebuffer.capacity()) {
							// 建立一个临时byte数组,将齐长度设为获取的数据的长度
							byte[] readByte = new byte[readInt];
							// 循环向此临时数组中添加数据
							for (int i = 0; i < readInt; i++) {
								readByte[i] = receivebuffer.get(i);
							}
							// 将此数据存入byte流中
							bos.write(readByte);
						}
						// 否则就是获得数据长度等于缓冲区大小
						else {
							// 将读取到的数据写入到byte流对象中
							bos.write(receivebuffer.array());
						}
						// 将缓冲区清空，以便进行下一次存储数据
						receivebuffer.clear();
					}
					// 当循环结束时byte流中已经存储了客户端发送的所有byte数据
					String ss = new String(bos.toByteArray());
					System.out.println("接收数据[" + ss.length() + "]" + ss);

				} else if (selectionKey.isWritable()) {
					sendbuffer.clear();
					client = (SocketChannel) selectionKey.channel();
					sendText = "message from client--" + (flag++);
					sendbuffer.put(sendText.getBytes());
					// 将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
					sendbuffer.flip();
					client.write(sendbuffer);
					System.out.println("客户端向服务器端发送数据--：" + sendText);
					client.register(selector, SelectionKey.OP_READ);
				}
			}
			selectionKeys.clear();
		}
	}
}
